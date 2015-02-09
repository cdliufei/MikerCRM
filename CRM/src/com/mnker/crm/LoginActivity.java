package com.mnker.crm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.anim.CustomAnimation;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.ViewUtils;
import com.mnker.crm.fragment.LeftMenuFragment;
import com.mnker.crm.fragment.LoginFragment;
import com.mnker.crm.fragment.MainFragment;
import com.mnker.crm.widget.util.PreferencesService;
import com.mnker.crm.widget.util.ViewScaleUtil;

public class LoginActivity extends SlidingFragmentActivity implements OnClickListener {
	private Context mContext;
	private LoginFragment mLoginFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_login);
		mContext = this;

		ViewScaleUtil.setScale(this);
		ViewUtils.inject(this);
		setContentViews();
	}

	private void setContentViews() {
		FragmentManager fm = getSupportFragmentManager();
		SlidingMenu sm = getSlidingMenu();
		// 背景
		sm.setBackgroundColor(Color.rgb(37, 37, 37));
		// 阴影
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.slide_menu_shadow);
		// 偏移
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		if (metrics.widthPixels > 0) {
			// 资源配置，在不同分辨率，总会有出现别扭的机型，
			// 可以通过屏幕实际宽度，按比例配置偏移，比如：黄金比例
			sm.setBehindOffset((int) (metrics.widthPixels * 0.382));
		} else {
			// 通过资源配置偏移
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		}
		// 设置侧滑栏动画
		sm.setBehindCanvasTransformer((new CustomAnimation()).getCustomZoomAnimation());

		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		sm.setMode(SlidingMenu.RIGHT);

		// 添加导航内容
		setContentView(R.layout.slide_login_content_frame);
		// 设置首页默认显示
		mLoginFragment = new LoginFragment();

		String token = PreferencesService.getInfo("token");
		if (!TextUtils.isEmpty(token)) {
			// mLoginFragment.skipLogin(this);
			loginSuccess();
		} else {
			replaceFragment(fm, mLoginFragment);
		}

		// 设置左侧滑栏内容
		LeftMenuFragment lmf = new LeftMenuFragment();
		setBehindContentView(R.layout.slide_menu_frame);
		fm.beginTransaction().replace(R.id.menu_frame, lmf).commit();
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
		// 执行替换
		FragmentTransaction trans = fm.beginTransaction();
		if (null == fm.findFragmentByTag(tag)) {
			trans.replace(R.id.content_frame, mLoginFragment, tag);
			// 不存在时，添加到stack，避免切换时，先前的被清除{fm.getFragments()}
			// {存在时，不添加，避免BackStackEntry不断累加}
			// trans.addToBackStack(tag);
			// } else {
			// trans.replace(R.id.content_frame, fm.findFragmentByTag(tag),
			// tag);
		}
		trans.commit();
	}

	private void login() {
		Intent intent = new Intent();
		intent.setClass(mContext, MainActivity.class);
		startActivity(intent);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		final Dialog connectDialog = new Dialog(mContext, R.style.my_dialog_theme);
		View view = getLayoutInflater().inflate(R.layout.client_setting_dialog, null);
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
		switch (v.getId()) {
		case R.id.iv_login:
			break;
		case R.id.tv_connect_setting:
			showDialog(100);
			break;

		default:
			break;
		}
	}

	public void loginSuccess() {
		FragmentManager fm = getSupportFragmentManager();
		String tag = String.valueOf(R.id.content_frame);
		// 执行替换
		FragmentTransaction trans = fm.beginTransaction();
		trans.remove(mLoginFragment);
		trans.replace(R.id.content_frame, new MainFragment(), tag);
		// 不存在时，添加到stack，避免切换时，先前的被清除{fm.getFragments()}
		// {存在时，不添加，避免BackStackEntry不断累加}
		// trans.addToBackStack(tag);
		trans.commit();
	}

	public void logOut() {
		SlidingMenu sm = ((LoginActivity) mContext).getSlidingMenu();
		if (sm.isMenuShowing()) {
			sm.toggle();
		} else {
			sm.showMenu();
		}
		FragmentManager fm = getSupportFragmentManager();
		String tag = String.valueOf(R.id.content_frame);
		// 执行替换
		FragmentTransaction trans = fm.beginTransaction();
		trans.replace(R.id.content_frame, new LoginFragment(), tag);
		// 不存在时，添加到stack，避免切换时，先前的被清除{fm.getFragments()}
		// {存在时，不添加，避免BackStackEntry不断累加}
		// trans.addToBackStack(tag);
		trans.commit();
	}

	public void synServerData() {
		mLoginFragment.skipLogin(this);
	}
}
