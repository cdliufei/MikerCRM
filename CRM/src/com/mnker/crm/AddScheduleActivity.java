package com.mnker.crm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.android.bbalbs.common.a.c;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.map.s;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mnker.crm.baidu.location.GeoCoderAddress;
import com.mnker.crm.base.IntentNo;
import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.fragment.LoginFragment;
import com.mnker.crm.http.HttpXUtils;
import com.mnker.crm.http.URLS;
import com.mnker.crm.http.json.AddNewPlanActData;
import com.mnker.crm.http.json.GetPlanParam;
import com.mnker.crm.http.json.PunchCard;
import com.mnker.crm.http.json.ReContactsData;
import com.mnker.crm.http.json.RePlan;
import com.mnker.crm.http.json.RePlanActData;
import com.mnker.crm.http.json.RePlanData;
import com.mnker.crm.http.json.util.JsonUtils;
import com.mnker.crm.tool.AsyncTaskControl;
import com.mnker.crm.tool.AsyncTaskControl.IAirControl;
import com.mnker.crm.tool.DialogUtil;
import com.mnker.crm.tool.TaskNo;
import com.mnker.crm.tool.Tool;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.tool.Utilities;
import com.mnker.crm.widget.SwitchButton;
import com.mnker.crm.ui.WeekPlanSelectActivity;
import com.mnker.crm.ui.adpater.WeekPlanAdapter;
import com.mnker.crm.widget.util.PreferencesService;
import com.mnker.crm.widget.util.Util;

public class AddScheduleActivity extends Activity implements OnClickListener {
	private static final int PURPOSE = 10001;

	private static final int SUMMARY = 10002;

	private static final int COMMENT = 10003;

	private static final int TALK = 10004;

	private static final int CONTACTS = 10005;

	private static final int NEXTSTEP = 10006;

	@ViewInject(R.id.iv_back)
	ImageView ivBack;

	@ViewInject(R.id.tv_schedule_title)
	TextView mScheduleTitle;

	@ViewInject(R.id.iv_schedule_ok)
	TextView mOk;

	@ViewInject(R.id.iv_location)
	ImageView mLocation;

	@ViewInject(R.id.et_zhuti)
	TextView mZhuTi;

	@ViewInject(R.id.et_leixing)
	TextView mLeixing;

	@ViewInject(R.id.et_partment)
	TextView mPartment;

	@ViewInject(R.id.et_owner)
	TextView mOwner;

	@ViewInject(R.id.et_planstart)
	TextView mPlanStartTime;

	@ViewInject(R.id.et_planend)
	TextView mPlanEndTime;

	@ViewInject(R.id.et_start)
	TextView mStartTime;

	@ViewInject(R.id.et_end)
	TextView mEndTime;

	@ViewInject(R.id.et_contacts)
	TextView mContacts;

	@ViewInject(R.id.et_customer)
	TextView mCustomer;

	@ViewInject(R.id.et_befornotice)
	TextView mBeforeNotice;

	@ViewInject(R.id.et_noticetime)
	TextView mNoticeTime;

	@ViewInject(R.id.et_state)
	TextView mState;

	@ViewInject(R.id.et_workplan)
	TextView mWorkPlan;

	@ViewInject(R.id.et_nextstep)
	TextView mNextStep;

	@ViewInject(R.id.et_talk)
	TextView mTalk;

	@ViewInject(R.id.et_purpose)
	TextView mPurpose;

	@ViewInject(R.id.et_summary)
	TextView mSummary;

	@ViewInject(R.id.et_comment)
	TextView mComment;

	@ViewInject(R.id.et_planend)
	TextView et_planend;
	@ViewInject(R.id.et_start)
	TextView et_start;
	@ViewInject(R.id.et_end)
	TextView et_end;
	@ViewInject(R.id.et_noticetime)
	TextView et_noticetime;

	private int actionType; // 1 show 0 edit;

	private String uuid;
	private LinearLayout planTimeView, planEndTimeView, planStartTimeView, planOverTimeView, add_schedule_item_notice_time;

	private String planTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_schedule);
		ViewUtils.inject(this);
		mContext = this;
		Intent bundle = getIntent();
		if (bundle != null) {
			String action = bundle.getStringExtra("type");
			System.out.println("---type " + action + "----activityId ");
			if (!TextUtils.isEmpty(action)) {
				if (action.equalsIgnoreCase("show")) {
					actionType = 1;
					uuid = bundle.getStringExtra("uuid");
				} else {
					planTime = bundle.getStringExtra("planTime");
					actionType = 0;
				}
			}
		}

		initView();
		initPointLocation();
		initTimeView();
		synServerData();
	}

	private void initTimeView() {
		planTimeView = (LinearLayout) findViewById(R.id.planTimeView);
		planEndTimeView = (LinearLayout) findViewById(R.id.planEndTimeView);
		planStartTimeView = (LinearLayout) findViewById(R.id.planStartTimeView);
		planOverTimeView = (LinearLayout) findViewById(R.id.planOverTimeView);
		add_schedule_item_notice_time = (LinearLayout) findViewById(R.id.add_schedule_item_notice_time);

		planTimeView.setOnClickListener(this);
		planEndTimeView.setOnClickListener(this);
		planStartTimeView.setOnClickListener(this);
		planOverTimeView.setOnClickListener(this);
		add_schedule_item_notice_time.setOnClickListener(this);

		switchBt.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (switchBt.isChecked()) {
					rePlanActData.isRemind = 1;
					setNoticeTime.setEnabled(true);
				} else {
					rePlanActData.isRemind = 0;
					setNoticeTime.setEnabled(false);
				}
			}
		});

	}

	private void initPointLocation() {
		pointLcation = new GeoCoderAddress(mContext, handler);
		// pointLcation.start();
	}

	private void getGpsLocation() {
		pointLcation.start();
	}

	Handler handler = new Handler() {
		private LatLng addDt;

		public void handleMessage(android.os.Message msg) {
			try {
				addDt = (LatLng) msg.obj;
				if (addDt != null) {
					PreferencesService.setLong("latitude", (long) addDt.latitude);
					PreferencesService.setLong("longitude", (long) addDt.longitude);
					System.out.println("gps == lati == " + PreferencesService.getLong("latitude") + " longi = " + PreferencesService.getLong("longitude"));
					Toast.makeText(mContext, getResources().getString(R.string.load_gps_success), 0).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(mContext, getResources().getString(R.string.load_gps_fail), 0).show();
			}
		};
	};
	private GeoCoderAddress pointLcation;

	@Override
	protected void onDestroy() {
		pointLcation.finish();
		super.onDestroy();
	}

	@OnClick(R.id.iv_back)
	public void onBack(View view) {
		finish();
	}

	@OnClick(R.id.iv_schedule_ok)
	public void onScheduleOk(View view) {
		if (checkIsValible()) {
			if (actionType == 1) {
				update2Db();
			} else {
				save2Db();
			}
			finish();
		}

		// upLoadDayTaskInfo2Server();
	}

	private void update2Db() {
		DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
		helper.updateOfflineActPlanInfoSuccess(rePlanActData, 1);
	}

	private void save2Db() {
		List<RePlanActData> list = new ArrayList<RePlanActData>();
		list.add(rePlanActData);
		DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
		helper.insertActPlanData(list, Util.getNowTime(), "1");
	}

	private boolean checkIsValible() { // 检查是否没有输入必须字段

		if (TextUtils.isEmpty(mPlanStartTime.getText().toString())) {
			Toast.makeText(mContext, getResources().getString(R.string.please_input_plansrattime), 0).show();
			return false;
		}
		if (TextUtils.isEmpty(mContacts.getText().toString())) {
			Toast.makeText(mContext, getResources().getString(R.string.please_input_contact), 0).show();
			return false;
		}
		if (TextUtils.isEmpty(mWorkPlan.getText().toString())) {
			Toast.makeText(mContext, getResources().getString(R.string.please_input_workplan), 0).show();
			return false;
		}
		// if (TextUtils.isEmpty(mTalk.getText().toString())) {
		// Toast.makeText(mContext,
		// getResources().getString(R.string.please_input_talk), 0).show();
		// return false;
		// }
		// if (TextUtils.isEmpty(mSummary.getText().toString())) {
		// Toast.makeText(mContext,
		// getResources().getString(R.string.please_input_summary), 0).show();
		// return false;
		// }

		return true;

	}

	@OnClick(R.id.iv_location)
	public void onLocation(View view) {
		getGpsLocation();
	}

	@OnClick(R.id.tv_leixing_arrow)
	public void onLeixing(View view) {
		// 弹框
		// Utilities.showInfo(mContext,
		// PreferencesService.getInfo("actTypesvalue"));
		getLeixingsItem();
	}

	private void getLeixingsItem() {
		final String[] items = new String[1];
		items[0] = PreferencesService.getInfo("actTypesvalue");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		int index = 0;
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				select = which;
			}
		});
		builder.setPositiveButton(R.string.share_notice_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.share_notice_cancle, null);

		builder.show();

	}

	@OnClick(R.id.et_partment)
	public void onPartment(View view) {

	}

	@OnClick(R.id.et_owner)
	public void onOwner(View view) {

	}

	@OnClick(R.id.tv_planstart_arrow)
	public void onPlanstart(View view) {

	}

	@OnClick(R.id.tv_planend_arrow)
	public void onPlanend(View view) {

	}

	@OnClick(R.id.tv_start_arrow)
	public void onStartTime(View view) {

	}

	@OnClick(R.id.tv_contacts_arrow)
	public void onContacts(View view) {
		Intent intent = new Intent(mContext, ContactsMenuActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("isSelect", true);
		intent.putExtras(bundle);
		startActivityForResult(intent, CONTACTS);
	}

	@OnClick(R.id.et_customer)
	public void onCustomer(View view) {

	}

	@ViewInject(R.id.et_befornoticeChecked)
	SwitchButton switchBt;

	@ViewInject(R.id.add_schedule_item_notice_time)
	LinearLayout setNoticeTime;

	@OnClick(R.id.et_befornoticeChecked)
	public void onBeforNotice(View view) {
		if (switchBt.isChecked()) {
			rePlanActData.isRemind = 1;
			setNoticeTime.setEnabled(true);
		} else {
			rePlanActData.isRemind = 0;
			setNoticeTime.setEnabled(false);
		}
	}

	@OnClick(R.id.tv_end_arrow)
	public void onEndTime(View view) {

	}

	// @OnClick(R.id.tv_noticetime_arrow)
	// public void onNoticeTime(View view) {
	//
	// }

	@OnClick(R.id.tv_state_arrow)
	public void onState(View view) {
		getWorkplansStatusItem();
	}

	int select;

	private void getWorkplansStatusItem() {
		List<HashMap<String, Object>> groupNameList = getTypeStatusData();

		final String[] items = new String[groupNameList.size()];
		final String[] actvtStatus = new String[groupNameList.size()];

		for (int i = 0; i < items.length; i++) {
			items[i] = ((String) groupNameList.get(i).get("status")).trim().replace("\n", "");
			actvtStatus[i] = ((String) groupNameList.get(i).get("actvtStatus")).trim().replace("\n", "");
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		int index = 0;
		if (!TextUtils.isEmpty(rePlanActData.actvtStatus)) {
			index = Integer.parseInt(rePlanActData.actvtStatus) - 1;
		}
		if (index < 0) {
			index = 0;
		}
		builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				select = which;
			}
		});
		builder.setPositiveButton(R.string.share_notice_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				rePlanActData.status = items[select];
				rePlanActData.actvtStatus = actvtStatus[select];
				if (rePlanActData.actvtStatus.equalsIgnoreCase("3")) {
					rePlanActData.finishedTime = Util.getNowTime();
				}else {
					
				}
				System.out.println("getWorkplansStatusItem == " + rePlanActData.actvtStatus);
				recoveryData();
			}
		});
		builder.setNegativeButton(R.string.share_notice_cancle, null);

		// builder.setItems(items, new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// rePlanActData.status = items[which];
		// rePlanActData.actvtStatus = actvtStatus[which];
		// if (rePlanActData.actvtStatus.equalsIgnoreCase("3")) {
		// rePlanActData.finishedTime = Util.getNowTime();
		// }
		// System.out.println("getWorkplansStatusItem == " +
		// rePlanActData.actvtStatus);
		// recoveryData();
		// }
		// });
		builder.show();

	}

	private List<HashMap<String, Object>> getTypeStatusData() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", getResources().getString(R.string.add_schedule_item_state_undo));
		map.put("actvtStatus", "1");
		list.add(map);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("status", getResources().getString(R.string.add_schedule_item_state_doing));
		map2.put("actvtStatus", "2");
		list.add(map2);
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("status", getResources().getString(R.string.add_schedule_item_state_done));
		map3.put("actvtStatus", "3");
		list.add(map3);
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("status", getResources().getString(R.string.add_schedule_item_state_waiting));
		map4.put("actvtStatus", "4");
		list.add(map4);
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("status", getResources().getString(R.string.add_schedule_item_state_push));
		map5.put("actvtStatus", "5");
		list.add(map5);
		return list;
	}

	@OnClick(R.id.tv_workplan_arrow)
	public void onWorkPlan(View view) {
		// List<RePlanData> rePlanDatas = gettWorkPlan();
		// if (rePlanDatas != null && rePlanDatas.size() > 0) {
		// getWorkplansItem(rePlanDatas);
		// }
		Intent mIntent = new Intent();
		mIntent.setClass(AddScheduleActivity.this, WeekPlanSelectActivity.class);
		AddScheduleActivity.this.startActivityForResult(mIntent, IntentNo.GOTOSELECTWEEKPLAN);
	}

	private void getWorkplansItem(List<RePlanData> rePlanDatas) {
		String[] fromType = new String[] { "type" };
		int[] toType = new int[] { android.R.id.text1 };
		List<HashMap<String, Object>> groupNameList = getTypeData(rePlanDatas);

		final String[] items = new String[groupNameList.size()];
		final int[] planId = new int[groupNameList.size()];

		for (int i = 0; i < items.length; i++) {
			items[i] = (String) groupNameList.get(i).get("planName");
			planId[i] = (Integer) groupNameList.get(i).get("planId");
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				rePlanActData.planName = items[which];
				rePlanActData.planId = planId[which];
				recoveryData();
			}
		});
		builder.show();

	}

	private List<HashMap<String, Object>> getTypeData(List<RePlanData> rePlanDatas) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (RePlanData rePlanData : rePlanDatas) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("planName", rePlanData.planName);
			map.put("planId", rePlanData.planId);
			list.add(map);
		}
		return list;
	}

	@OnClick(R.id.tv_nextstep_arrow)
	public void onNextStep(View view) {
		Intent intent = new Intent(mContext, MessageInputActivity.class);
		if (actionType == 1) {
			intent.putExtra("memo", rePlanActData.xybjh);
		}
		intent.putExtra("title", getResources().getString(R.string.add_schedule_item_nextstep));
		startActivityForResult(intent, NEXTSTEP);
	}

	@OnClick(R.id.tv_talk_arrow)
	public void onTalk(View view) {
		Intent intent = new Intent(mContext, MessageInputActivity.class);
		if (actionType == 1) {
			intent.putExtra("memo", rePlanActData.thnr);
		}
		intent.putExtra("title", getResources().getString(R.string.add_schedule_item_talk));
		startActivityForResult(intent, TALK);
	}

	@OnClick(R.id.tv_purpose_arrow)
	public void onPurpose(View view) {
		Intent intent = new Intent(mContext, MessageInputActivity.class);
		if (actionType == 1) {
			intent.putExtra("memo", rePlanActData.purpose);
		}
		intent.putExtra("title", getResources().getString(R.string.add_schedule_item_purpose));
		startActivityForResult(intent, PURPOSE);
	}

	@OnClick(R.id.tv_summary_arrow)
	public void onSummary(View view) {
		Intent intent = new Intent(mContext, MessageInputActivity.class);
		if (actionType == 1) {
			intent.putExtra("memo", rePlanActData.zj);
		}
		intent.putExtra("title", getResources().getString(R.string.add_schedule_item_summary));
		startActivityForResult(intent, SUMMARY);
	}

	@OnClick(R.id.tv_comment_arrow)
	public void onComment(View view) {
		Intent intent = new Intent(mContext, MessageInputActivity.class);
		if (actionType == 1) {
			intent.putExtra("memo", rePlanActData.remark);
		}
		intent.putExtra("title", getResources().getString(R.string.add_schedule_item_comment));
		startActivityForResult(intent, COMMENT);
	}

	private List<RePlanActData> rePlanActDatas = new ArrayList<RePlanActData>();
	private RePlanActData rePlanActData;

	private Context mContext;

	private void initView() {
		if (actionType == 1) { // show
			getActPlansDetail(uuid);
		} else {
			rePlanActData = new RePlanActData();
			initBaseUserInfo();
		}

		// ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		//
		// ivBack.setOnClickListener(this);

		// recoveryData();
	}

	private void initBaseUserInfo() {
		// 新增填充基本数据
		rePlanActData.uuid = Tool.getUUID();
		String username = PreferencesService.getInfo("username");
		String actType = PreferencesService.getInfo("actTypesvalue");
		mZhuTi.setText(username + actType);
		rePlanActData.name = username + actType;
		mZhuTi.setEnabled(false);
		mLeixing.setText(mContext.getString(R.string.withoutPlan));
		rePlanActData.typeName = mContext.getString(R.string.withoutPlan);
		mLeixing.setEnabled(false);
		mPartment.setText(PreferencesService.getInfo("deptName"));
		rePlanActData.deptName = PreferencesService.getInfo("deptName");
		rePlanActData.deptId = PreferencesService.getInt("deptId");
		mPartment.setEnabled(false);
		mOwner.setText(username);
		mOwner.setEnabled(false);
		rePlanActData.assignedUserName = username;
		rePlanActData.assignedUserId = PreferencesService.getInt("actTypeskey");
		rePlanActData.createUserId = PreferencesService.getInt("actTypeskey");
		rePlanActData.createUserName = username;
		if (planTime == null) {
			mPlanStartTime.setText(Util.getDefaultPlanStartTime());
			rePlanActData.planStartTime = Util.getDefaultPlanStartTime();
		} else {
			mPlanStartTime.setText(planTime + " 08:00");
			rePlanActData.planStartTime = planTime + " 08:00";
		}
		mPlanStartTime.setEnabled(false);
		mPlanEndTime.setText("");
		mPlanEndTime.setEnabled(false);
		mStartTime.setText("");
		mStartTime.setEnabled(false);
		mEndTime.setText("");
		mEndTime.setEnabled(false);
		mContacts.setText("");
		mContacts.setEnabled(false);
		mCustomer.setText("");
		mCustomer.setEnabled(false);
		mBeforeNotice.setText(R.string.add_schedule_item_before_notice_no);
		rePlanActData.isRemind = 0;
		mBeforeNotice.setEnabled(false);
		mNoticeTime.setText("");
		mNoticeTime.setEnabled(false);
		mNoticeTime.setEnabled(false);

		String actvtStatus = "";
		actvtStatus = getResources().getString(R.string.add_schedule_item_state_undo);
		rePlanActData.actvtStatus = "1";
		rePlanActData.status = actvtStatus;
		mState.setText(actvtStatus);
		mState.setEnabled(false);
		String workPlan = getDefaultWorkPlan();
		String[] split = workPlan.split(":");
		if (split != null && split.length == 2) {
			rePlanActData.planName = split[1];
			rePlanActData.planId = Integer.parseInt(split[2]);
		}
		mWorkPlan.setText(workPlan);
		mWorkPlan.setEnabled(false);
		mNextStep.setText("");
		mNextStep.setEnabled(false);
		mTalk.setText("");
		mTalk.setEnabled(false);
		mPurpose.setText("");
		mPurpose.setEnabled(false);
		mSummary.setText("");
		mSummary.setEnabled(false);
		mComment.setText("");
		mComment.setEnabled(false);
		rePlanActData.isAppAdd = 1;

	}

	private String getDefaultWorkPlan() {
		DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
		List<RePlanData> rePlanDatas = helper.getWeekPlanInfo(0, "uuid", null);
		if (rePlanDatas != null && rePlanDatas.size() > 0) {
			String workPlanString = rePlanDatas.get(0).planName;
			return workPlanString + "" + rePlanDatas.get(0).planId;
		}
		return "";
	}

	private List<RePlanData> gettWorkPlan() {

		DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
		List<RePlanData> rePlanDatas = helper.getWeekPlanInfo(0, "uuid", null);
		return rePlanDatas;
	}

	private void upLoadDayTaskInfo2Server() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				// List<RePlanActData> list = new ArrayList<RePlanActData>();
				// list.add(rePlanActData);

				AddNewPlanActData addNewPlanActData = new AddNewPlanActData();
				addNewPlanActData.name = rePlanActData.name;
				addNewPlanActData.accountId = rePlanActData.accountId;
				addNewPlanActData.accountName = rePlanActData.accountName;
				if (actionType == 0) {
					addNewPlanActData.activityId = new Random().nextInt(100000) + 1000;
				} else {
					addNewPlanActData.activityId = rePlanActData.activityId;
				}
				addNewPlanActData.actvtStatus = rePlanActData.actvtStatus;
				addNewPlanActData.assignedUserId = rePlanActData.assignedUserId;
				addNewPlanActData.createUserId = rePlanActData.createUserId;
				addNewPlanActData.createUserName = rePlanActData.createUserName;
				addNewPlanActData.contactName = rePlanActData.contactName;
				addNewPlanActData.contactId = rePlanActData.contactId;
				addNewPlanActData.deptId = rePlanActData.deptId;
				if (actionType == 0) {
					addNewPlanActData.isAppAdd = 1;
				}
				addNewPlanActData.isRemind = rePlanActData.isRemind;
				addNewPlanActData.planName = rePlanActData.planName;
				addNewPlanActData.planId = rePlanActData.planId;
				addNewPlanActData.planStartTime = rePlanActData.planStartTime;
				addNewPlanActData.reportTime = rePlanActData.planStartTime;
				addNewPlanActData.typeId = rePlanActData.assignedUserId;
				// addNewPlanActData.typeId =
				// PreferencesService.getInt("actTypeskey");
				addNewPlanActData.typeName = rePlanActData.typeName;
				addNewPlanActData.uuid = rePlanActData.uuid;
				if (actionType == 0) {
					addNewPlanActData.createTime = Util.getNowTime();
				} else {
					addNewPlanActData.createTime = rePlanActData.createTime;
				}
				addNewPlanActData.reportTime = Util.getNowTime();
				addNewPlanActData.type = "02";
				if (actionType == 0) {
					addNewPlanActData.finishedTime = Util.getNowTime();
				} else {
					addNewPlanActData.finishedTime = rePlanActData.finishedTime;
				}

				List<AddNewPlanActData> list = new ArrayList<AddNewPlanActData>();
				list.add(addNewPlanActData);
				System.out.println("add new = " + new Gson().toJson(addNewPlanActData));
				String ret = HttpXUtils.getInfoByPostObject(URLS.uploadAct, list, PreferencesService.getInfo("token"));
				// String ret = "";
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
							Toast.makeText(mContext, mContext.getResources().getString(R.string.upload_actplan_success), 0).show();
							DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(mContext);
							dBhelper.updateOfflineActPlanInfoSuccess(rePlanActData, 0);
						} else {
							Toast.makeText(mContext, mContext.getResources().getString(R.string.upload_actplan_fail), 0).show();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(mContext, mContext.getResources().getString(R.string.upload_actplan_fail), 0).show();
				}
			}
		};
		asyncTaskControl.execute(TaskNo.PunchCardActivity_uoload, icontrol, uiCallBack);
	}
	public  String ToDBC(String input) {          
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {              
        if (c[i] == 12288) {                 
        c[i] = (char) 32;                  
        continue;
         }
         if (c[i] > 65280 && c[i] < 65375)
            c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			if (data != null) {
				switch (requestCode) {
				case PURPOSE:
					rePlanActData.purpose = data.getStringExtra("memo");
					mPurpose.setText(data.getStringExtra("memo"));
					break;
				case SUMMARY:
					rePlanActData.zj = data.getStringExtra("memo");
					mSummary.setText(data.getStringExtra("memo"));
					break;
				case COMMENT:
					rePlanActData.remark = data.getStringExtra("memo");
					mComment.setText(data.getStringExtra("memo"));
					break;
				case TALK:
					rePlanActData.thnr = data.getStringExtra("memo");
					mTalk.setText(data.getStringExtra("memo"));
					break;
				case NEXTSTEP:
					rePlanActData.xybjh = data.getStringExtra("memo");
					mNextStep.setText(data.getStringExtra("memo"));
					break;
				case CONTACTS:
					ReContactsData reContactsData = (ReContactsData) data.getSerializableExtra("contact");
					rePlanActData.contactName = reContactsData.contactName;
					rePlanActData.contactId = reContactsData.contactId;
					rePlanActData.accountName = reContactsData.accountName;
					rePlanActData.accountId = Integer.parseInt(reContactsData.accountId);
					mContacts.setText(rePlanActData.contactName);
					mCustomer.setText(rePlanActData.accountName);
					break;
				case IntentNo.GOTOSELECTWEEKPLAN:
					if (data != null) {
						RePlanData planData = (RePlanData) data.getSerializableExtra("weekplan");
						rePlanActData.planId = planData.planId;
						rePlanActData.planName = planData.planName;
						Log.d("zaokun", "planData.planName==================" + planData.planName);
						if(planData.planName.indexOf("到")!=-1){
							mWorkPlan.setText(ToDBC(planData.planName).replace("到", "到 \n").trim());
						}else {
							mWorkPlan.setText(ToDBC(planData.planName));
						}
						

					}
					break;
				default:
					break;
				}
				// recoveryData();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void recoveryData() {
		if (rePlanActData == null) {
			return;
		}
		mScheduleTitle.setText(rePlanActData.name);
		mZhuTi.setText(rePlanActData.name);
		mLeixing.setText(rePlanActData.typeName);
		mPartment.setText(rePlanActData.deptName);
		mOwner.setText(rePlanActData.assignedUserName);
		mPlanStartTime.setText(Util.formatTimeString(rePlanActData.planStartTime));
		mPlanEndTime.setText(Util.formatTimeString(rePlanActData.planEndTime));
		mStartTime.setText(Util.formatTimeString(rePlanActData.startTime));
		mEndTime.setText(Util.formatTimeString(rePlanActData.endTime));
		mContacts.setText(rePlanActData.contactName);
		mCustomer.setText(rePlanActData.accountName);
		mBeforeNotice.setText(Util.formatTimeString(rePlanActData.contactName));
		if (rePlanActData.isRemind == 1) {
			mBeforeNotice.setText(R.string.add_schedule_item_before_notice_yes);
		} else {
			mBeforeNotice.setText(R.string.add_schedule_item_before_notice_no);
		}
		mNoticeTime.setText(rePlanActData.remindTime);

		String actvtStatus = "";
		switch (Integer.parseInt(rePlanActData.actvtStatus)) {
		case 0:
			actvtStatus = getResources().getString(R.string.add_schedule_item_state_undo);
			break;
		case 1:
			actvtStatus = getResources().getString(R.string.add_schedule_item_state_undo);
			break;
		case 2:
			actvtStatus = getResources().getString(R.string.add_schedule_item_state_doing);
			break;
		case 3:
			actvtStatus = getResources().getString(R.string.add_schedule_item_state_done);
			break;
		case 4:
			actvtStatus = getResources().getString(R.string.add_schedule_item_state_waiting);
			break;
		case 5:
			actvtStatus = getResources().getString(R.string.add_schedule_item_state_push);
			break;

		default:
			break;
		}

		mState.setText(rePlanActData.status);
		if(rePlanActData.planName.indexOf("到")!=-1){
			mWorkPlan.setText(ToDBC(rePlanActData.planName).replace("到", "到\n ").trim());
		}else {
			mWorkPlan.setText(rePlanActData.planName);
		}
		
		mNextStep.setText(rePlanActData.xybjh);
		mTalk.setText(rePlanActData.thnr);
		mPurpose.setText(rePlanActData.purpose);
		mSummary.setText(rePlanActData.zj);
		mComment.setText(rePlanActData.remark);

	}

	private void getDetailSchedule(int activityId) {
		if (activityId != 0) {
			rePlanActDatas = LoginFragment.mPlanActs;
			for (RePlanActData rePlanActDataTemp : rePlanActDatas) {
				if (rePlanActDataTemp.activityId == activityId) {
					this.rePlanActData = rePlanActDataTemp;
					break;
				}
			}
		}
	}

	private void getActPlansDetail(final String uuid) {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
				List<RePlanActData> mActPlans = helper.getActPlanDataInfo(uuid, -1, null, null);
				if (mActPlans.size() > 0) {
					rePlanActData = mActPlans.get(0);
				}
				return "";
			}
		};

		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String data) {
				recoveryData();
			}
		};

		asyncTaskControl.execute(0, icontrol, uiCallBack);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_schedule_add:
			Intent intent = new Intent(this, AddScheduleActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_schedule_qurey:

			break;
		case R.id.tv_schedule_syn:

			break;
		case R.id.iv_back:
			finish();
			break;
		// mPlanStartTime et_planend et_start et_end et_noticetime
		case R.id.planTimeView:
			showTimeDialog(1, null, "计划开始时间");
			break;
		case R.id.planEndTimeView:

			showTimeDialog(2, null, "计划结束时间");

			break;
		case R.id.planStartTimeView:
			showTimeDialog(3, null, "开始时间");
			break;
		case R.id.planOverTimeView:
			showTimeDialog(4, null, "结束时间");
			break;
		case R.id.add_schedule_item_notice_time:
			if (rePlanActData.isRemind == 1) {
				showTimeDialog(5, null, "提醒时间");
			}
			break;
		default:
			break;
		}

	}

	private void showTimeDialog(final int type, String defaultTime, String title) {
		Calendar calendar = Calendar.getInstance();
		if (defaultTime != null) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date start = df.parse(defaultTime);
				calendar.setTime(start);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			calendar.setTime(new Date());
		}
		AlertDialog.Builder builder = DialogUtil.getThemeDialogBuilder(this);
		View view = View.inflate(this, R.layout.date_time_dialog, null);
		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
		datePicker.setVisibility(View.VISIBLE);
		DialogUtil.setDatePickerTheme(datePicker);
		final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
		builder.setView(view);
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		builder.setTitle(title);
		builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						datePicker.getYear() + "-" + String.format("%02d", (datePicker.getMonth() + 1)) + "-"
								+ String.format("%02d", datePicker.getDayOfMonth()) + " " + String.format("%02d", timePicker.getCurrentHour())).append(":")
						.append(String.format("%02d", timePicker.getCurrentMinute()));
				// startTime.setText(sb);
				updateTimeText(type, sb.toString());
				dialog.cancel();
			}
		});

		Dialog dialog = builder.create();
		dialog.show();
	}

	private void synServerData() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, null);
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				GetPlanParam getPlanParam = new GetPlanParam();
				// updateTime = "2014-01-01 00:00:00";
				getPlanParam.timestamp = PreferencesService.getInfo("weekuptime");
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
							helper.insertWeekPlanData(rePlan.data, getPlanParam.timestamp);
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
		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String ret) {

			}
		};
		asyncTaskControl.execute(TaskNo.LoginFragment_getplan, icontrol, uiCallBack);
	}

	private void updateTimeText(int type, String content) {
		// mPlanStartTime et_planend et_start et_end et_noticetime
		// content = content.concat(":00");
		switch (type) {
		case 1:
			if (TextUtils.isEmpty(et_planend.getText().toString())) {
				mPlanStartTime.setText(content);
				rePlanActData.planStartTime = content;
				return;
			}
			boolean valible = checkDateIsAvalible(et_planend.getText().toString(), content);
			if (!valible) {
				mPlanStartTime.setText(content);
				rePlanActData.planStartTime = content;
			} else {
				Toast.makeText(mContext, getResources().getString(R.string.settime_err), 0).show();
			}
			break;
		case 2:
			valible = checkDateIsAvalible(mPlanStartTime.getText().toString(), content);

			if (valible) {
				et_planend.setText(content);
				rePlanActData.planEndTime = content;
			} else {
				Toast.makeText(mContext, getResources().getString(R.string.settime_err), 0).show();
			}

			break;
		case 3:
			if (TextUtils.isEmpty(et_end.getText().toString())) {
				et_start.setText(content);
				rePlanActData.startTime = content;
				return;
			}
			valible = checkDateIsAvalible(et_end.getText().toString(), content);
			if (!valible) {
				et_start.setText(content);
				rePlanActData.startTime = content;
			} else {
				Toast.makeText(mContext, getResources().getString(R.string.settime_err), 0).show();
			}

			// et_start.setText(content);
			// rePlanActData.startTime = content;
			break;
		case 4:
			if (TextUtils.isEmpty(et_start.getText().toString())) {
				Toast.makeText(mContext, getResources().getString(R.string.settime_before), 0).show();
				return;
			}

			valible = checkDateIsAvalible(et_start.getText().toString(), content);
			boolean isAfterPlanstartTime = checkDateIsAvalible(mPlanStartTime.getText().toString(), content);

			if (isAfterPlanstartTime) {
				if (valible) {
					et_end.setText(content);
					rePlanActData.endTime = content;
				} else {
					Toast.makeText(mContext, getResources().getString(R.string.settime_err), 0).show();
				}
			} else {
				Toast.makeText(mContext, getResources().getString(R.string.endtime_err), 0).show();
			}

			break;
		case 5:
			et_noticetime.setText(content);
			rePlanActData.remindTime = content;
			break;

		default:
			break;
		}
	}

	private boolean checkDateIsAvalible(String startTime, String endTime) {
		Date startDate = Util.formatTimeStringToDate(startTime);
		Date endDate = Util.formatTimeStringToDate(endTime);
		if (startDate.after(endDate)) {
			return false;
		}
		return true;
	}
}
