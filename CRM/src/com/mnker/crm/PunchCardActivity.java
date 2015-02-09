package com.mnker.crm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import org.json.JSONObject;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;
import com.mnker.crm.PunchCardActivity.PunchAdapter;
import com.mnker.crm.baidu.location.GeoCoderAddress;
import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.fragment.LoginFragment;
import com.mnker.crm.fragment.WorkScheduleFragment;
import com.mnker.crm.http.HttpXUtils;
import com.mnker.crm.http.URLS;
import com.mnker.crm.http.json.GetPlanParam;
import com.mnker.crm.http.json.PunchCard;
import com.mnker.crm.http.json.RePlan;
import com.mnker.crm.http.json.util.JsonUtils;
import com.mnker.crm.tool.AsyncTaskControl;
import com.mnker.crm.tool.TaskNo;
import com.mnker.crm.tool.Tool;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.tool.AsyncTaskControl.IAirControl;
import com.mnker.crm.widget.util.PreferencesService;
import com.mnker.crm.widget.util.Util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PunchCardActivity extends FragmentActivity implements OnClickListener, UiCallBack {
	private TextView tvPunchTime;
	private TextView tvPunchNowTime;
	private Context mContext;
	private ListView mListView;
	private PunchAdapter mAdapter;
	private List<PunchCard> punchCards = new ArrayList<PunchCard>();
	private Timer mTimer;
	private GeoCoderAddress pointLcation;
	protected LatLng addDt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.punch_card);
		mContext = this;
		initView();
		// addView();
		upLoadPuchInfo();
		initTimer();
		initPointLocation();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				addDt = (LatLng) msg.obj;
				if (addDt != null) {
					PreferencesService.setLong("latitude", (long) addDt.latitude);
					PreferencesService.setLong("longitude", (long) addDt.longitude);
					System.out.println("gps == lati == " + PreferencesService.getLong("latitude") + " longi = " + PreferencesService.getLong("longitude"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};
	private PunchCard punchCard;
	private String uuid;

	private void initPointLocation() {
		pointLcation = new GeoCoderAddress(PunchCardActivity.this, handler);
		pointLcation.start();
	}

	private void initTimer() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				PunchCardActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						tvPunchTime.setText(Util.getPunchTime());
					}
				});
			}
		}, 2000, 5000);
	}

	private void updateList() {

	}

	private void initListView() {

	}

	@Override
	protected void onDestroy() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		pointLcation.finish();
		super.onDestroy();
	}

	private void initView() {
		tvPunchTime = (TextView) findViewById(R.id.punch_time);
		tvPunchNowTime = (TextView) findViewById(R.id.tv_punch_record_now_time);
		ImageView ivPunch = (ImageView) findViewById(R.id.iv_punch);
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		mListView = (ListView) findViewById(R.id.lv_listview);

		mAdapter = new PunchAdapter(this, punchCards);

		mListView.setAdapter(mAdapter);

		ivBack.setOnClickListener(this);
		ivPunch.setOnClickListener(this);

		tvPunchTime.setText(Util.getPunchTime());
	}

	private void addView() {
		FragmentManager fm = getSupportFragmentManager();
		String tag = String.valueOf(R.id.content_frame);
		// 执行替换
		FragmentTransaction trans = fm.beginTransaction();
		trans.replace(R.id.content_frame, new WorkScheduleFragment(), tag);
		// 不存在时，添加到stack，避免切换时，先前的被清除{fm.getFragments()}
		// {存在时，不添加，避免BackStackEntry不断累加}
		trans.addToBackStack(tag);
		trans.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_schedule_add:
			break;
		case R.id.tv_schedule_qurey:

			break;
		case R.id.iv_punch:
			punchCard();
			break;
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}

	}

	private void punchCard() {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		long preTime = PreferencesService.getLong("punchTime");
		Log.d("zaokun", "" + preTime + "  currentTime=" + currentTime);
		if (currentTime - preTime > 60 * 1000) {

			PreferencesService.setLong("punchTime", currentTime);

			String punchTime;
			punchTime = Util.getPunchNowTime();
			saveDB(punchTime);
			refreshUI(punchTime);
			upLoadPuchInfoToServer(punchTime);
			upLoadPuchInfo();
		} else {
			// showDialog(100);
			Toast.makeText(this, R.string.punch_once_err, 1).show();
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// Builder builder = new AlertDialog.Builder(this);
		return super.onCreateDialog(id);
	}

	private void saveDB(String punchTime) {
		DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(this);
		punchCard = new PunchCard();
		// punchCard.uuid = PreferencesService.getInfo("uuid");
		uuid = Tool.getUUID();
		punchCard.uuid = uuid;
		punchCard.imei = PreferencesService.getInfo("imei");
		punchCard.signTime = punchTime;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000");

		punchCard.x = df.format(PreferencesService.getLong("latitude"));
		punchCard.y = df.format(PreferencesService.getLong("longitude"));
		dBhelper.insertPunchInfo(punchCard);
	}

	private List<PunchCard> getFromDb(String uuid) {
		Log.d("zaokun", "getFromDb uuid==" + uuid);
		DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(this);
		return dBhelper.getPunchInfo(null, null);
	}

	private void upLoadPuchInfo() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				punchCards = getFromDb(uuid);
				return "";
			}

		};
		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String data) {
				setAdapter();
			}
		};
		asyncTaskControl.execute(TaskNo.PunchCardActivity_uoload, icontrol, uiCallBack);
	}

	private void upLoadPuchInfoToServer(final String punchTime) {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				PunchCard punchCard = new PunchCard();
				punchCard.uuid = uuid;
				punchCard.imei = PreferencesService.getInfo("imei");
				java.text.DecimalFormat df = new java.text.DecimalFormat("#.000");

				punchCard.x = df.format(PreferencesService.getLong("latitude"));
				punchCard.y = df.format(PreferencesService.getLong("longitude"));

				punchCard.signTime = Util.getPunchTime(punchTime);

				List<PunchCard> list = new ArrayList<PunchCard>();
				list.add(punchCard);
				String ret = HttpXUtils.getInfoByPostObject(URLS.uploadSign, list, PreferencesService.getInfo("token"));
				return ret;
			}

		};
		// UiCallBack uiCallBack = new UiCallBack() {
		//
		// @Override
		// public void updateUi(int taskNo, String data) {
		// setAdapter();
		// }
		// };
		asyncTaskControl.execute(TaskNo.PunchCardActivity_uoload, icontrol, this);
	}

	protected void setAdapter() {
		if (mListView != null) {
			mAdapter = new PunchAdapter(this, punchCards);
			mListView.setAdapter(mAdapter);
		}
	}

	private void refreshUI(CharSequence punchTime) {

		// tvPunchNowTime.setText(punchTime);
	}

	@Override
	public void updateUi(int taskNo, String ret) {
		switch (taskNo) {
		case TaskNo.PunchCardActivity_uoload:

			try {
				JSONObject retJsonObject = new JSONObject(ret);
				if (retJsonObject != null && !retJsonObject.isNull("success")) {
					if (retJsonObject.getString("success").equalsIgnoreCase("true")) {
						Toast.makeText(mContext, mContext.getResources().getString(R.string.daka_success), 0).show();
						DataBaseOperateSqliteDao dBhelper = DataBaseOperateSqliteDao.getInstance(this);
						dBhelper.updateOfflinePunchInfoSuccess(punchCard);
					} else {
						Toast.makeText(mContext, mContext.getResources().getString(R.string.daka_fail), 0).show();
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(mContext, getResources().getString(R.string.daka_fail), 0).show();

			}
			break;

		default:
			break;
		}
	}

	public class PunchAdapter extends BaseAdapter {
		List<PunchCard> punchCards;
		Context mContext;

		PunchAdapter(Context context, List<PunchCard> punchCards) {
			this.punchCards = punchCards;
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return punchCards.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return punchCards.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHoler holer;
			if (convertView == null) {
				holer = new ViewHoler();
				convertView = getLayoutInflater().inflate(R.layout.punch_card_item, null);
				holer.tv_punch_record_cicle = (TextView) convertView.findViewById(R.id.tv_punch_record_cicle);
				holer.tv_punch_record_now_time = (TextView) convertView.findViewById(R.id.tv_punch_record_now_time);
				holer.tv_punch_record_now_msg = (TextView) convertView.findViewById(R.id.tv_punch_record_now_msg);
				convertView.setTag(holer);
			} else {
				holer = (ViewHoler) convertView.getTag();
			}

			PunchCard punchCard = punchCards.get(position);

			holer.tv_punch_record_now_time.setText(punchCard.signTime);
			holer.tv_punch_record_now_msg.setText(R.string.tv_punch_record_now_msg);
			if (position == 0) {
				holer.tv_punch_record_cicle.setTextColor(Color.WHITE);
				holer.tv_punch_record_now_time.setTextColor(Color.WHITE);
				holer.tv_punch_record_now_msg.setTextColor(Color.WHITE);
				setBack(Color.WHITE, holer.tv_punch_record_cicle);
			} else {
				holer.tv_punch_record_cicle.setTextColor(Color.GRAY);
				holer.tv_punch_record_now_time.setTextColor(Color.GRAY);
				holer.tv_punch_record_now_msg.setTextColor(Color.GRAY);
				setBack(Color.GRAY, holer.tv_punch_record_cicle);
			}

			return convertView;
		}

		class ViewHoler {
			TextView tv_punch_record_cicle;
			TextView tv_punch_record_now_time;
			TextView tv_punch_record_now_msg;
		}

	}

	public void setBack(int color, View view) {
		GradientDrawable mDrawable = (GradientDrawable) view.getBackground();
		mDrawable.setColor(color);
		view.setBackgroundDrawable(mDrawable);
		view.invalidate();
	}
}
