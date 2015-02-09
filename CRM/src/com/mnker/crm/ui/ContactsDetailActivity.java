package com.mnker.crm.ui;

import com.mnker.crm.R;
import com.mnker.crm.http.json.ReContactsData;
import com.mnker.crm.tool.UiCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ContactsDetailActivity extends Activity implements UiCallBack,View.OnClickListener {
	private ReContactsData mReContactsData;
	private ImageView mymessage,sendtel;
	
//	private TextView phoneNumber,officePhoneNumber,contactType,marryStatus,spectName,birthday,departName,ownName,ownNameID,jobAssign,unionName,
//	
//	       unionID,masterManager,famPhoneNumber,faxNumber,emailNumber,postCode,address,jobName,isPrefessional,personStatus,specialFirst,overTimeFirst,
//	       specialSecond,overTimeSecond,specialThird,overTimeThird,overSchoolFirst,overSchoolSecond,societyJob,societyJobDesc,nationType,soceityTime,
//	       mateName,mateNumber,mateWorkPlace,mateBirthday,mateFamStatus,specialResult,workExperience,characater,healthStatus,like,mainLike,likeBook,fateBelive,eatType,
//	       custormerBuild,customerMethod,ownArea,ownProductType,speicalGroup,roomNumber,isSendMagzine;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			if(getIntent().getExtras()!=null){
				mReContactsData=(ReContactsData)getIntent().getExtras().getSerializable("contact");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_detail);
		initHead();
		initContent();
	}
    private void initHead(){
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(this);
    }
    private void initContent(){
    
    	mymessage=(ImageView)findViewById(R.id.mymessage);
    	sendtel=(ImageView)findViewById(R.id.sendtel);
    	mymessage.setOnClickListener(this);
    	sendtel.setOnClickListener(this);
    	((TextView)findViewById(R.id.name)).setText(mReContactsData.contactName+"");
    	((TextView)findViewById(R.id.accountName)).setText(mReContactsData.contactId+"");
    	
    	((TextView)findViewById(R.id.phoneNumber)).setText(mReContactsData.mobile+"");
    	((TextView)findViewById(R.id.officePhoneNumber)).setText(mReContactsData.officePhone+"");
    	((TextView)findViewById(R.id.contactType)).setText(mReContactsData.contactType+"");
    	((TextView)findViewById(R.id.marryStatus)).setText(mReContactsData.maritalName+"");
    	((TextView)findViewById(R.id.spectName)).setText(mReContactsData.salutationName+"");
    	((TextView)findViewById(R.id.birthday)).setText(mReContactsData.birthDate+"");
    	((TextView)findViewById(R.id.departName)).setText(mReContactsData.deptName+"");
    	((TextView)findViewById(R.id.ownName)).setText(mReContactsData.ownerName+"");
    	((TextView)findViewById(R.id.ownNameID)).setText(mReContactsData.ownerId+"");
    	
    	((TextView)findViewById(R.id.jobAssign)).setText(mReContactsData.position+"");
    	
    	((TextView)findViewById(R.id.unionName)).setText(mReContactsData.accountName+"");
    	((TextView)findViewById(R.id.unionID)).setText(mReContactsData.accountId+"");
    	((TextView)findViewById(R.id.masterManager)).setText(mReContactsData.reportTo+"");
    	((TextView)findViewById(R.id.famPhoneNumber)).setText(mReContactsData.homePhone+"");
    	((TextView)findViewById(R.id.faxNumber)).setText(mReContactsData.fax+"");
    	
    	((TextView)findViewById(R.id.emailNumber)).setText(mReContactsData.email+"");
    	((TextView)findViewById(R.id.postCode)).setText(mReContactsData.mailingZipCode+"");
    	((TextView)findViewById(R.id.address)).setText(mReContactsData.mailingAddress+"");
    	
    	((TextView)findViewById(R.id.jobName)).setText(mReContactsData.zc+"");
    	((TextView)findViewById(R.id.isPrefessional)).setText(mReContactsData.sfcgzjkcy+"");
    	
    	((TextView)findViewById(R.id.personStatus)).setText(mReContactsData.zcmm+"");
    	((TextView)findViewById(R.id.specialFirst)).setText(mReContactsData.byzyy+"");
    	((TextView)findViewById(R.id.overTimeFirst)).setText(mReContactsData.bysjy+"");
    	
    	((TextView)findViewById(R.id.specialSecond)).setText("");
    	((TextView)findViewById(R.id.overTimeSecond)).setText(mReContactsData.bysje+"");
    	((TextView)findViewById(R.id.specialThird)).setText(mReContactsData.byzy3+"");
    	((TextView)findViewById(R.id.overTimeThird)).setText(mReContactsData.bysj3+"");
    	((TextView)findViewById(R.id.overSchoolFirst)).setText(mReContactsData.byys1+"");
    	((TextView)findViewById(R.id.societyJob)).setText(mReContactsData.shzw+"");
    	((TextView)findViewById(R.id.societyJobDesc)).setText(mReContactsData.shzwms+"");
    	((TextView)findViewById(R.id.nationType)).setText(mReContactsData.mz+"");
    	((TextView)findViewById(R.id.soceityTime)).setText(mReContactsData.rzsj+"");
    	((TextView)findViewById(R.id.mateName)).setText(mReContactsData.pexm+"");
    	((TextView)findViewById(R.id.mateNumber)).setText(mReContactsData.pesj+"");
    	((TextView)findViewById(R.id.mateWorkPlace)).setText(mReContactsData.pegzdw+"");
    	((TextView)findViewById(R.id.mateBirthday)).setText(mReContactsData.posr+"");
    	((TextView)findViewById(R.id.mateFamStatus)).setText(mReContactsData.jtqk+"");
    	
    	((TextView)findViewById(R.id.specialResult)).setText(mReContactsData.zycg+"");
    	((TextView)findViewById(R.id.workExperience)).setText(mReContactsData.gzll+"");
    	((TextView)findViewById(R.id.characater)).setText(mReContactsData.xgtz+"");
    	((TextView)findViewById(R.id.healthStatus)).setText(mReContactsData.jkqk+"");
    	
    	((TextView)findViewById(R.id.like)).setText(mReContactsData.sh+"");
    	((TextView)findViewById(R.id.mainLike)).setText(mReContactsData.zyxxah+"");
    	((TextView)findViewById(R.id.likeBook)).setText(mReContactsData.xhdds+"");
    	((TextView)findViewById(R.id.fateBelive)).setText(mReContactsData.zjxy+"");
    	((TextView)findViewById(R.id.eatType)).setText(mReContactsData.cykw+"");
    	
    	((TextView)findViewById(R.id.custormerBuild)).setText(mReContactsData.khjsfa+"");
    	((TextView)findViewById(R.id.customerMethod)).setText(mReContactsData.khwhfa+"");
    	((TextView)findViewById(R.id.ownArea)).setText(mReContactsData.fzqy+"");
    	((TextView)findViewById(R.id.ownProductType)).setText(mReContactsData.fzcplb+"");
    	((TextView)findViewById(R.id.speicalGroup)).setText(mReContactsData.zyz+"");
    	((TextView)findViewById(R.id.roomNumber)).setText(mReContactsData.ks+"");
    	((TextView)findViewById(R.id.isSendMagzine)).setText(mReContactsData.sfjfmkzz+"");
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.mymessage:
			sendMessageBySystem(mReContactsData);
			break;
		case R.id.sendtel:
			callFunction(mReContactsData);
			break;
		default:
			break;
		}
	}
	@Override
	public void updateUi(int taskNo, String data) {
		// TODO Auto-generated method stub
		
	}
}
