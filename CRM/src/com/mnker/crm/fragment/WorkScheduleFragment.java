package com.mnker.crm.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mnker.crm.AddScheduleActivity;
import com.mnker.crm.MainActivity;
import com.mnker.crm.R;
import com.mnker.crm.ScheduleActivity;
import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.http.json.RePlanActData;
import com.mnker.crm.tool.AsyncTaskControl;
import com.mnker.crm.tool.AsyncTaskControl.IAirControl;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.tool.UndoWorkCollectInfo;
import com.mnker.crm.widget.CalendarView;
import com.mnker.crm.widget.calendar.Cell;
import com.mnker.crm.widget.util.Util;

public class WorkScheduleFragment extends Fragment implements OnClickListener, CalendarView.OnCellTouchListener {
	private ImageView mSetting;
	private TextView mPunchCard;
	private TextView mSchedule;
	private TextView mContact;
	private ScheduleActivity mContext;
	private ListView mScheduleListView;
	private ScheduleAdapter mAdapter;
	private List<HashMap<String, Object>> resource;
	private List<RePlanActData> rePlanActData = new ArrayList<RePlanActData>();
	private List<RePlanActData> rePlanActDataTemp = new ArrayList<RePlanActData>();
	private List<RePlanActData> undoRePlanActDataTemp = new ArrayList<RePlanActData>();
	private TextView mMonthTv;
	private CalendarView mCalendarView;
	private ArrayList<UndoWorkCollectInfo> unDoworkList;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mAdapter.notifyDataSetChanged();
			super.handleMessage(msg);
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.schedule_calandar, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = (ScheduleActivity) activity;
		super.onAttach(activity);
	}

	private void initView(View view) {
		ImageView mPreIv = (ImageView) view.findViewById(R.id.pre_month);
		ImageView mNextIv = (ImageView) view.findViewById(R.id.next_month);
		mMonthTv = (TextView) view.findViewById(R.id.set_month);
		mCalendarView = (CalendarView) view.findViewById(R.id.calendar);
		mCalendarView.setOnCellTouchListener(this);
		//
		mPreIv.setOnClickListener(this);
		mNextIv.setOnClickListener(this);

		mScheduleListView = (ListView) view.findViewById(R.id.schedule_list);
		// resource = getData();
		mAdapter = new ScheduleAdapter(mContext, rePlanActData);
		mScheduleListView.setAdapter(mAdapter);

		mScheduleListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (rePlanActData != null) {
					String uuid = rePlanActData.get(position).uuid;
					showDetailSchedule(uuid);
				}
			}
		});
		initUndoWorkTotal();
		getActPlansData();
		mMonthTv.setText(getResources().getString(R.string.schedule_calendar_month, (mCalendarView.mHelper.getMonth() + 1))
				+ getResources().getString(R.string.schedule_calendar_year, mCalendarView.mHelper.getYear()));
	}

	private void showDetailSchedule(String uuid) {
		Intent intent = new Intent();
		intent.putExtra("uuid", uuid);
		intent.putExtra("type", "show");
		intent.setClass(getActivity(), AddScheduleActivity.class);
		startActivity(intent);
	}

	private void getActPlansData() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {

				DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
				List<RePlanActData> mActPlans = helper.getActPlanDataInfo("uuid", 0, mContext.getCalandarView().getMonthString(), null);
				// rePlanActData.clear();
				// rePlanActData.addAll(mActPlans);
				Log.d("zaokun", "getActPlansData timeString=" + rePlanActData.size());
				rePlanActDataTemp.clear();
				rePlanActDataTemp.addAll(mActPlans);
				clearUnDoworkList();
				getUndoTotalWork();
				getSelectDayTask(mCalendarView.getSelectDayTime());
				return "";
			}
		};

		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String data) {
				// 化小圆圈
				rePlanActData.clear();
				rePlanActData.addAll(undoRePlanActDataTemp);
				refreshCalendarUI();
				mHandler.sendEmptyMessage(0);
				// mAdapter.notifyDataSetChanged();
			}
		};

		asyncTaskControl.execute(0, icontrol, uiCallBack);

	}

	protected void refreshCalendarUI() {
		mCalendarView.setUndoWorkTotal(unDoworkList);
	}

	private void getOneDayActPlansData(final String string) {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				getSelectDayTask(string);
				return "";
			}
		};

		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String data) {
				rePlanActData.clear();
				rePlanActData.addAll(undoRePlanActDataTemp);
				// mAdapter.notifyDataSetChanged();
				mHandler.sendEmptyMessage(0);
			}
		};

		asyncTaskControl.execute(0, icontrol, uiCallBack);

	}

	protected void getSelectDayTask(String selectDay) {
		// List<RePlanActData> temPlanActDatas = new ArrayList<RePlanActData>();
		// rePlanActData.clear();
		undoRePlanActDataTemp.clear();
		for (RePlanActData planActData : rePlanActDataTemp) {
			String timeString = planActData.planStartTime;
			if (timeString.startsWith(selectDay)) {
				undoRePlanActDataTemp.add(planActData);
			}

		}
		// return temPlanActDatas;
	}

	protected void initUndoWorkTotal() {
		unDoworkList = new ArrayList<UndoWorkCollectInfo>();
		for (int i = 1; i < 32; i++) {
			UndoWorkCollectInfo undoWorkCollectInfo = new UndoWorkCollectInfo();
			unDoworkList.add(undoWorkCollectInfo);
		}
	}

	protected void getUndoTotalWork() {
		if (rePlanActDataTemp.size() > 0) {
			for (RePlanActData planActData : rePlanActDataTemp) {
				String timeString = planActData.planStartTime;
				Log.d("zaokun", "getUndoTotalWork timeString=" + timeString);
				// int month = Util.getMonthFromTimeString(timeString);
				int day = Util.getDayFromTimeString(timeString);
				if (!planActData.actvtStatus.equalsIgnoreCase("3")) {
					unDoworkList.get(day - 1).total++;
				}
			}
		}
	}

	private void clearUnDoworkList() {
		for (UndoWorkCollectInfo undoWorkCollectInfo : unDoworkList) {
			undoWorkCollectInfo.total = 0;
		}
		// unDoworkList.clear();
		// initUndoWorkTotal();
	}

	private List<HashMap<String, Object>> getData() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 1; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("recordtime", "8:" + (35 + i));
			map.put("recordtitle", "test服务行动");
			map.put("recordmsg", "test");
			list.add(map);
		}
		return list;
	}

	private void login() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), MainActivity.class);
		startActivity(intent);
	}

	protected Dialog onCreateDialog(int id) {

		final Dialog connectDialog = new Dialog(getActivity(), R.style.my_dialog_theme);
		View view = getActivity().getLayoutInflater().inflate(R.layout.client_setting_dialog, null);
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
		Calendar calendar;
		switch (v.getId()) {
		case R.id.iv_setting:
			break;
		case R.id.tv_punch_card:
			break;
		case R.id.tv_work_schedule:
			break;
		case R.id.tv_contact:
			break;
		case R.id.pre_month:
			// calendar = Calendar.getInstance();
			// CalendarView.mHelper = new
			// MonthDisplayHelper(calendar.get(Calendar.YEAR),
			// calendar.get(Calendar.MONTH));
			unDoworkList.clear();
			initUndoWorkTotal();
			refreshCalendarUI();
			mCalendarView.previousMonth();
			mMonthTv.setText(getResources().getString(R.string.schedule_calendar_month, (mCalendarView.mHelper.getMonth() + 1))
					+ getResources().getString(R.string.schedule_calendar_year, mCalendarView.mHelper.getYear()));
			getActPlansData();
			break;
		case R.id.next_month:
			unDoworkList.clear();
			initUndoWorkTotal();
			refreshCalendarUI();
			mCalendarView.nextMonth();
			mMonthTv.setText(getResources().getString(R.string.schedule_calendar_month, (mCalendarView.mHelper.getMonth() + 1))
					+ getResources().getString(R.string.schedule_calendar_year, mCalendarView.mHelper.getYear()));
			getActPlansData();
			break;

		default:
			break;
		}
	}

	public class ScheduleAdapter extends BaseAdapter {
		List<RePlanActData> planActDatas;
		Context mContext;

		public ScheduleAdapter(Context context, List<RePlanActData> rePlanActData) {
			this.planActDatas = rePlanActData;
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return planActDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return planActDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHoler holder = null;
			if (convertView == null) {
				holder = new ViewHoler();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.schedule_item, null);
				holder.recordTime = (TextView) convertView.findViewById(R.id.record_time);
				holder.recordTitle = (TextView) convertView.findViewById(R.id.record_title);
				holder.recordMsg = (TextView) convertView.findViewById(R.id.record_msg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHoler) convertView.getTag();
			}
			try {
				if (position < planActDatas.size()) {
					RePlanActData planActData = planActDatas.get(position);

					holder.recordTime.setText(Util.getPlanStartTime(planActData.planStartTime));
					holder.recordTitle.setText(planActData.name);
					holder.recordMsg.setText(planActData.assignedUserName);
					// holder.recordMsg.setText(planActData.accountName);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return convertView;
		}

		class ViewHoler {
			private TextView recordTime;
			private TextView recordTitle;
			private TextView recordMsg;

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
		queryUndoworkPlans();
		super.onResume();
	}

	private void queryUndoworkPlans() {
		getActPlansData();
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
	public void onTouch(Cell cell) {
		int[] dateTime = getCellTime(cell);
		final String yearStr = dateTime[0] + "-";
		final String monthStr = String.format("%02d", dateTime[1]) + "-";
		final String dayStr = String.format("%02d", dateTime[2]);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				getOneDayActPlansData(yearStr + monthStr + dayStr);
			}
		});
		// getSelectDayTask(yearStr+monthStr+dayStr);
		// goToDayByTouch(dateTime[0] + "/" + dateTime[1] + "/" + dateTime[2]);

	}

	public int[] getCellTime(final Cell cell) {
		int[] dateTime = new int[3];
		dateTime[0] = mCalendarView.getYear();
		dateTime[1] = mCalendarView.getMonth() + 1;
		dateTime[2] = cell.getDayOfMonth();

		return dateTime;
	}

	public CalendarView getCalandarView() {
		return mCalendarView;
	}
}
