package com.mnker.crm.db;

public class DataTables {
	public static final String WEEKPLAN = "week_plan";
	public static final String DAYTASK = "day_task";
	public static final String PUNCHCARD = "punch_card";
	public static final String CONTACTSINFO = "contacts";
	public static final String ACCOUNTINFO = "accounts";

	// 打卡
	static class PunchCard {
		// 主键
		public static final String uuid = "uuid";
		public static final String imei = "imei";
		public static final String signTime = "signTime";
		public static final String x = "x";
		public static final String y = "y";
		public static final String ismodify = "ismodify";
		public static final String updateTime = "updateTime";

		// 扩展字段
		public static final String data1 = "data1";
		public static final String data2 = "data2";
		public static final String data3 = "data3";
	}

	// 周计划
	static class WeekPlan {
		// 主键
		public static final String id = "id";
		public static final String uuid = "uuid";
		public static final String planId = "planId";
		public static final String planName = "planName";
		public static final String startDate = "startDate";
		public static final String endDate = "endDate";
		public static final String updateTime = "updateTime";
		public static final String ismodify = "ismodify";

		// 扩展字段
		public static final String data1 = "data1";
		public static final String data2 = "data2";
		public static final String data3 = "data3";
	}

	// 周计划
	static class AccountInfo {
		// 主键
		public static final String uuid = "uuid";
		public static final String accountId = "accountId";
		public static final String accountName = "accountName";
		public static final String stopFlag = "stopFlag";
		public static final String updateTime = "updateTime";

		// 扩展字段
		public static final String data1 = "data1";
		public static final String data2 = "data2";
		public static final String data3 = "data3";
	}

	// 每天工作流程
	static class DayTask {
		// 主键
		public static final String id = "id";
		public static final String activityId = "activityId";
		public static final String uuid = "uuid";
		public static final String name = "name";
		public static final String assignedUserId = "assignedUserId";
		public static final String assignedUserName = "assignedUserName";

		public static final String deptId = "deptId";
		public static final String deptName = "deptName";
		public static final String accountId = "accountId";
		public static final String accountName = "accountName";
		public static final String isRemind = "isRemind";
		public static final String remindAhead = "remindAhead";
		public static final String remindTime = "remindTime";

		public static final String createUserId = "createUserId";
		public static final String createUserName = "createUserName";
		public static final String createTime = "createTime";
		public static final String modifyTime = "modifyTime";
		
		
		public static final String description = "description";
		public static final String thnr = "thnr";
		public static final String jg = "jg";
		public static final String xybjh = "xybjh";
		
		public static final String remark = "remark";
		public static final String zj = "zj";
		public static final String purpose = "purpose";
		public static final String planId = "planId";
		public static final String planName = "planName";
		
		public static final String closeFlag = "closeFlag";
		public static final String eventAddress = "eventAddress";
		public static final String finishedTime = "finishedTime";
		public static final String actvtStatus = "actvtStatus";
		
		public static final String status = "status";
		public static final String planStartTime = "planStartTime";
		public static final String planEndTime = "planEndTime";
		
		public static final String startTime = "startTime";
		public static final String endTime = "endTime";
		public static final String reportTime = "reportTime";
		public static final String typeName = "typeName";
		public static final String contactName = "contactName";
		public static final String isAppAdd = "isAppAdd";
		public static final String contactId = "contactId";

		// 用户信息
		public static final String ismodify = "ismodify";
		public static final String updateTime = "updateTime";

		// 扩展字段
		public static final String FIELD_CARD_DATA1 = "data1";
		public static final String FIELD_CARD_DATA2 = "data2";
		public static final String FIELD_CARD_DATA3 = "data3";
	}

	// 联系人
	static class ContactsInfo {
		public static final String uuid = "uuid";
		public static final String contactId = "contactId";
		public static final String contactName = "contactName";
		public static final String contactType = "contactType";
		public static final String stopFlag = "stopFlag";
		public static final String genderId = "genderId";
		public static final String genderName = "genderName";
		public static final String maritalName = "maritalName";
		public static final String salutationName = "salutationName";
		public static final String birthDate = "birthDate";
		public static final String deptName = "deptName";
		public static final String ownerName = "ownerName";
		public static final String ownerId = "ownerId";
		public static final String position = "position";
		public static final String accountName = "accountName";
		public static final String accountId = "accountId";
		public static final String reportTo = "reportTo";
		public static final String mobile = "mobile";
		public static final String officePhone = "officePhone";
		public static final String homePhone = "homePhone";
		public static final String fax = "fax";
		public static final String email = "email";
		public static final String mailingZipCode = "mailingZipCode";
		public static final String mailingAddress = "mailingAddress";
		public static final String description = "description";
		public static final String zc = "zc";
		public static final String sfcgzjkcy = "sfcgzjkcy";
		public static final String zcmm = "zcmm";
		public static final String qq = "qq";
		public static final String sr = "sr";
		public static final String byzyy = "byzyy";
		public static final String bysjy = "bysjy";
		public static final String bysje = "bysje";
		public static final String byzy3 = "byzy3";
		public static final String bysj3 = "bysj3";
		public static final String byys1 = "byys1";
		public static final String byys2 = "byys2";
		public static final String byys3 = "byys3";
		public static final String shzw = "shzw";
		public static final String shzwms = "shzwms";
		public static final String mz = "mz";
		public static final String rzsj = "rzsj";
		public static final String pexm = "pexm";
		public static final String pesj = "pesj";
		public static final String pegzdw = "pegzdw";
		public static final String posr = "posr";
		public static final String jtqtzycyqk = "jtqtzycyqk";
		public static final String jtqk = "jtqk";
		public static final String zycg = "zycg";
		public static final String gzll = "gzll";
		public static final String xgtz = "xgtz";
		public static final String jkqk = "jkqk";
		public static final String sh = "sh";
		public static final String zyxxah = "zyxxah";
		public static final String xhdds = "xhdds";
		public static final String zjxy = "zjxy";
		public static final String cykw = "cykw";
		public static final String khjsfa = "khjsfa";
		public static final String khwhfa = "khwhfa";
		public static final String fzqy = "fzqy";
		public static final String fzcplb = "fzcplb";
		public static final String zyz = "zyz";
		public static final String ks = "ks";
		public static final String sfjfmkzz = "sfjfmkzz";
		public static final String zw = "zw";
		public static final String sf = "sf";
		public static final String ssbm = "ssmb";
		public static final String lxrfl = "lxrfl";
		public static final String updateTime = "updateTime";

		// 扩展字段
		public static final String FIELD_CARD_DATA1 = "data1";
		public static final String FIELD_CARD_DATA2 = "data2";
		public static final String FIELD_CARD_DATA3 = "data3";
	}

}
