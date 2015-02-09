package com.mnker.crm.tool;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncTaskControl extends AsyncTask<Void, Integer, String> {

	private static final String Tag = "AsyncTaskControl";
	private Context context;
	private String dialogMsg;
	private OwnProgressDialog dialog;
	private UiCallBack _uiCallback;
	private int taskNo;

	public interface IAirControl {
		public String airControl();
	}

	private IAirControl _airControl;

	public AsyncTaskControl(Context con, String dialogMsg) {
		context = con;
		this.dialogMsg = dialogMsg;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (dialogMsg != null) {
			dialog = new OwnProgressDialog(context, dialogMsg);
			dialog.SetAsyncTask(this);
			dialog.showDialog();
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		String ret = null;
		if (_airControl != null) {
			ret = _airControl.airControl();
		}
		return ret;
	}

	@Override
	protected void onPostExecute(String ret) {
		// add by huxiaodong 2013-10-10
		// handler.removeCallbacks(runnable);
		// end
		if (_uiCallback != null)// ˢ��UI
			_uiCallback.updateUi(this.taskNo, ret);
		if (dialog != null && dialog.isShowing())// ���ضԻ���
			dialog.dissmissDialog();

		return;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		return;
	}

	public void execute(int taskNo, IAirControl icontrol, UiCallBack icallback) {
		this.taskNo = taskNo;
		_airControl = icontrol;
		_uiCallback = icallback;
		execute();

	}

}
