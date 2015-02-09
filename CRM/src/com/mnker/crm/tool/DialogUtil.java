package com.mnker.crm.tool;


import com.mnker.crm.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.widget.DatePicker;

public class DialogUtil
{
	public final static int SHOW_MESSAGE = 0;
	public final static int SHOW_SUCCESS = 1;
	public final static int SHOW_ERROR = 2;
	public final static int SHOW_QUESTION = 3;
	
	private DialogUtil(){}

	public static Dialog getMessageDialog(Context context, String message, OnClickListener okClickListener)
	{
		return getMessageDialog(context, null, message, okClickListener);
	}
	public static Dialog getMessageDialog(Context context, String title, String message, OnClickListener okClickListener)
	{
		return getDialog(context, SHOW_MESSAGE, title, message, "确认", okClickListener, null, null);
	}
	
	public static Dialog getSuccessDialog(Context context, String message, OnClickListener okClickListener)
	{
		return getSuccessDialog(context, null, message, okClickListener);
	}
	public static Dialog getSuccessDialog(Context context, String title, String message, OnClickListener okClickListener)
	{
		return getDialog(context, SHOW_SUCCESS, title, message, "确认", okClickListener, null, null);
	}
	
	public static Dialog getErrorDialog(Context context, String message, OnClickListener okClickListener)
	{
		return getErrorDialog(context, null, message, okClickListener);
	}
	public static Dialog getErrorDialog(Context context, String title, String message, OnClickListener okClickListener)
	{
		return getDialog(context, SHOW_ERROR, title, message, "确认", okClickListener, null, null);
	}
	
	public static Dialog getCommonQuestionDialog(Context context, String message, OnClickListener okClickListener, OnClickListener cancelClickListener)
	{
		return getQuestionDialog(context, null, message, "确认", okClickListener, "取消", cancelClickListener);
	}
	public static Dialog getQuestionDialog(Context context, String message, String leftButtonText, OnClickListener leftClickListener, String rightButtonText, OnClickListener rightClickListener)
	{
		return getQuestionDialog(context, null, message, leftButtonText, leftClickListener, rightButtonText, rightClickListener);
	}
	public static Dialog getQuestionDialog(Context context, String title, String message, String leftButtonText, OnClickListener leftClickListener, String rightButtonText, OnClickListener rightClickListener)
	{
		return getDialog(context, SHOW_QUESTION, title, message, leftButtonText, leftClickListener, rightButtonText, rightClickListener);
	}
	
	public static Dialog getDialog(Context context, int showType, String title, String message, String leftButtonText, OnClickListener leftClickListener, String rightButtonText, OnClickListener rightClickListener)
	{
//		int iconResId = 0;
//		switch (showType)
//		{
//			case SHOW_SUCCESS:
//				title = StringUtil.isEmpty(title) ? "操作成功" : title;
//				iconResId = R.drawable.ic_success;
//				break;
//			case SHOW_ERROR:
//				title = StringUtil.isEmpty(title) ? "操作失败" : title;
//				iconResId = R.drawable.ic_error;
//				break;
//			case SHOW_QUESTION:
//				title = StringUtil.isEmpty(title) ? "提示" : title;
//				iconResId = R.drawable.ic_question;
//				break;
//			case SHOW_MESSAGE:
//				title = StringUtil.isEmpty(title) ? "提示" : title;
//				iconResId = R.drawable.ic_message;
//				break;
//		}
//		title ="CRM";
		title =context.getResources().getString(R.string.add_schedule_item_type);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
		customBuilder.setTitle(title)
			.setMessage(message)
//			.setIcon(iconResId)
			.setPositiveButton(leftButtonText, leftClickListener)
			.setNegativeButton(rightButtonText, rightClickListener);
        return customBuilder.create();
	}
	
	public static AlertDialog.Builder getThemeDialogBuilder(Context context)
	{
		if(VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2)
		{
			return getDialogBuilder11(context);
		}
		return new AlertDialog.Builder(context);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static void setDatePickerTheme(DatePicker datePicker)
	{
		if(VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2)
		{
			datePicker.setCalendarViewShown(false);
		}
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static void setActivityHoloTheme(Activity activity)
	{
		if(VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2)
		{
			activity.setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private static Builder getDialogBuilder11(Context context)
	{
		return new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
	}
}
