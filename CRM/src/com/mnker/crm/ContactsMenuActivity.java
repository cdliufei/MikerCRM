package com.mnker.crm;

import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.auth.i;
//import com.baoyz.swipemenulistview.SwipeMenu;
//import com.baoyz.swipemenulistview.SwipeMenuCreator;
//import com.baoyz.swipemenulistview.SwipeMenuItem;
//import com.baoyz.swipemenulistview.SwipeMenuListView;
//import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.mnker.crm.MyLetterListView.OnTouchingLetterChangedListener;
import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.fragment.LoginFragment;
import com.mnker.crm.http.HttpXUtils;
import com.mnker.crm.http.URLS;
import com.mnker.crm.http.json.GetPlanParam;
import com.mnker.crm.http.json.ReContactsData;
import com.mnker.crm.tool.AsyncTaskControl;
import com.mnker.crm.tool.TaskNo;
import com.mnker.crm.tool.Tool;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.tool.AsyncTaskControl.IAirControl;
import com.mnker.crm.ui.ContactsDetailActivity;
import com.mnker.crm.widget.RightCharacterListView;
import com.mnker.crm.widget.util.LetterUtil;

public class ContactsMenuActivity extends Activity implements UiCallBack,View.OnClickListener {

	Context mContext = null;

	private ProgressDialog dialog;

	private ImageButton contact_list_back_btn;
	private RelativeLayout contact_list_back_btnll;

	private static final int PROGRESSDIALOGID = 1;

	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	private static final int PHONES_NUMBER_INDEX = 1;

	private static final int PHONES_PHOTO_ID_INDEX = 2;
	private static final int PHONES_CONTACT_ID_INDEX = 3;
	private static final int UPDATECONTACTLIST = 4;
	private static final int UPDATECONTACTALLLIST =5;
	
	private ArrayList<ReContactsData> itemsContent = new ArrayList<ReContactsData>();
	private ArrayList<ReContactsData> items = new ArrayList<ReContactsData>();
	private ArrayList<String> mContactsName = new ArrayList<String>();
	private ArrayList<String> mContactsNumber = new ArrayList<String>();
	private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
    private boolean isSelect;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATECONTACTLIST:
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
					}
				});
				
				break;
			case UPDATECONTACTALLLIST:
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						itemsContent.clear();
						itemsContent.addAll(items);
						adapter.notifyDataSetChanged();
					}
				});
				
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	private BaseAdapter adapter;

	private OverlayThread overlayThread;
	private TextView overlay;
	private String[] sections;
	RightCharacterListView letterListView = null;
	ListView mListView = null;
	private HashMap<String, Integer> alphaIndexer;
	public DisplayMetrics dm;
	public ArrayList<ReContactsData> mListItems = new ArrayList<ReContactsData>();
	public ArrayList<Integer> mListSectionPos = new ArrayList<Integer>();

	private EditText mSearchView;
	private TextView mEmptyView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list);
		try {
			dm= new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			if(getIntent().getExtras()!=null){
				isSelect=getIntent().getExtras().getBoolean("isSelect", false);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		initHead();
		alphaIndexer = new HashMap<String, Integer>();
		overlayThread = new OverlayThread();
		initOverlay();
		//init list
		initView();
		getContactsData();
		
	}	
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
		
	}
    private void initHead(){
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(this);
    }
	private void getContactsData() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_msg));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				DataBaseOperateSqliteDao helper = DataBaseOperateSqliteDao.getInstance(mContext);
				List<ReContactsData> mContactsDatas= helper.getContactsInfo(null);
				items.clear();
				items.addAll(mContactsDatas);
				Collections.sort(items);
				itemsContent.clear();
				itemsContent.addAll(items);
				return "";
			}
		};

		asyncTaskControl.execute(0, icontrol, this);

	}

	public void sendMessageBySystem(ReContactsData item){
		String phoneNo = item.mobile;
		Uri smsToUri = Uri.parse("smsto:"+phoneNo);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", "");
		startActivity(intent);
	}
	public void callFunction(ReContactsData item){
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNo = item.mobile;
        if(phoneNo!=null){
    		if(tm. SIM_STATE_READY == tm.getSimState() && !"".equals(phoneNo)){
    			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
    			startActivity(intent);
    	    }else{
    	    	Toast.makeText(this, "您的设备不支持拨打电话功能",
    	                Toast.LENGTH_LONG).show();
    	    	
    	    }
        }
		

	}
	private void initView() {
		contact_list_back_btn = (ImageButton) findViewById(R.id.contact_list_back_btn);
		contact_list_back_btnll = (RelativeLayout) findViewById(R.id.contact_list_back_btnll);
		mSearchView = (EditText) findViewById(R.id.search_view);
		mEmptyView = (TextView) findViewById(R.id.tv_nosearch);
		contact_list_back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		contact_list_back_btnll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mSearchView.addTextChangedListener(filterTextWatcher);
		adapter = new ListAdapter(this, itemsContent);
		mListView = (ListView) findViewById(R.id.contact_list);
		mListView.setAdapter(adapter);
		letterListView = (RightCharacterListView) findViewById(R.id.ContactLetterListView);
		letterListView.setListView(mListView, dm.heightPixels, dm.density);
		letterListView.setTextView(overlay);
//		letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				try {
					if(!isSelect){
						Intent intent = new Intent();
						intent.putExtra("contact", itemsContent.get(position));
						intent.setClass(ContactsMenuActivity.this,ContactsDetailActivity.class);
						startActivity(intent);
					}else {
						Intent intent = new Intent();
						intent.putExtra("contact", itemsContent.get(position));
						setResult(1, intent);
						ContactsMenuActivity.this.finish();
					}
			
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			if (adapter != null && str != null) {
				if(items.size()>0){
					mListItems.clear();
					for (ReContactsData item : items) {
						if (item.contactName.toLowerCase(Locale.getDefault()).indexOf(str.toLowerCase())!=-1) {
							mListItems.add(item);
						}
					}
					itemsContent.clear();
					itemsContent.addAll(mListItems);
					Message  updateMessage=Message.obtain();
					updateMessage.what=UPDATECONTACTLIST;
					handler.sendMessage(updateMessage);
				}
			}else {
				Message  updateMessage=Message.obtain();
				updateMessage.what=UPDATECONTACTALLLIST;
				handler.sendMessage(updateMessage);
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
	};

	/**
	 * ListViewAdapter
	 * 
	 * @author sy
	 * 
	 */
	private class ListAdapter extends BaseAdapter implements SectionIndexer{
		private LayoutInflater inflater;
		private List<ReContactsData> list;
		public ListAdapter(Context context, List<ReContactsData> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("zaokun", "position==="+position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
					convertView = inflater.inflate(R.layout.clist_item, null);
					holder.catalog_splitLine = (ImageView) convertView.findViewById(R.id.catalog_splitLine);
					holder.name = (TextView) convertView.findViewById(R.id.name);
					holder.accountName = (TextView) convertView.findViewById(R.id.accountName);
					holder.phone = (TextView) convertView.findViewById(R.id.phone);
					holder.tvCatalog = (TextView)convertView.findViewById(R.id.contactitem_catalog);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
				holder.name.setText(list.get(position).contactName);
				holder.accountName.setText("(" + list.get(position).accountName + ")");
				holder.phone.setText(list.get(position).mobile.toString());
				try {
					String catalog ="";
					String orderLabel = list.get(position).contactName ;
					if(orderLabel!=null && !"".equals(orderLabel)){
						catalog = Tool.converterToFirstSpell(orderLabel).substring(0, 1).toUpperCase();
					}else{
						catalog = "";
					}
					if(position == 0){
						holder.tvCatalog.setVisibility(View.VISIBLE);
						holder.tvCatalog.setText(catalog);
					}else{
						String lastCatalog = "";
						String preOrderLabel =list.get(position-1).contactName ;
							if(preOrderLabel!=null && !"".equals(preOrderLabel)){
								lastCatalog=Tool.converterToFirstSpell(preOrderLabel.substring(0, 1)).toUpperCase();
							}
						if(catalog.equals(lastCatalog)){
							holder.tvCatalog.setVisibility(View.GONE);
							holder.catalog_splitLine.setVisibility(View.GONE);
						}else{
							holder.tvCatalog.setVisibility(View.VISIBLE);
							holder.catalog_splitLine.setVisibility(View.VISIBLE);
							holder.tvCatalog.setText(catalog);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			return convertView;
		}

		private class ViewHolder {
			ImageView catalog_splitLine;
			TextView name;
			TextView accountName;
			TextView phone;
			TextView tvCatalog;
		}

		@Override
		public Object[] getSections() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getPositionForSection(int section) {
			for (int i = 0; i < list.size(); i++) { 
				if(list.get(i).contactName!=null && !"".equals(list.get(i).contactName)){
	            String l = list.get(i).firstChar;  
	            char firstChar = l.toUpperCase().charAt(0);  
	            if (firstChar == section) {  
	                return i;  
	            }  
				}
	        } 
			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}


	@Override
	protected void onDestroy() {
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		windowManager.removeViewImmediate(overlay);
		super.onDestroy();
	}

	private class LetterListViewListener implements OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mListView.setSelection(position);
				overlay.setText(s);
				// overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				handler.postDelayed(overlayThread, 1500);
			}
		}

	}
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}
	protected android.app.Dialog onCreateDialog(int id) {
		switch (id) {
		case PROGRESSDIALOGID:
			dialog = ProgressDialog.show(ContactsMenuActivity.this, null, "ͨ");
			break;
		default:
			return null;
		}
		return dialog;
	};

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	@Override
	public void updateUi(int taskNo, String data) {
//		adapter.notifyDataSetChanged();
		Log.d("zaokun", "updateUi itemsContentsize="+items.size());
//		adapter.notifyDataSetChanged();
		Message  updateMessage=Message.obtain();
		updateMessage.what=UPDATECONTACTLIST;
		handler.sendMessage(updateMessage);
//		setAdapter(items);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}
}
