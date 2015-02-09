package com.mnker.crm.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.mnker.crm.LoginActivity;
import com.mnker.crm.R;
import com.mnker.crm.db.DataBaseOperateSqliteDao;
import com.mnker.crm.http.HttpXUtils;
import com.mnker.crm.http.URLS;
import com.mnker.crm.http.json.CheckVersionParam;
import com.mnker.crm.http.json.RePlan;
import com.mnker.crm.http.json.util.JsonUtils;
import com.mnker.crm.tool.AsyncTaskControl;
import com.mnker.crm.tool.AsyncTaskControl.IAirControl;
import com.mnker.crm.tool.TaskNo;
import com.mnker.crm.tool.UiCallBack;
import com.mnker.crm.tool.Utilities;
import com.mnker.crm.widget.util.PreferencesService;
import com.mnker.crm.widget.util.Util;

public class LeftMenuFragment extends Fragment implements OnClickListener {
	private static final String TAG = "LeftMenuFragment";
	private String url;
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.slid_setting, container, false);
		getAndSetViews(view);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mContext = activity;
		super.onAttach(activity);
	}

	private void getAndSetViews(View view) {
		view.findViewById(R.id.iv_usericon).setOnClickListener(this);
		TextView userName = (TextView) view.findViewById(R.id.tv_username);
		TextView checkTv = (TextView) view.findViewById(R.id.tv_check);
		checkTv.setOnClickListener(this);
		userName.setText(PreferencesService.getInfo("username") == null ? "我" : PreferencesService.getInfo("username"));
		userName.setOnClickListener(this);
		view.findViewById(R.id.tv_connect_http).setOnClickListener(this);
		view.findViewById(R.id.tv_sync).setOnClickListener(this);
		view.findViewById(R.id.tv_logout).setOnClickListener(this);
		url = PreferencesService.getInfo("url");
		// if (url != null) {
		// URLS.baseUrl = url;
		// }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_usericon:
			break;
		case R.id.tv_check:
			checkAPP();
			break;
		case R.id.tv_username:
			break;
		case R.id.tv_connect_http:
			onCreateDialog(100);
			break;
		case R.id.tv_sync:
			synData();
			SlidingMenu sm = ((LoginActivity) mContext).getSlidingMenu();
			if (sm.isMenuShowing()) {
				sm.toggle();
			} else {
				sm.showMenu();
			}
			break;
		case R.id.tv_logout:
			PreferencesService.clear();
			DataBaseOperateSqliteDao.getInstance(getActivity()).clearAllTables();
			// saveData("token", "");
			// saveData("token", "");
			// saveData("imei", "");
			// saveData("uuid", "");
			// saveData("deptName", "");
			// saveData("deptId", "");
			// saveData("actTypeskey", "");
			// saveData("actTypesvalue", "");
			((LoginActivity) getActivity()).logOut();
			break;
		}
	}

	private void synData() {
		((LoginActivity) mContext).synServerData();
	}

	private void checkAPP() {
		AsyncTaskControl asyncTaskControl = new AsyncTaskControl(mContext, mContext.getResources().getString(R.string.loading_asyn_checkapp));
		IAirControl icontrol = new IAirControl() {
			@Override
			public String airControl() {
				CheckVersionParam checkParam = new CheckVersionParam();
				checkParam.os = "android";
				checkParam.version = getVersionCode(mContext) + "";

				final String ret = HttpXUtils.getInfoByPostObject(URLS.checkVersion, checkParam, PreferencesService.getInfo("token"));
                String resultString="error";
				try {
					// TODO Auto-generated method stub

					JSONObject retJsonObject = null;
					retJsonObject = new JSONObject(ret);
					if (retJsonObject != null && !retJsonObject.isNull("hasNew")) {
						if (retJsonObject.getBoolean("hasNew")) {
							((LoginActivity) mContext).runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(mContext, mContext.getResources().getString(R.string.hasnewapp), 0).show();
								}
							});
				
//							String input = HttpXUtils.getInfoByPostObject(URLS.download, checkParam, PreferencesService.getInfo("token"));
//
//							downloadApk(input.getBytes());
							resultString="update";
						} else {
							resultString="notneed";
							((LoginActivity) mContext).runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(mContext, mContext.getResources().getString(R.string.nonewapp), 0).show();
								}
							});
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					resultString="error";
					((LoginActivity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(mContext, mContext.getResources().getString(R.string.net_err), 0).show();
						}
					});
				}

				return resultString;
			}
		};
		UiCallBack uiCallBack = new UiCallBack() {

			@Override
			public void updateUi(int taskNo, String data) {
				if(data!=null && "update".equals(data)){
					Utilities.dialog(getActivity(),  mContext.getResources().getString(R.string.hasnewapp), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							Intent browserIntent= new Intent(Intent.ACTION_VIEW, Uri.parse(URLS.download));
							getActivity().startActivity(browserIntent);
							getActivity().finish();
						}
					}, null);
				}


			}
		};
		asyncTaskControl.execute(TaskNo.LoginFragment_getplan, icontrol, uiCallBack);
	}

	private void downloadApk(byte[] bs) {
		// if
		// (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		// {
		// String sdpath = Environment.getExternalStorageDirectory() +
		// File.separator + "download";
		// FileOutputStream fos = null;
		// InputStream is = null;
		// try {
		// URL url = new URL(mHashMap.get("url_high"));
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.connect();
		// int length = conn.getContentLength();
		// is = conn.getInputStream();
		// File file = new File(sdpath);
		// if (!file.exists()) {
		// file.mkdir();
		// }
		// File apkFile = new File(sdpath, mHashMap.get("name_high"));
		// installPath = apkFile.getAbsoluteFile();
		// fos = new FileOutputStream(apkFile);
		// int count = 0;
		// int numread = 0;
		// byte buf[] = new byte[1024];
		// int progressSpilit = 0;
		// while ((numread = is.read(buf)) != -1) {
		// count += numread;
		// if (progressSpilit == 0 || (int) (count * 100 / length) - 10 >
		// progressSpilit) {
		// progressSpilit += 10;
		// Message msg = Message.obtain();
		// msg.arg1 = count;
		// msg.arg2 = length;
		// msg.what = DOWN_UPDATE;
		// updateHandler.sendMessage(msg);
		// }
		// fos.write(buf, 0, numread);
		// }
		// // 下载完成
		// updateHandler.sendEmptyMessage(DOWN_FINISHED);
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// try {
		// if (fos != null) {
		// fos.close();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// if (is != null) {
		// is.close();
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// } else {
		// 下载失败
		// }
	}

	private int getVersionCode(Context context) {
		int versionName = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			String soft_name = context.getApplicationContext().getPackageName();
			versionName = context.getPackageManager().getPackageInfo(soft_name, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	protected Dialog onCreateDialog(int id) {

		final Dialog connectDialog = new Dialog(getActivity(), R.style.my_dialog_theme);
		View view = getActivity().getLayoutInflater().inflate(R.layout.client_setting_dialog, null);
		connectDialog.setContentView(view);

		TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
		TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
		final EditText connectHttp = (EditText) view.findViewById(R.id.et_connect_http);
		url = PreferencesService.getInfo("url");
		if (TextUtils.isEmpty(url)) {
			url = URLS.defaultUrl;
			URLS.baseUrl = URLS.defaultUrl;
			PreferencesService.setPreferences("url", url);
		}
		connectHttp.setText(url);
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
					PreferencesService.setPreferences("url", connectHttp.getText().toString().trim());
					URLS.baseUrl = PreferencesService.getInfo("url");
				}
			}
		});

		connectDialog.setCancelable(false);
		connectDialog.setCanceledOnTouchOutside(false);
		connectDialog.show();
		return connectDialog;
	}

	private final static int MSG_SUPPORT = 1;
	private final static int MSG_DOWNLOAD_DIALOG = 2;
	private final static int MSG_SCORE_DIALOG = 3;
	private Handler mHandle = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUPPORT:
				// getSupportDialog();
				break;
			case MSG_DOWNLOAD_DIALOG:
				// downloadDialog();
				break;
			case MSG_SCORE_DIALOG:
				// scoreDialog();
				break;
			}
			return false;
		}
	});

	// private void getSupportDialog() {
	// showWaitDialog();
	// }

	// private void downloadDialog() {
	// new
	// AlertDialog.Builder(getActivity()).setTitle(R.string.str_ma_get_source_ok_dialog_title)
	// .setMessage(String.format(getResources().getString(R.string.str_ma_get_source_ok_dialog_msg),
	// Utils.mkTmpDirs()))
	// .setPositiveButton(android.R.string.ok, new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// copyBackground();
	// dialog.dismiss();
	// }
	// }).setCancelable(false).create().show();
	// }

	// private void copyBackground() {
	// showWaitDialog();
	// new AsyncTask<Object, Object, String>() {
	// @Override
	// protected String doInBackground(Object... params) {
	// return doAsyncOperations();
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// dismissWaitDialog();
	// showConfirmDialog(result);
	// }
	// }.execute(null, null, null);
	// }

	// private void showConfirmDialog(String result) {
	// new
	// AlertDialog.Builder(getActivity()).setTitle(R.string.str_ma_get_source_dialog_title_ok)
	// .setMessage(String.format(getResources().getString(R.string.str_ma_get_source_dialog_msg_ok),
	// result))
	// .setNegativeButton(R.string.str_ma_get_source_dialog_btn_rego, new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// AppConnect.getInstance(getActivity()).showOffers(getActivity());
	// }
	// }).setPositiveButton(android.R.string.ok, new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// ;
	// }
	// }).setCancelable(false).create().show();
	// }

	// private String doAsyncOperations() {
	// String dir = Utils.mkTmpDirs();
	// // First clear directory
	// File dirs = new File(dir);
	// if (dirs.exists()) {
	// File[] files = dirs.listFiles();
	// if (files.length > 0) {
	// for (File f : files) {
	// f.delete();
	// }
	// }
	// }
	// // Do copy
	// File file = new File(dir, Utils.FILE_BASE_NAME +
	// System.currentTimeMillis() + ".rar");
	// String origin = Utils.getFilePathName(Utils.FILE_ASSETS_PATH,
	// Utils.FILE_BASE_NAME);
	// try {
	// InputStream is = getActivity().getAssets().open(origin);
	// String path = saveFileToLocal(file, is);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return file.getAbsolutePath();
	// }

	private String saveFileToLocal(File file, InputStream input) {
		byte[] buffer = new byte[8192];
		int n = 0;
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			while ((n = input.read(buffer)) >= 0) {
				output.write(buffer, 0, n);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			try {
				file.delete();
			} catch (Exception ex) {
			}
		} finally {
			if (null != output) {
				try {
					output.flush();
					output.close();
				} catch (IOException e) {
				}
			}
		}
		return file.getAbsolutePath();
	}

	// private void scoreDialog() {
	// new
	// AlertDialog.Builder(getActivity()).setTitle(R.string.str_ma_get_source_dialog_title).setMessage(R.string.str_ma_get_source_dialog_msg)
	// .setPositiveButton(R.string.str_ma_get_source_dialog_btn_go, new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// AppConnect.getInstance(getActivity()).showOffers(getActivity());
	// }
	// }).setNegativeButton(R.string.str_ma_get_source_dialog_btn_cancel, new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// ;
	// }
	// }).setCancelable(false).create().show();
	// }

	private ProgressDialog mWaitDialog;

	// private void showWaitDialog() {
	// if (null == mWaitDialog) {
	// mWaitDialog = new ProgressDialog(getActivity());
	// mWaitDialog.setMessage(getActivity().getResources().getString(R.string.str_loading_wait));
	// mWaitDialog.setCancelable(false);
	// }
	// if (!mWaitDialog.isShowing()) {
	// mWaitDialog.show();
	// }
	// }

	public void dismissWaitDialog() {
		if (null != mWaitDialog && mWaitDialog.isShowing()) {
			mWaitDialog.dismiss();
		}
	}
}
