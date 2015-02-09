package com.mnker.crm;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageInputActivity extends Activity implements OnClickListener {

	private Context mContext;
	private EditText memoET;
	public static final String TEXT_FORMAT = "<font color='#0000ff'><b>%s</b></font>%d";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_live_weather_photo_share);
		mContext = this;
		initView();
	}

	private void initView() {
		ImageView backBtn = (ImageView) findViewById(R.id.iv_back);
		TextView title = (TextView) findViewById(R.id.tv_schedule_title);
		TextView confirm = (TextView) findViewById(R.id.tv_msg_confirm);

		memoET = (EditText) findViewById(R.id.et_input_info);
		final TextView inputCountTv = (TextView) findViewById(R.id.tv_input_count);
		String txtstr = String.format(TEXT_FORMAT, 200 + "/", 200);
		Spanned spt = Html.fromHtml(txtstr);
		inputCountTv.setText(spt);

		backBtn.setOnClickListener(this);
		confirm.setOnClickListener(this);

		Intent intent = getIntent();
		String titleName = intent.getStringExtra("title");
		if (!TextUtils.isEmpty(titleName)) {
			title.setText(titleName);
		}

		memoET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int count = s.toString().length();
				String txtstr = String.format(TEXT_FORMAT, (200 - count) + "/", 200);
				Spanned spt = Html.fromHtml(txtstr);
				inputCountTv.setText(spt);
			}
		});
		String memoStr = intent.getStringExtra("memo");
		if (!TextUtils.isEmpty(memoStr)) {
			memoET.setText(memoStr);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 100:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setTitle(R.string.share_notice_title);
			builder.setPositiveButton(R.string.share_notice_ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}
			});
			builder.setNegativeButton(R.string.share_notice_cancle, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.show();
			break;

		default:
			break;
		}

		return super.onCreateDialog(id);
	}

	@Override
	public void onClick(View v) {
		String memoString = memoET.getText().toString().trim();
		switch (v.getId()) {
		case R.id.iv_back:
			if (TextUtils.isEmpty(memoString)) {
				finish();
			} else {
				showDialog(100);
			}
			break;
		case R.id.tv_msg_confirm:
			if (!TextUtils.isEmpty(memoString)) {
				Intent intent = new Intent();
				intent.putExtra("memo", memoString);
				setResult(100, intent);
			}
			finish();
			break;

		default:
			break;
		}

	}

}
