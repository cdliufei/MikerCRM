package com.mnker.crm.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.mnker.crm.LoginActivity;
import com.mnker.crm.R;
import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.http.HttpXUtils;
import com.mnker.crm.http.URLS;
import com.mnker.crm.http.json.AddNewPlanActData;
import com.mnker.crm.http.json.GetPlanParam;
import com.mnker.crm.http.json.LoginParam;
import com.mnker.crm.http.json.PunchCard;
import com.mnker.crm.http.json.ReAccount;
import com.mnker.crm.http.json.ReContacts;
import com.mnker.crm.http.json.ReContactsData;
import com.mnker.crm.http.json.ReLogin;
import com.mnker.crm.http.json.RePlan;
import com.mnker.crm.http.json.RePlanAct;
import com.mnker.crm.http.json.RePlanActData;
import com.mnker.crm.http.json.RePlanData;
import com.mnker.crm.http.json.util.JsonUtils;
import com.mnker.crm.tool.AsyncTaskControl;
import com.mnker.crm.tool.AsyncTaskControl.IAirControl;
import com.mnker.crm.tool.TaskNo;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.widget.util.PreferencesService;
import com.mnker.crm.widget.util.Util;

public class LoginFragment extends Fragment implements OnClickListener, UiCallBack {
	private LoginActivity mContext;
	private EditText mUsernam;
	private EditText mPassWord;
	private EditText mWorkType;
	private TextView mConnectSetting;
	private ImageView mLogin;
	private String imeiString;
	private List<HashMap<String, String>> groupNameList;
	private Spinner groupSpinner;
	protected String mSelecteGroupName = "";
	private int taskNo;
	public static List<ReContactsData> mContacts;
	public static String token;
	private String url;
	public static List<RePlanActData> mPlanActs;
	public static List<RePlanData> mRePlan;
	private String updateTime;
	private int contactStartCount = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_login, container, false);

		url = PreferencesService.getInfo("url");
		if (TextUtils.isEmpty(url)) {
			url = URLS.defaultUrl;
			URLS.baseUrl = URLS.defaultUrl;
			PreferencesService.setPreferences("url", url);
		}
		initView(view);
		ViewUtils.inject(this, view);
		TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
		imeiString = tm.getDeviceId();
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = (LoginActivity) activity;
		super.onAttach(activity);
	}

	private void initView(View view) {
		mUsernam = (EditText) view.findViewById(R.id.et_username);
		mPassWord = (EditText) view.findViewById(R.id.et_password);
		// mWorkType = (EditText) view.findViewById(R.id.et_worktype);
		groupSpinner = (Spinner) view.findViewById(R.id.et_worktype);
		mConnectSetting = (TextView) view.findViewById(R.id.tv_connect_setting);
		mLogin = (ImageView) view.findViewById(R.id.iv_login);
		if (PreferencesService.getInfo("username") != null) {
			mUsernam.setText(PreferencesService.getInfo("username"));
		}
		if (PreferencesService.getInfo("pwd") != null) {
			mPassWord.setText(PreferencesService.getInfo("pwd"));
		}
		mLogin.setOnClickListener(this);
		mConnectSetting.setOnClickListener(this);
		initSpinner();

		updateTime = PreferencesService.getInfo("updateTime");
		if (TextUtils.isEmpty(updateTime)) {
			updateTime = "0";
		}
	}

	private int getselectIndex() {
		int index = 0;
		for (int i = 0; i < groupNameList.size(); i++) {
			HashMap<String, String> temp = groupNameList.get(i);
			if (temp.get("typeEn") != null && mSelecteGroupName.equals(temp.get("typeEn"))) {
				index = i;
			}
		}
		return index;
	}

	private void initSpinner() {
		String[] fromType = new String[] { "type" };
		int[] toType = new int[] { android.R.id.text1 };
		groupNameList = getTypeData();
		mSelecteGroupName = PreferencesService.getInfo("mSelecteGroupName");
		SimpleAdapter mTypeAdapter = new SimpleAdapter(mContext, groupNameList, android.R.layout.simple_spinner_item, fromType, toType);
		mTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSpinner.setAdapter(mTypeAdapter);
		if (mSelecteGroupName != null) {
			groupSpinner.setSelection(getselectIndex());
		} else {
			groupSpinner.setSelection(0);
		}

		groupSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mSelecteGroupName = groupNameList.get(position).get("typeEn");
				PreferencesService.setPreferences("mSelecteGroupName", mSelecteGroupName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				mSelecteGroupName = groupNameList.get(0).get("typeEn");
				PreferencesService.setPreferences("mSelecteGroupName", mSelecteGroupName);
			}
		});
	}

	private List<HashMap<String, String>> getTypeData() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("type", "工程师");
		map.put("typeEn", "engineer");
		list.add(map);
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("type", "销售");
		map2.put("typeEn", "sale");
		list.add(map2);
		return list;
	}

	private void login() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				LoginParam loginParam = new LoginParam();
				// loginParam.username = "test";
				// loginParam.password = "123456";
				loginParam.username = mUsernam.getText().toString().trim();
				loginParam.password = mPassWord.getText().toString().trim();
				loginParam.imei = imeiString;
				loginParam.module = mSelecteGroupName;

				String ret = HttpXUtils.getInfoByPostObject(URLS.login, loginParam, null);

				return ret;
			}
		};

		asyncTaskControl.execute(TaskNo.LoginFragment_LOGIN, icontrol, this);
	}

	protected Dialog onCreateDialog(int id) {

		final Dialog connectDialog = new Dialog(getActivity(), R.style.my_dialog_theme);
		View view = getActivity().getLayoutInflater().inflate(R.layout.client_setting_dialog, null);
		connectDialog.setContentView(view);

		TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
		TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
		final EditText connectHttp = (EditText) view.findViewById(R.id.et_connect_http);
		if (!TextUtils.isEmpty(url)) {
			connectHttp.setText(url);
		} else {
			URLS.baseUrl = URLS.defaultUrl;
			connectHttp.setText(URLS.baseUrl);
		}
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				connectDialog.dismiss();
			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				connectDialog.dismiss();
				if (!TextUtils.isEmpty(connectHttp.getText().toString())) {
					PreferencesService.setPreferences("url", connectHttp.getText().toString().trim());
					URLS.baseUrl = PreferencesService.getInfo("url");
				}
			}
		});

		connectDialog.setCancelable(false);
		connectDialog.setCanceledOnTouchOutside(false);
		connectDialog.show();
		return connectDialog;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_login:

			login();
			// mContext.loginSuccess();

			break;
		case R.id.tv_connect_setting:
			onCreateDialog(100);
			break;

		default:
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void updateUi(int taskNo, String ret) {
		if (!TextUtils.isEmpty(ret)) {
			switch (taskNo) {
			case TaskNo.LoginFragment_LOGIN:
				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						PreferencesService.setPreferences("username", mUsernam.getText().toString().trim());
						PreferencesService.setPreferences("pwd", mPassWord.getText().toString().trim());
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.login_success),
							// 0).show();
							ReLogin reLogin = JsonUtils.getLoginResult(ret);
							// 保存 用户信息 token
							token = reLogin.data.token;
							PreferencesService.setPreferences("token", token);
							PreferencesService.setPreferences("imei", imeiString);
							// PreferencesService.setPreferences("uuid",
							// "2bdbf972-ee06-4caf-9f12-628d9646f201");
							// saveData("username", reLogin.data.name);
							PreferencesService.setPreferences("deptName", reLogin.data.deptName);
							PreferencesService.setInt("deptId", reLogin.data.deptId);
							for (String key : reLogin.actTypes.keySet()) {
								String valueString = reLogin.actTypes.get(key);
								Log.d("zaokun", "key == " + key + "valueString=================" + valueString);
								PreferencesService.setInt("actTypeskey", Integer.parseInt(key));
								PreferencesService.setPreferences("actTypesvalue", valueString);
								break;
							}
							// 同步数据
							synServerData();
							// 获取联系人
							getContctsData();
							// 获取客户信息
							getAccountData();
							// 获取客户信息
							getActPlanData();
							mContext.loginSuccess();
						} else {
							Toast.makeText(mContext, mContext.getResources().getString(R.string.login_err), 0).show();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(mContext, mContext.getResources().getString(R.string.login_err), 0).show();
				}

				break;
			case TaskNo.LoginFragment_getplan:
				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.syncontact_success),
							// 0).show();
							// RePlan rePlan = JsonUtils.getPlanResult(ret);
							// // 保存周计划到数据库
							// mRePlan = rePlan.data;

						} else {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.login_err),
							// 0).show();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(mContext, mContext.getResources().getString(R.string.login_err), 0).show();
				}
				break;
			case TaskNo.LoginFragment_getcontacts:
				// try {
				// JSONObject retJsonObject = new JSONObject(ret);
				// if (retJsonObject != null &&
				// !retJsonObject.isNull("success")) {
				// if
				// (retJsonObject.getString("success").equalsIgnoreCase("true"))
				// {
				// Toast.makeText(mContext,
				// mContext.getResources().getString(R.string.syncontact_success),
				// 0).show();
				// // ReContacts reContacts =
				// // JsonUtils.getContactsResult(ret);
				// // // 保存联系人
				// // mContacts = reContacts.data;
				// // 同步数据
				// } else {
				// // Toast.makeText(mContext,
				// // mContext.getResources().getString(R.string.login_err),
				// // 0).show();
				// }
				// }
				//
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// Toast.makeText(mContext,
				// mContext.getResources().getString(R.string.login_err),
				// 0).show();
				// }
				break;
			case TaskNo.LoginFragment_getaccount:

				break;
			case TaskNo.LoginFragment_getactplan:
				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.syncontact_success),
							// 0).show();
							// RePlanAct rePlanAct =
							// JsonUtils.getPlanActResult(ret);
							// // 保存联系人
							// mPlanActs = rePlanAct.data;
							// 同步数据
						} else {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.login_err),
							// 0).show();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(mContext, mContext.getResources().getString(R.string.login_err), 0).show();
				}
				break;
			default:
				break;
			}
		}

	}

	private void synServerData() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				GetPlanParam getPlanParam = new GetPlanParam();
				// updateTime = "2014-01-01 00:00:00";
				getPlanParam.timestamp = updateTime;
				PreferencesService.setPreferences("updateTime", Util.getNowTime());
				PreferencesService.setPreferences("weekuptime", Util.getNowTime());
				// getPlanParam.timestamp = Util.getNowTime();
				getPlanParam.start = 0;
				getPlanParam.counts = 1000;

				String ret = HttpXUtils.getInfoByPostObject(URLS.getPlan, getPlanParam, PreferencesService.getInfo("token"));

				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.syncontact_success),
							// 0).show();
							RePlan rePlan = JsonUtils.getPlanResult(ret);
							DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
							helper.insertWeekPlanData(rePlan.data, updateTime);
						} else {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.login_err),
							// 0).show();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// Toast.makeText(mContext,
					// mContext.getResources().getString(R.string.login_err),
					// 0).show();
				}

				return ret;
			}
		};

		asyncTaskControl.execute(TaskNo.LoginFragment_getplan, icontrol, this);
	}

	private void getContctsData() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				boolean hasMore = true;
				while (hasMore) {
					hasMore = syncContact();
					Log.d("zaokun", "getContctsData hasMore===" + hasMore);
				}
				return "success";
			}
		};

		asyncTaskControl.execute(TaskNo.LoginFragment_getcontacts, icontrol, this);
	}

	private boolean syncContact() {
		boolean hasGetMore = false;
		GetPlanParam getPlanParam = new GetPlanParam();
		// getPlanParam.timestamp = "2014-01-01 00:00:00";
		getPlanParam.timestamp = updateTime;

		getPlanParam.start = contactStartCount;
		getPlanParam.counts = 1000;
		String ret = HttpXUtils.getInfoByPostObject(URLS.getContacts, getPlanParam, PreferencesService.getInfo("token"));

		try {
			JSONObject retJsonObject = new JSONObject(ret);
			if (retJsonObject != null && !retJsonObject.isNull("success")) {
				if (retJsonObject.getString("success").equalsIgnoreCase("true")) {

					ReContacts reContacts = JsonUtils.getContactsResult(ret);
					// 保存联系人
					// mContacts = reContacts.data;
					DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
					helper.insertContactsData(reContacts.data, updateTime);
					contactStartCount = contactStartCount + reContacts.data.size();
					mContext.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, "已同步" + contactStartCount + "条", 0).show();
						}
					});
					if (reContacts.data.size() < 1000) {
						hasGetMore = false;
					} else {
						hasGetMore = true;
					}
				} else {
					// Toast.makeText(mContext,
					// mContext.getResources().getString(R.string.login_err),
					// 0).show();
					hasGetMore = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			hasGetMore = false;
		}
		return hasGetMore;
	}

	private void getAccountData() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				GetPlanParam getPlanParam = new GetPlanParam();
				// getPlanParam.timestamp = "2014-01-01 00:00:00";
				getPlanParam.timestamp = updateTime;
				getPlanParam.start = 0;
				getPlanParam.counts = 10000;

				String ret = HttpXUtils.getInfoByPostObject(URLS.getAccount, getPlanParam, PreferencesService.getInfo("token"));
				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.syncontact_success),
							// 0).show();
							ReAccount reAccount = JsonUtils.getAccountResult(ret);
							// 保存客户信息
							DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
							helper.insertAccountsData(reAccount.data, updateTime);
						} else {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.login_err),
							// 0).show();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// Toast.makeText(mContext,
					// mContext.getResources().getString(R.string.login_err),
					// 0).show();
				}
				return ret;
			}
		};

		asyncTaskControl.execute(TaskNo.LoginFragment_getaccount, icontrol, this);
	}

	private void getActPlanData() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				GetPlanParam getPlanParam = new GetPlanParam();
				// getPlanParam.timestamp = "0";
				getPlanParam.timestamp = updateTime;
				getPlanParam.start = 0;
				getPlanParam.counts = 1000;

				String ret = HttpXUtils.getInfoByPostObject(URLS.getAct, getPlanParam, PreferencesService.getInfo("token"));
				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.syncontact_success),
							// 0).show();
							RePlanAct rePlanAct = JsonUtils.getPlanActResult(ret);
							// 保存天计划
							// mPlanActs = rePlanAct.data;
							DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
							helper.insertActPlanData(rePlanAct.data, updateTime, "0");
						} else {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.login_err),
							// 0).show();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// Toast.makeText(mContext,
					// mContext.getResources().getString(R.string.login_err),
					// 0).show();
				}
				return ret;
			}
		};

		asyncTaskControl.execute(TaskNo.LoginFragment_getactplan, icontrol, this);
	}

	public void skipLogin(Context context) {
		mContext = (LoginActivity) context;

		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		imeiString = tm.getDeviceId();
		updateTime = PreferencesService.getInfo("updateTime");
		if (TextUtils.isEmpty(updateTime)) {
			updateTime = "0";
		}
		// 同步数据
		synServerData();
		// 获取联系人
		getContctsData();
		// 获取客户信息
		getAccountData();
		// 获取客户信息
		getActPlanData();

		// 同步打卡信息

		synPunchCardInfo();
		// 同步计划信息

		synActPlansInfo();

	}

	private void synPunchCardInfo() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				List<PunchCard> punchList = getOfflinePunchFromDb();
				for (PunchCard punchCard : punchList) {
					punchCard.signTime = Util.getPunchTime(punchCard.signTime);
				}
				String ret = HttpXUtils.getInfoByPostObject(URLS.uploadSign, punchList, PreferencesService.getInfo("token"));
				return ret;
			}

		};
		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String ret) {
				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// getResources().getString(R.string.daka_success),
							// 0).show();
							DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(mContext);
							List<PunchCard> punchList = getOfflinePunchFromDb();
							for (PunchCard punchCard2 : punchList) {
								dBhelper.updateOfflinePunchInfoSuccess(punchCard2);
							}
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// Toast.makeText(mContext,
					// getResources().getString(R.string.daka_fail), 0).show();

				}
			}
		};
		asyncTaskControl.execute(TaskNo.PunchCardActivity_uoload, icontrol, uiCallBack);
	}

	private List<PunchCard> getOfflinePunchFromDb() {
		DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(mContext);
		return dBhelper.queryOfflinePunchInfo();
	}

	private void synActPlansInfo() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				List<RePlanActData> rePlanActDatas = getOfflineActPlansFromDb();
				List<AddNewPlanActData> uPlanActDatas = coverToAddNewPlan(rePlanActDatas);
				String ret = HttpXUtils.getInfoByPostObject(URLS.uploadAct, uPlanActDatas, PreferencesService.getInfo("token"));
				return ret;
			}

		};
		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String ret) {
				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// getResources().getString(R.string.daka_success),
							// 0).show();
							DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(mContext);
							List<RePlanActData> rePlanActDatas = getOfflineActPlansFromDb();
							for (RePlanActData rePlanActData : rePlanActDatas) {
								dBhelper.updateOfflineActPlanInfoSuccess(rePlanActData, 0);
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// Toast.makeText(mContext,
					// getResources().getString(R.string.daka_fail), 0).show();

				}
			}
		};
		asyncTaskControl.execute(TaskNo.PunchCardActivity_uoload, icontrol, uiCallBack);
	}

	protected List<AddNewPlanActData> coverToAddNewPlan(List<RePlanActData> rePlanActDatas) {
		List<AddNewPlanActData> list = new ArrayList<AddNewPlanActData>();
		for (RePlanActData rePlanActData : rePlanActDatas) {
			AddNewPlanActData addNewPlanActData = new AddNewPlanActData();
			addNewPlanActData.accountId = rePlanActData.accountId;
			addNewPlanActData.accountName = rePlanActData.accountName;
			addNewPlanActData.name = rePlanActData.name;
			addNewPlanActData.activityId = rePlanActData.accountId;
			addNewPlanActData.actvtStatus = rePlanActData.actvtStatus;
			addNewPlanActData.assignedUserId = rePlanActData.assignedUserId;
			addNewPlanActData.createUserId = rePlanActData.createUserId;
			addNewPlanActData.contactName = rePlanActData.contactName;
			addNewPlanActData.contactId = rePlanActData.contactId;
			addNewPlanActData.deptId = rePlanActData.deptId;
			addNewPlanActData.isAppAdd = 0;
			addNewPlanActData.isRemind = rePlanActData.isRemind;
			addNewPlanActData.planName = rePlanActData.planName;
			addNewPlanActData.planId = rePlanActData.planId;
			addNewPlanActData.planStartTime = rePlanActData.planStartTime;
			addNewPlanActData.reportTime = rePlanActData.planStartTime;
			addNewPlanActData.typeId = PreferencesService.getInt("actTypeskey");
			addNewPlanActData.typeName = rePlanActData.typeName;
			addNewPlanActData.uuid = rePlanActData.uuid;
			addNewPlanActData.createTime = rePlanActData.createTime;
			addNewPlanActData.type = "02";
			list.add(addNewPlanActData);
		}
		return list;

	}

	private List<RePlanActData> getOfflineActPlansFromDb() {
		DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(mContext);
		return dBhelper.queryOfflineActPlanInfo();
	}
}
