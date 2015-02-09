package com.mnker.crm.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.mnker.crm.ContactsMenuActivity;
import com.mnker.crm.LoginActivity;
import com.mnker.crm.MainActivity;
import com.mnker.crm.PunchCardActivity;
import com.mnker.crm.R;
import com.mnker.crm.ScheduleActivity;

public class MainFragment extends Fragment implements OnClickListener {
	private ImageView mSetting;
	private TextView mPunchCard;
	private TextView mSchedule;
	private TextView mContact;
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, container, false);
		initView(view);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	private void initView(View view) {
		mSetting = (ImageView) view.findViewById(R.id.iv_setting);
		mPunchCard = (TextView) view.findViewById(R.id.tv_punch_card);
		mSchedule = (TextView) view.findViewById(R.id.tv_work_schedule);
		mContact = (TextView) view.findViewById(R.id.tv_contact);

		mSetting.setOnClickListener(this);
		mPunchCard.setOnClickListener(this);
		mSchedule.setOnClickListener(this);
		mContact.setOnClickListener(this);

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
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_setting:
			SlidingMenu sm = ((LoginActivity) mContext).getSlidingMenu();
			if (sm.isMenuShowing()) {
				sm.toggle();
			} else {
				sm.showMenu();
			}
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

}
