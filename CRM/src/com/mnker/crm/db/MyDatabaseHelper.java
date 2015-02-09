package com.mnker.crm.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 2;


	public MyDatabaseHelper(Context context) {
		super(context, "loacaldata", null, VERSION);
	}

	public MyDatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public MyDatabaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public MyDatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table IF NOT EXISTS  " + DataTables.PUNCHCARD+ "("+
				"_id INTEGER PRIMARY KEY," +
				"uuid TEXT," +
				"imei TEXT," +
				"x TEXT," +
				"y TEXT," +
				"ismodify TEXT," +
				"signTime TEXT," +
				"updateTime TEXT" +
				")");
		db.execSQL("create table IF NOT EXISTS  " + DataTables.WEEKPLAN+ "("+
				"_id INTEGER PRIMARY KEY," +
				"id TEXT," +
				"uuid TEXT," +
				"planId INTEGER," +
				"planName TEXT," +
				"startDate TEXT," +
				"endDate TEXT," +
				"updateTime TEXT," +
				"ismodify TEXT," +
				"data1 TEXT," +
				"data2 TEXT," +
				"data3 TEXT" +
				")");
		db.execSQL("create table IF NOT EXISTS  " + DataTables.ACCOUNTINFO+ "("+
				"_id INTEGER PRIMARY KEY," +
				"uuid TEXT," +
				"accountId INTEGER," +
				"accountName TEXT," +
				"stopFlag INTEGER," +
				"updateTime TEXT," +
				"data1 TEXT," +
				"data2 TEXT," +
				"data3 TEXT" +
				")");
		//daytask
				db.execSQL("create table IF NOT EXISTS  " + DataTables.DAYTASK + "("
						+ "_id INTEGER PRIMARY KEY,"
						+ "uuid TEXT," 
						+ "activityId INTEGER,"
//						+ "uuid TEXT," 
						+ "name TEXT," 
						+ "assignedUserId INTEGER," 
						+ "assignedUserName TEXT," 
						+ "deptId INTEGER," 
						+ "deptName TEXT,"
						+ "accountId INTEGER,"
						+ "accountName TEXT,"
						+ "isRemind INTEGER,"
						+ "remindAhead INTEGER,"
						+ "remindTime TEXT,"
						+ "createUserId INTEGER," 
						+ "createUserName TEXT,"
						+ "createTime TEXT,"
						+ "modifyTime TEXT," 
						+ "description TEXT,"
						+ "thnr TEXT,"
						+ "jg TEXT," 
						+ "xybjh TEXT," 
						+ "remark TEXT," 
						+ "zj TEXT," 
						+ "purpose TEXT," 
						+ "planId INTEGER," 
						+ "planName TEXT," 
						+ "closeFlag INTEGER," 
						+ "eventAddress TEXT," 
						+ "finishedTime TEXT," 
						+ "actvtStatus TEXT,"
						+ "status TEXT,"
						+ "planStartTime TEXT,"
						+ "planEndTime TEXT,"
						+ "startTime TEXT,"
						+ "endTime TEXT,"
						+ "reportTime TEXT,"
						+ "typeName TEXT,"
						+ "contactName TEXT,"
						+ "isAppAdd INTEGER,"
						+ "contactId INTEGER,"
						+ "ismodify INTEGER,"
						+ "updateTime TEXT,"
						+ "data1 TEXT,"
						+ "data2 TEXT,"
						+ "data3 TEXT"
						+ ")");
				//contacts
				db.execSQL("create table IF NOT EXISTS  " + DataTables.CONTACTSINFO + "("
						+ "id INTEGER PRIMARY KEY," 
						+ "uuid TEXT," 
						+ "contactId INTEGER,"
						+ "contactName TEXT," 
						+ "contactType TEXT," 
						+ "stopFlag TEXT,"
						+ "genderId TEXT,"
						+ "genderName TEXT," 
						+ "maritalName TEXT,"
						+ "salutationName TEXT," 
						+ "birthDate TEXT,"
						+ "deptName TEXT,"
						+ "ownerName TEXT," 
						+ "ownerId TEXT," 
						+ "position TEXT," 
						+ "accountName TEXT," 
						+ "accountId TEXT," 
						+ "reportTo TEXT," 
						+ "mobile TEXT," 
						+ "officePhone TEXT," 
						+ "homePhone TEXT," 
						+ "email TEXT," 
						+ "mailingZipCode TEXT,"
						+ "mailingAddress TEXT,"
						+ "description TEXT,"
						+ "zc TEXT,"
						+ "sfcgzjkcy TEXT,"
						+ "zcmm TEXT,"
						+ "qq TEXT,"
						+ "sr TEXT,"
						+ "byzyy TEXT,"
						+ "bysjy TEXT,"
						+ "bysje TEXT,"
						+ "bysj3 TEXT,"
						+ "byzy3 TEXT,"
						+ "byys1 TEXT,"
						+ "byys2 TEXT,"
						+ "byys3 TEXT,"
						+ "ssmb TEXT,"
						+ "shzw TEXT,"
						+ "shzwms TEXT,"
						+ "mz TEXT,"
						+ "rzsj TEXT,"
						+ "pexm TEXT,"
						+ "pesj TEXT,"
						+ "pegzdw TEXT,"
						+ "posr TEXT,"
						+ "jtqtzycyqk TEXT,"
						+ "jtqk TEXT,"
						+ "zycg TEXT,"
						+ "fax TEXT,"
						+ "gzll TEXT,"
						+ "xgtz TEXT,"
						+ "jkqk TEXT,"
						+ "sh TEXT,"
						+ "zyxxah TEXT,"
						+ "xhdds TEXT,"
						+ "zjxy TEXT,"
						+ "cykw TEXT,"
						+ "khjsfa TEXT,"
						+ "khwhfa TEXT,"
						+ "fzqy TEXT,"
						+ "fzcplb TEXT,"
						+ "zyz TEXT,"
						+ "ks TEXT,"
						+ "sfjfmkzz TEXT,"
						+ "zw TEXT,"
						+ "sf TEXT,"
						+ "ssbm TEXT,"
						+ "lxrfl TEXT,"
						+ "updateTime TEXT,"
						+ "data1 TEXT,"
						+ "data2 TEXT,"
						+ "data3 TEXT"
						+ ")");
			
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     //备份数据
	}

}
