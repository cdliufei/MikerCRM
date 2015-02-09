package com.mnker.crm.http;

public class URLS {
	public static String baseUrl = "http://121.40.145.169:10023";
	public static String defaultUrl = "http://121.40.145.169:10023";
	// public static final String baseUrl =
	// "http://202.98.157.48:8080/air/social/";
	public static final String login = baseUrl + "/app/sys/login.json";
	public static final String checkVersion = baseUrl + "/app/sys/checkVersion.html";
	public static final String download = baseUrl + "/app/sys/download.html";
	public static final String getPlan = baseUrl + "/app/activity/getPlan.json";
	public static final String getAct = baseUrl + "/app/activity/getAct.json";
	public static final String getContacts = baseUrl + "/app/contact/get.json";
	public static final String getAccount = baseUrl + "/app/account/get.json";
	public static final String uploadAct = baseUrl + "/app/activity/uploadAct.json";
	public static final String uploadSign = baseUrl + "/app/sign/upload.json";
}
