package com.mnker.crm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.fragment.DayScheduleListFragment;
import com.mnker.crm.fragment.LoginFragment;
import com.mnker.crm.fragment.MainFragment;
import com.mnker.crm.fragment.WorkScheduleFragment;
import com.mnker.crm.http.HttpXUtils;
import com.mnker.crm.http.URLS;
import com.mnker.crm.http.json.AddNewPlanActData;
import com.mnker.crm.http.json.GetPlanParam;
import com.mnker.crm.http.json.PunchCard;
import com.mnker.crm.http.json.ReAccount;
import com.mnker.crm.http.json.ReContacts;
import com.mnker.crm.http.json.ReLogin;
import com.mnker.crm.http.json.RePlan;
import com.mnker.crm.http.json.RePlanAct;
import com.mnker.crm.http.json.RePlanActData;
import com.mnker.crm.http.json.util.JsonUtils;
import com.mnker.crm.tool.AsyncTaskControl;
import com.mnker.crm.tool.TaskNo;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.tool.AsyncTaskControl.IAirControl;
import com.mnker.crm.widget.CalendarView;
import com.mnker.crm.widget.util.PreferencesService;
import com.mnker.crm.widget.util.Util;

public class ScheduleActivity extends FragmentActivity implements OnClickListener, UiCallBack {

	private WorkScheduleFragment mCalendarViewFm;
	private DayScheduleListFragment mDayTaskFm;
	private Context mContext;
	private String imeiString;
	private String updateTime;
	private ImageView ivScheduleList;
	private ImageView ivScheduleCalendar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_manage_content_frame);
		mContext = this;
		initView();
		addView();
	}

	private void initView() {
		TextView tvShAdd = (TextView) findViewById(R.id.tv_schedule_add);
		TextView tvShQuery = (TextView) findViewById(R.id.tv_schedule_qurey);
		TextView tvShSync = (TextView) findViewById(R.id.tv_schedule_syn);
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		ivScheduleList = (ImageView) findViewById(R.id.iv_schedule_list);
		ivScheduleCalendar = (ImageView) findViewById(R.id.iv_schedule_calandar);

		tvShAdd.setOnClickListener(this);
		tvShQuery.setOnClickListener(this);
		tvShSync.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		ivScheduleList.setOnClickListener(this);
		ivScheduleCalendar.setOnClickListener(this);

		mCalendarViewFm = new WorkScheduleFragment();
		mDayTaskFm = new DayScheduleListFragment();
		changeState(0);
	}

	private void addView() {
		FragmentManager fm = getSupportFragmentManager();
		String tag = String.valueOf(R.id.content_frame);
		// 执行替换
		FragmentTransaction trans = fm.beginTransaction();
		trans.replace(R.id.content_frame, mCalendarViewFm, tag);
		// 不存在时，添加到stack，避免切换时，先前的被清除{fm.getFragments()}
		// {存在时，不添加，避免BackStackEntry不断累加}
		// trans.addToBackStack(tag);
		trans.commit();
	}

	public void daySchedule() {
		FragmentManager fm = getSupportFragmentManager();
		String tag = String.valueOf(R.id.content_frame);
		// 执行替换
		FragmentTransaction trans = fm.beginTransaction();
		trans.replace(R.id.content_frame, mDayTaskFm, tag);
		// 不存在时，添加到stack，避免切换时，先前的被清除{fm.getFragments()}
		// {存在时，不添加，避免BackStackEntry不断累加}
		// trans.addToBackStack(tag);
		trans.commit();
	}

	public void daySchedule(String queryKey) {
		FragmentManager fm = getSupportFragmentManager();
		String tag = String.valueOf(R.id.content_frame);
		// 执行替换
		FragmentTransaction trans = fm.beginTransaction();
		trans.replace(R.id.content_frame, new DayScheduleListFragment(queryKey), tag);
		// 不存在时，添加到stack，避免切换时，先前的被清除{fm.getFragments()}
		// {存在时，不添加，避免BackStackEntry不断累加}
		trans.addToBackStack(tag);
		trans.commit();
	}

	/**
	 * 执行内容切换
	 * 
	 * @param fm
	 * @param mLoginFragment
	 * @param id
	 *            导航view ID
	 */
	private void replaceFragment(FragmentManager fm, LoginFragment mLoginFragment) {
		String tag = String.valueOf(R.id.content_frame);
		FragmentTransaction trans = fm.beginTransaction();
		if (null == fm.findFragmentByTag(tag)) {
			trans.replace(R.id.content_frame, mLoginFragment, tag);
		}
		trans.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_schedule_add:
			Intent intent = new Intent(this, AddScheduleActivity.class);
			intent.putExtra("planTime", mCalendarViewFm.getCalandarView().getSelectDayTime());
			intent.putExtra("type", "add");
			startActivity(intent);
			break;
		case R.id.tv_schedule_qurey:
			changeState(1);
			daySchedule();
			// if (ivScheduleList.isFocused()) {
			showDialog(101);
			// }
			break;
		case R.id.tv_schedule_syn:
			syncServerData();
			break;
		case R.id.iv_schedule_list:
			changeState(1);
			daySchedule();
			break;
		case R.id.iv_schedule_calandar:
			changeState(0);
			addView();
			break;
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}

	}

	private void changeState(int type) {
		if (type == 0) {
			ivScheduleCalendar.setFocusable(true);
			ivScheduleCalendar.setFocusableInTouchMode(true);
			ivScheduleCalendar.requestFocus();
			ivScheduleCalendar.requestFocusFromTouch();
			ivScheduleList.setFocusable(false);
		} else if (type == 1) {
			ivScheduleCalendar.setFocusable(false);
			ivScheduleList.setFocusable(true);
			ivScheduleList.setFocusableInTouchMode(true);
			ivScheduleList.requestFocus();
			ivScheduleList.requestFocusFromTouch();
		}
	}

	private void syncServerData() {
		skipLogin(mContext);
	}

	protected Dialog onCreateDialog(int id) {

		final Dialog connectDialog = new Dialog(mContext, R.style.my_dialog_theme);
		View view = getLayoutInflater().inflate(R.layout.query_daytak_dialog, null);
		connectDialog.setContentView(view);

		TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
		TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
		final EditText connectHttp = (EditText) view.findViewById(R.id.et_connect_http);
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
					// mDayTaskFm.setQueryKey();
					daySchedule(connectHttp.getText().toString());
				}
			}
		});

		connectDialog.setCancelable(false);
		connectDialog.setCanceledOnTouchOutside(false);
		connectDialog.show();
		return connectDialog;
	}

	@Override
	public void onBackPressed() {
		// // TODO Auto-generated method stub
		// super.onBackPressed();
		ScheduleActivity.this.finish();
	}

	public CalendarView getCalandarView() {
		return mCalendarViewFm.getCalandarView();
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
				GetPlanParam getPlanParam = new GetPlanParam();
				// getPlanParam.timestamp = "2014-01-01 00:00:00";
				getPlanParam.timestamp = updateTime;
				getPlanParam.start = 0;
				getPlanParam.counts = 1000;

				String ret = HttpXUtils.getInfoByPostObject(URLS.getContacts, getPlanParam, PreferencesService.getInfo("token"));

				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.syncontact_success),
							// 0).show();
							ReContacts reContacts = JsonUtils.getContactsResult(ret);
							// 保存联系人
							// mContacts = reContacts.data;
							DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
							helper.insertContactsData(reContacts.data, updateTime);
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

		asyncTaskControl.execute(TaskNo.LoginFragment_getcontacts, icontrol, this);
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
				getPlanParam.counts = 1000;

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

		// TelephonyManager tm = (TelephonyManager)
		// context.getSystemService(context.TELEPHONY_SERVICE);
		// imeiString = tm.getDeviceId();
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

	private List<RePlanActData> getOfflineActPlansFromDb() {
		DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(mContext);
		return dBhelper.queryOfflineActPlanInfo();
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

	@Override
	public void updateUi(int taskNo, String ret) {
		if (!TextUtils.isEmpty(ret)) {
			switch (taskNo) {
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
					// Toast.makeText(mContext,
					// mContext.getResources().getString(R.string.login_err),
					// 0).show();
				}
				break;
			case TaskNo.LoginFragment_getcontacts:
				try {
					JSONObject retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("success")) {
						if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
							// Toast.makeText(mContext,
							// mContext.getResources().getString(R.string.syncontact_success),
							// 0).show();
							// ReContacts reContacts =
							// JsonUtils.getContactsResult(ret);
							// // 保存联系人
							// mContacts = reContacts.data;
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
					// Toast.makeText(mContext,
					// mContext.getResources().getString(R.string.login_err),
					// 0).show();
				}
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
					// Toast.makeText(mContext,
					// mContext.getResources().getString(R.string.login_err),
					// 0).show();
				}
				break;
			default:
				break;
			}
		}
	}

}
