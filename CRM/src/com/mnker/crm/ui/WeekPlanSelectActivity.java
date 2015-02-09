package com.mnker.crm.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mnker.crm.R;
import com.mnker.crm.base.IntentNo;
import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.http.json.ReContactsData;
import com.mnker.crm.http.json.RePlanData;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.ui.adpater.WeekPlanAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class WeekPlanSelectActivity extends Activity implements UiCallBack,View.OnClickListener {
	private ListView weekplanlist;
	private EditText mSearchView;
	private WeekPlanAdapter mWeekPlanAdapter;
	private List<RePlanData> listData=new ArrayList<RePlanData>();
	private ArrayList<RePlanData> items = new ArrayList<RePlanData>();
	private ArrayList<RePlanData> mListItems = new ArrayList<RePlanData>();
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			mWeekPlanAdapter.notifyDataSetChanged();
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weekplanlist);
		initHead();
		initContent();
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			
				List<RePlanData> temArrayList=gettWorkPlan();
				items.addAll(temArrayList);
				listData.addAll(items);
				mHandler.sendEmptyMessage(0);
				}
		});
	}
    private void initHead(){
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				WeekPlanSelectActivity.this.finish();
				
			}
		});
    }
    private void initContent(){
    	mSearchView = (EditText) findViewById(R.id.search_view);
    	weekplanlist=(ListView)findViewById(R.id.weekplanlist);
    	mWeekPlanAdapter=new WeekPlanAdapter(WeekPlanSelectActivity.this, listData);
    	weekplanlist.setAdapter(mWeekPlanAdapter);
    	weekplanlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Autto-generated method stub
				Intent dataIntent=new Intent();
				dataIntent.putExtra("weekplan", listData.get(arg2));
				setResult(IntentNo.GOTOSELECTWEEKPLAN, dataIntent);
				WeekPlanSelectActivity.this.finish();
			}
		});
    	mSearchView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String str = arg0.toString();
				if (mWeekPlanAdapter != null && str != null) {
					if(items.size()>0){
						mListItems.clear();
						for (RePlanData item : items) {
							if (item.planName.toLowerCase(Locale.getDefault()).indexOf(str.toLowerCase())!=-1) {
								mListItems.add(item);
							}
						}
						listData.clear();
						listData.addAll(mListItems);
		                mHandler.sendEmptyMessage(0);
					}
				}else {
					listData.clear();
					listData.addAll(items);
					mWeekPlanAdapter.notifyDataSetChanged();
				}
			}
		});
    }
	@Override
	public void updateUi(int taskNo, String data) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	private List<RePlanData> gettWorkPlan() {
		DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao
				.getInstance(WeekPlanSelectActivity.this);
		List<RePlanData> rePlanDatas = helper.getWeekPlanInfo(0, "uuid", null);
		return rePlanDatas;
	}
}
