package com.mnker.crm.widget.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;

public class Util {
	public static String formatToMomth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf = new SimpleDateFormat("yyyy-MM");
		String time = null;
		if (d != null) {
			time = sdf.format(d);
		}
		return time;
	}

	public static String getNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		return time;
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = sdf.format(new Date());
		return time;
	}

	public static String getDefaultPlanStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());
		return time + " 08:00";
	}

	public static String getPunchNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String time = sdf.format(new Date());
		return time;
	}

	public static String getPunchTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(new Date());
		return time;
	}

	public static String getPunchTime(String punchtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		try {
			Date date = sdf.parse(punchtime);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf2.format(date);
			return time;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return getNowTime();
	}

	public static String getPlanStartTime(String planStartTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
		try {
			Date date = sdf.parse(planStartTime);
			String time = sdf3.format(date);
			return time;
		} catch (ParseException e) {
			e.printStackTrace();
			Date date;
			try {
				date = sdf2.parse(planStartTime);
				String time = sdf3.format(date);
				return time;
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return "00:00";
	}

	public static int getMonthFromTimeString(String timeString) {
		try {
			String month = timeString.substring(timeString.indexOf("-") + 1, timeString.lastIndexOf("-") - 1);
			if (!TextUtils.isEmpty(month)) {
				return Integer.parseInt(month);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public static int getDayFromTimeString(String timeString) {
		try {
			String day = timeString.substring(timeString.lastIndexOf("-") + 1, timeString.lastIndexOf("-") + 3);
			if (!TextUtils.isEmpty(day)) {
				return Integer.parseInt(day);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public static Date formatTimeStringToDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			try {
				date = sdf2.parse(time);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return date;
	}

	public static String formatTimeString(String time) {
		if (TextUtils.isEmpty(time)) {
			return time;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		try {
			date = sdf.parse(time);
			return sdf2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return time;
		}
	}
}
