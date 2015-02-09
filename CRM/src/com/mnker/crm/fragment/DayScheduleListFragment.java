package com.mnker.crm.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mnker.crm.AddScheduleActivity;
import com.mnker.crm.ContactsMenuActivity;
import com.mnker.crm.LoginActivity;
import com.mnker.crm.MainActivity;
import com.mnker.crm.PunchCardActivity;
import com.mnker.crm.R;
import com.mnker.crm.ScheduleActivity;
import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.fragment.DayScheduleListFragment.DayScheduleAdapter;
import com.mnker.crm.fragment.DayScheduleListFragment.DayScheduleAdapter.ViewHoler;
import com.mnker.crm.fragment.WorkScheduleFragment.ScheduleAdapter;
import com.mnker.crm.http.json.PunchCard;
import com.mnker.crm.http.json.RePlanActData;
import com.mnker.crm.tool.AsyncTaskControl;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.tool.AsyncTaskControl.IAirControl;
import com.mnker.crm.widget.util.Util;

@SuppressLint("ValidFragment")
public class DayScheduleListFragment extends Fragment implements OnClickListener {
	private ImageView mSetting;
	private TextView mPunchCard;
	private TextView mSchedule;
	private TextView mContact;
	private ScheduleActivity mContext;
	private TextView mDateTime;
	private ListView mDayScheduleList;
	private DayScheduleAdapter mAdapter;
	private List<RePlanActData> rePlanActData = new ArrayList<RePlanActData>();
	private String queryKey;

	public DayScheduleListFragment(String queryKey) {
		this.queryKey = queryKey;
	}

	public DayScheduleListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.day_schedule_list, container, false);
		initView(view);
		// ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = (ScheduleActivity) activity;
		super.onAttach(activity);
	}

	private void initView(View view) {
		mDateTime = (TextView) view.findViewById(R.id.tv_day_schedule_title);
		mDayScheduleList = (ListView) view.findViewById(R.id.lv_day_schedule_listview);
		mDateTime.setText(Util.getCurrentTime());

		// rePlanActData = LoginFragment.mPlanActs;
		getOneMonthActPlansData();
		mAdapter = new DayScheduleAdapter(mContext, rePlanActData);
		mDayScheduleList.setAdapter(mAdapter);

		mDayScheduleList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (rePlanActData != null) {
					String uuid = rePlanActData.get(position).uuid;
					showDetailSchedule(uuid);
				}
			}
		});
	}

	private void getOneMonthActPlansData() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
				List<RePlanActData> mActPlans = helper.getActPlanDataInfo("uuid", 0, null, queryKey);
				rePlanActData.clear();
				// for (RePlanActData planActData : mActPlans) {
				// rePlanActData.add(planActData);
				// }
				rePlanActData.addAll(mActPlans);
				return "";
			}
		};

		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String data) {
				mAdapter.notifyDataSetChanged();
			}
		};

		asyncTaskControl.execute(0, icontrol, uiCallBack);

	}

	private void showDetailSchedule(String uuid) {
		Intent intent = new Intent();
		intent.putExtra("uuid", uuid);
		intent.putExtra("type", "show");
		intent.setClass(getActivity(), AddScheduleActivity.class);
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
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_setting:
			break;
		case R.id.tv_punch_card:
			intent = new Intent(mContext, PunchCardActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_work_schedule:
			intent = new Intent(mContext, ScheduleActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_contact:
			intent = new Intent(mContext, ContactsMenuActivity.class);
			startActivity(intent);
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

	public class DayScheduleAdapter extends BaseAdapter {
		List<RePlanActData> planActDatas;
		Context mContext;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DayScheduleAdapter(Context context, List<RePlanActData> planActData) {
			this.planActDatas = planActData;
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
        public String getPre(int cuurentPosition){
        	String afterFormatTimeString="";
        	try {
        		afterFormatTimeString=df.format(df.parse(planActDatas.get(cuurentPosition-1).planStartTime)); 
			} catch (Exception e) {
				// TODO: handle exception
			}
        
        	return afterFormatTimeString;
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
				holder.titleNeedForDay = (TextView) convertView.findViewById(R.id.titleNeedForDay);
				convertView.setTag(holder);
			} else {
				holder = (ViewHoler) convertView.getTag();
			}

			RePlanActData planActData = planActDatas.get(position);
			String startNowTime=planActData.planStartTime;
			try {
				String afterFormatTimeString=df.format(df.parse(startNowTime));
				if(position==0){
					holder.titleNeedForDay.setVisibility(View.VISIBLE);
					holder.titleNeedForDay.setText(afterFormatTimeString);
				}else {
				   if(!afterFormatTimeString.equals(getPre(position))){
					   holder.titleNeedForDay.setVisibility(View.VISIBLE);
						holder.titleNeedForDay.setText(afterFormatTimeString);
				   }else {
					   holder.titleNeedForDay.setVisibility(View.GONE);
				}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			holder.recordTime.setText(Util.getPlanStartTime(planActData.planStartTime));
			holder.recordTitle.setText(planActData.name);
			holder.recordMsg.setText(planActData.assignedUserName);
			
			// holder.recordMsg.setText(planActData.accountName);
			return convertView;
		}

		class ViewHoler {
			private TextView recordTime;
			private TextView recordTitle;
			private TextView recordMsg;
			private TextView titleNeedForDay;
		}

	}

	public void setQueryKey(String string) {

	}
}
