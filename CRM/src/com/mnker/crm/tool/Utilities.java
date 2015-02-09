package com.mnker.crm.tool;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mnker.crm.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Utilities {
	
	 public static final OnClickListener defaultListener = new OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    };
	    
	public static String getData(String string) {
		JSONObject obj;
		try {
			obj = new org.json.JSONObject(string);
			return obj.getJSONObject("data").toString();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
	public static String getDataStr(String string) {
       JSONObject obj;
       try {
           obj = new org.json.JSONObject(string);
           return obj.getString("data");
       } catch (Exception e) {

           e.printStackTrace();
       }
       return null;
   }
	public static String getSendOk(String string) {
		JSONObject obj;
		try {
			obj = new org.json.JSONObject(string);
			return obj.getString("sendOK");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public static String getStatus(String string){
	    JSONObject obj;
       try {
           obj = new org.json.JSONObject(string);
           return obj.getString("status");
       } catch (JSONException e) {
           e.printStackTrace();
           return null;
       }
	}
	
	public static String getResult(String string) {
       JSONObject obj;
       try {
           obj = new org.json.JSONObject(string);
           return obj.getJSONObject("results").toString();
       } catch (Exception e) {

           e.printStackTrace();
       }
       return null;
   }
	
   public static Dialog dialog(Context context, String message,
           OnClickListener okListener, OnClickListener cancelListener) {
//       AlertDialog.Builder builder = new AlertDialog.Builder(context);
//       if (okListener != null) {
//           builder.setPositiveButton("确认", okListener);
//       } else {
//           builder.setPositiveButton("确", defaultListener);
//       }
//       
//       if (cancelListener != null) {
//           builder.setNegativeButton("取消", cancelListener);
//       } else {
//           builder.setNegativeButton("取消", defaultListener);
//       }
//       
//       builder.setCancelable(true);
//
//       if (message != null) {
//           builder.setMessage(message);
//       }
//
//       AlertDialog dialog = builder.show();
//       return dialog;
	   Dialog dialog = DialogUtil.getQuestionDialog(context, context.getResources().getString(R.string.app_name), message, 
			   "确认", okListener == null ? defaultListener : okListener, 
			   "取消", cancelListener == null ? defaultListener : cancelListener);
	   dialog.setCancelable(true);
	   dialog.show();
	   return dialog;
   }
   
   public static Dialog dialogCheck(Context context, String message,
           OnClickListener okListener, OnClickListener cancelListener) {
//       AlertDialog.Builder builder = new AlertDialog.Builder(context);
//       if (okListener != null) {
//           builder.setPositiveButton("是", okListener);
//       } else {
//           builder.setPositiveButton("是", defaultListener);
//       }
//       
//       if (cancelListener != null) {
//           builder.setNegativeButton("否", cancelListener);
//       } else {
//           builder.setNegativeButton("否", defaultListener);
//       }
//       
//       builder.setCancelable(true);
//
//       if (message != null) {
//           builder.setMessage(message);
//       }
//
//       AlertDialog dialog = builder.show();
//       return dialog;
	   Dialog dialog = DialogUtil.getQuestionDialog(context, context.getResources().getString(R.string.app_name), message, 
			   "是", okListener == null ? defaultListener : okListener, 
			   "否", cancelListener == null ? defaultListener : cancelListener);
	   dialog.setCancelable(true);
	   dialog.show();
	   return dialog;
   }
   
   public static Dialog agreedialog(Context context, String message,
           OnClickListener okListener, OnClickListener cancelListener) {
       AlertDialog.Builder builder = new AlertDialog.Builder(context);
       if (okListener != null) {       
           builder.setPositiveButton("Agree", okListener);
       } else {
           builder.setPositiveButton("Agree", defaultListener);
       }
       
       if (cancelListener != null) {
           builder.setNegativeButton("Reselect", cancelListener);
       } else {
           builder.setNegativeButton("Reselect", defaultListener);
       }
       
       builder.setCancelable(true);

       if (message != null) {
           builder.setMessage(message);
       }

       AlertDialog dialog = builder.show();
       return dialog;
   }
   
	public static void showInfo(Context c,String message) {		
//		Builder builder = new AlertDialog.Builder(c);
//		builder.setMessage(message);
//		builder.setNegativeButton("OK", defaultListener);
//		builder.create().show();
	   Dialog dialog = DialogUtil.getMessageDialog(c, message, defaultListener);
	   dialog.show();
	}
	
	public static boolean isErrorMessage(String message) {
		try {
			JSONObject obj = new org.json.JSONObject(message);
			String  errorMsg = obj.get("errorMessage").toString();
           if(errorMsg != null && errorMsg.trim().length() > 0){
                return false;
           }
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void showErr(Context c,String err){		
//		try {
//			Builder builder = new AlertDialog.Builder(c);
//			builder.setTitle("失败");
//			builder.setMessage(err);
//			builder.setNegativeButton("确认", null);
//			builder.create().show();
//		} catch (Exception e) {
//		}
		Dialog dialog = DialogUtil.getMessageDialog(c, "失败", err, null);
		dialog.show();
	}
	
	public static void showWarn(Context c,String warn) {		
//			Builder builder = new AlertDialog.Builder(c);
//			builder.setTitle("提示");
//			builder.setMessage(warn);
//			builder.setNegativeButton("确认", null);
//			builder.create().show();
		Dialog dialog = DialogUtil.getMessageDialog(c, "提示", warn, null);
		dialog.show();
	}
	
	public static void showSuccess(Context c,String msg){      
//       try {
//           Builder builder = new AlertDialog.Builder(c);
//           builder.setTitle("成功");
//           builder.setMessage(msg);
//           builder.setNegativeButton("确认", null);
//           builder.create().show();
//       } catch (Exception e) {
//       }
		Dialog dialog = DialogUtil.getMessageDialog(c, "成功", msg, null);
		dialog.show();
   }
	
	public static void showSuccessWithOK(Context c,String msg, OnClickListener listener){      
//       try {
//           Builder builder = new AlertDialog.Builder(c);
//           builder.setTitle("提示");
//           builder.setMessage(msg);
//           builder.setNegativeButton("确认",listener);
//           builder.create().show();
//       } catch (Exception e) {
//       }
		Dialog dialog = DialogUtil.getMessageDialog(c, "提示", msg, listener);
		dialog.show();
   }
	
	
	public static int dip2px(Context context, float dpValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  
	} 
	
	public static File getFileFromBytes(byte[] b, String outputFile) {
	      BufferedOutputStream stream = null;
	       File file = null;
	       try {
	      file = new File(outputFile);
	      if(!file.exists()){
	    	  file.createNewFile();
	      }
	           FileOutputStream fstream = new FileOutputStream(file);
	           stream = new BufferedOutputStream(fstream);
	           stream.write(b);
	       } catch (Exception e) {
	           e.printStackTrace();
	      } finally {
	          if (stream != null) {
	               try {
	                  stream.close();
	               } catch (IOException e1) {
	                  e1.printStackTrace();
	              }
	          }
	      }
	       return file;
	   }
	
	
	public static int getImageHight(int screenWidth,int imageW,int imageH){
		int imageHight = 0;
		double proportion = 0.00000;
		proportion = screenWidth/imageW;
		imageHight = (int) Math.rint(imageH * proportion);
		return imageHight;
	}
	public static Dialog dialog(Context context, String message, 
			String okTitle, OnClickListener okListener, 
			String cancelTitle, OnClickListener cancelListener)
	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setTitle("街坊");
//		builder.setPositiveButton(okTitle, okListener == null ? defaultListener : okListener);
//		builder.setNegativeButton(cancelTitle, cancelListener == null ? defaultListener : cancelListener);
//
//		builder.setCancelable(true);
//
//		if (message != null && !"".equals(message.trim()))
//		{
//			builder.setMessage(message);
//		} else
//		{
//			
//		}
//		builder.setMessage(StringUtil.isEmpty(message) ? "错误" : message);
//		AlertDialog dialog = builder.show();
//		return dialog;
		okListener = okListener == null ? defaultListener : okListener;
		cancelListener = cancelListener == null ? defaultListener : cancelListener;
		Dialog dialog = DialogUtil.getQuestionDialog(context, message, okTitle, okListener, cancelTitle, cancelListener);
		dialog.show();
		return dialog;
	}
}
