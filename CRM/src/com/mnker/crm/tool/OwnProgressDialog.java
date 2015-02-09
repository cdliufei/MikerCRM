package com.mnker.crm.tool;

/**
 * 
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/**
 * 
 */
public class OwnProgressDialog extends ProgressDialog {
	// public static OwnProgressDialog ownProgressDialog;
	Context context;
	String message;

	AsyncTaskControl mAsyncTaskControl;
	public OwnProgressDialog(Context con, String msg) {
		super(con);
		// ownProgressDialog=this;
		context = con;
		message = msg;

	}

	public void SetAsyncTask(AsyncTaskControl task) {
		mAsyncTaskControl = task;
	}
	public void showDialog() {
		try {
			setMessage(message);
			setCancelable(true);
			setIndeterminate(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ���ضԻ���
	 */
	public void dissmissDialog() {
		try {
			if (mAsyncTaskControl != null)
				mAsyncTaskControl.cancel(true);
			Log.i("OwnProgressDialog", "dissmiss");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		if (mAsyncTaskControl != null)
			mAsyncTaskControl.cancel(true);
		Log.i("OwnProgressDialog", "onStop");
		super.onStop();
	}

}
