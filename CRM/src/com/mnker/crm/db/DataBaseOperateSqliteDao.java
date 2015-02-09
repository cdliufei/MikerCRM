package com.mnker.crm.db;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mnker.crm.db.DataTables.AccountInfo;
import com.mnker.crm.db.DataTables.ContactsInfo;
import com.mnker.crm.db.DataTables.DayTask;
import com.mnker.crm.db.DataTables.WeekPlan;
import com.mnker.crm.http.json.PunchCard;
import com.mnker.crm.http.json.ReAccountData;
import com.mnker.crm.http.json.ReActypeData;
import com.mnker.crm.http.json.ReContactsData;
import com.mnker.crm.http.json.RePlanAct;
import com.mnker.crm.http.json.RePlanActData;
import com.mnker.crm.http.json.RePlanData;
import com.mnker.crm.widget.util.Util;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Contacts.Intents.Insert;
import android.text.TextUtils;
import android.util.Log;

public class DataBaseOperateSqliteDao {

	private MyDatabaseHelper helper;
	private SQLiteDatabase db;
	private static DataBaseOperateSqliteDao mInstanceDao;

	private DataBaseOperateSqliteDao(Context context) {
		helper = new MyDatabaseHelper(context.getApplicationContext());
	}

	public static DataBaseOperateSqliteDao getInstance(Context context) {
		if (mInstanceDao == null) {
			mInstanceDao = new DataBaseOperateSqliteDao(context);
		}
		return mInstanceDao;
	}

	/******************************************** 通讯录 *******************************************************************/
	public void clearFeedTable(String tablename) {
		String sql = "DELETE FROM " + tablename + ";";
		db.execSQL(sql);
	}

	public void clearAllTables() {
		try {

			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			String delPunchSql = "DELETE FROM " + DataTables.PUNCHCARD + ";";
			db.execSQL(delPunchSql);
			String delContactsSql = "DELETE FROM " + DataTables.CONTACTSINFO + ";";
			db.execSQL(delContactsSql);
			String delAccountsSql = "DELETE FROM " + DataTables.ACCOUNTINFO + ";";
			db.execSQL(delAccountsSql);
			String delDaytasksSql = "DELETE FROM " + DataTables.DAYTASK + ";";
			db.execSQL(delDaytasksSql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public void insertContactData() {
		try {

			db = helper.getWritableDatabase();
			db.beginTransaction();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public synchronized void insertPunchInfo(PunchCard punchCard) {
		try {

			System.out.println(" insertPunchInfo " + punchCard.uuid + "  " + punchCard.signTime);
			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();

			if (punchCard != null) {
				ContentValues values = new ContentValues();
				values.put("uuid", punchCard.uuid);
				values.put("imei", punchCard.imei);
				values.put("signTime", punchCard.signTime);
				values.put("x", punchCard.x);
				values.put("y", punchCard.y);
				values.put("ismodify", "1");
				// int index = db.update(DataTables.PUNCHCARD, values,
				// "uuid = ? and signTime = ?", new String[] { punchCard.uuid,
				// punchCard.signTime });
				// if (index == -1) {
				db.insertOrThrow(DataTables.PUNCHCARD, null, values);
				// }
			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	public synchronized List<PunchCard> getPunchInfo(String uuid, String imei) {
		List<PunchCard> list = new ArrayList<PunchCard>();
		Cursor cursor = null;
		try {
			Log.d("zaokun", "getPunchInfo uuid==" + uuid + " imei=" + imei);
			// if (TextUtils.isEmpty(uuid)) {
			// return list;
			// }
			if (db == null || !db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			// db.beginTransaction();

			// String selection = "uuid = ?";
			// String[] selectionArgs = new String[] { uuid };
			String selection = null;
			String[] selectionArgs = null;
			String orderBy = "signTime DESC";
			// String orderBy = null;
			cursor = db.query(DataTables.PUNCHCARD, null, selection, selectionArgs, null, null, orderBy);
			Log.d("zaokun", " getPunchInfo uuid = " + uuid + "  " + cursor.getCount());
			int MaxTotal = 5;
			int i = 0;
			// if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				PunchCard punchCard = new PunchCard();
				punchCard.uuid = uuid;
				punchCard.imei = cursor.getString(cursor.getColumnIndex(DataTables.PunchCard.imei));
				punchCard.signTime = cursor.getString(cursor.getColumnIndex(DataTables.PunchCard.signTime));
				list.add(punchCard);
				i++;
				if (i >= MaxTotal) {
					break;
				}
			}
			// }
			// db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				// db.endTransaction();
				db.close();
			}
		}
		return list;
	}

	public synchronized List<PunchCard> queryOfflinePunchInfo() {
		List<PunchCard> list = new ArrayList<PunchCard>();
		Cursor cursor = null;
		try {
			if (db == null || !db.isOpen()) {
				db = helper.getReadableDatabase();
			}

			String selection = "ismodify = ?";
			String[] selectionArgs = new String[] { "1" };
			String orderBy = null;
			// String orderBy = null;
			cursor = db.query(DataTables.PUNCHCARD, null, selection, selectionArgs, null, null, orderBy);
			Log.d("zaokun", " getPunchInfo uuid  " + cursor.getCount());
			// if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				PunchCard punchCard = new PunchCard();
				punchCard.uuid = cursor.getString(cursor.getColumnIndex(DataTables.PunchCard.uuid));
				punchCard.imei = cursor.getString(cursor.getColumnIndex(DataTables.PunchCard.imei));
				punchCard.signTime = cursor.getString(cursor.getColumnIndex(DataTables.PunchCard.signTime));
				punchCard.x = cursor.getString(cursor.getColumnIndex(DataTables.PunchCard.x));
				punchCard.y = cursor.getString(cursor.getColumnIndex(DataTables.PunchCard.y));
				list.add(punchCard);
			}
			// }
			// db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				// db.endTransaction();
				db.close();
			}
		}
		return list;
	}

	public synchronized void updateOfflinePunchInfoSuccess(PunchCard punchCard) {
		try {

			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();

			if (punchCard != null) {
				ContentValues values = new ContentValues();
				values.put("uuid", punchCard.uuid);
				values.put("imei", punchCard.imei);
				values.put("signTime", punchCard.signTime);
				values.put("x", punchCard.x);
				values.put("y", punchCard.y);
				values.put("ismodify", "0");
				db.update(DataTables.PUNCHCARD, values, "signTime = ?", new String[] { punchCard.signTime });
			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	public synchronized void insertWeekPlanData(List<RePlanData> data, String updateTime) {
		try {
			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();

			if (data == null || data.size() == 0) {
				return;
			}
			for (RePlanData rePlanData : data) {
				ContentValues values = new ContentValues();
				values.put(WeekPlan.uuid, rePlanData.uuid);
				values.put(WeekPlan.planId, rePlanData.planId);
				values.put(WeekPlan.planName, rePlanData.planName);
				values.put(WeekPlan.startDate, rePlanData.startDate);
				values.put(WeekPlan.endDate, rePlanData.endDate);
				values.put(WeekPlan.updateTime, updateTime);
				values.put(WeekPlan.ismodify, "0");
				int index = -1;
				// try {
				// index = db.update(DataTables.WEEKPLAN, values, "uuid = ?",
				// new String[] { rePlanData.uuid });
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				if (index == -1) {
					try {
						db.insert(DataTables.WEEKPLAN, null, values);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	public synchronized List<RePlanData> getWeekPlanInfo(int planId, String uuid, String imei) {
		List<RePlanData> list = new ArrayList<RePlanData>();
		Cursor cursor = null;
		try {
			Log.d("zaokun", "getWeekPlanInfo uuid==" + uuid + " imei=" + imei);
			if (TextUtils.isEmpty(uuid)) {
				return list;
			}
			if (db == null || !db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			// db.beginTransaction();

			String selection = "uuid = ?";
			String[] selectionArgs = new String[] { uuid };
			String orderBy = "startDate DESC";
			// String orderBy = null;
			cursor = db.query(DataTables.WEEKPLAN, null, null, null, null, null, orderBy);
			Log.d("zaokun", " getWeekPlanInfo uuid = " + uuid + "  " + cursor.getCount());
			// if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				RePlanData planData = new RePlanData();
				planData.uuid = cursor.getString(cursor.getColumnIndex(DataTables.WeekPlan.uuid));
				planData.planId = cursor.getInt(cursor.getColumnIndex(DataTables.WeekPlan.planId));
				planData.planName = cursor.getString(cursor.getColumnIndex(DataTables.WeekPlan.planName));
				planData.startDate = cursor.getString(cursor.getColumnIndex(DataTables.WeekPlan.startDate));
				planData.endDate = cursor.getString(cursor.getColumnIndex(DataTables.WeekPlan.endDate));
				list.add(planData);
			}
			// }
			// db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				// db.endTransaction();
				db.close();
			}
		}
		return list;
	}

	public synchronized void insertContactsData(List<ReContactsData> data, String updateTime) {
		try {
			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();

			if (data == null || data.size() == 0) {
				return;
			}
			for (ReContactsData reContactsData : data) {
				ContentValues values = new ContentValues();
				values.put(ContactsInfo.uuid, reContactsData.uuid);
				values.put(ContactsInfo.accountId, reContactsData.accountId);
				values.put(ContactsInfo.contactName, reContactsData.contactName);
				values.put(ContactsInfo.accountName, reContactsData.accountName);
				values.put(ContactsInfo.mobile, reContactsData.mobile);

				values.put(ContactsInfo.contactId, reContactsData.contactId);
				values.put(ContactsInfo.contactType, reContactsData.contactType);
				values.put(ContactsInfo.stopFlag, reContactsData.stopFlag);
				values.put(ContactsInfo.genderId, reContactsData.genderId);
				values.put(ContactsInfo.genderName, reContactsData.genderName);
				values.put(ContactsInfo.maritalName, reContactsData.maritalName);
				values.put(ContactsInfo.salutationName, reContactsData.salutationName);
				values.put(ContactsInfo.birthDate, reContactsData.birthDate);
				values.put(ContactsInfo.ownerName, reContactsData.ownerName);
				values.put(ContactsInfo.deptName, reContactsData.deptName);
				values.put(ContactsInfo.ownerName, reContactsData.ownerName);
				values.put(ContactsInfo.ownerId, reContactsData.ownerId);
				values.put(ContactsInfo.position, reContactsData.position);
				values.put(ContactsInfo.accountName, reContactsData.accountName);
				values.put(ContactsInfo.accountId, reContactsData.accountId);
				values.put(ContactsInfo.reportTo, reContactsData.reportTo);
				values.put(ContactsInfo.officePhone, reContactsData.officePhone);
				values.put(ContactsInfo.homePhone, reContactsData.homePhone);
				values.put(ContactsInfo.fax, reContactsData.fax);
				values.put(ContactsInfo.email, reContactsData.email);
				values.put(ContactsInfo.mailingZipCode, reContactsData.mailingZipCode);
				values.put(ContactsInfo.mailingAddress, reContactsData.mailingAddress);
				values.put(ContactsInfo.description, reContactsData.description);
				values.put(ContactsInfo.zc, reContactsData.zc);
				values.put(ContactsInfo.sfcgzjkcy, reContactsData.sfcgzjkcy);
				values.put(ContactsInfo.zcmm, reContactsData.zcmm);
				values.put(ContactsInfo.qq, reContactsData.qq);
				values.put(ContactsInfo.sr, reContactsData.sr);
				values.put(ContactsInfo.byzyy, reContactsData.byzyy);
				values.put(ContactsInfo.bysjy, reContactsData.bysjy);
				values.put(ContactsInfo.bysje, reContactsData.bysje);
				values.put(ContactsInfo.byzy3, reContactsData.byzy3);
				values.put(ContactsInfo.bysj3, reContactsData.bysj3);
				values.put(ContactsInfo.byys1, reContactsData.byys1);
				values.put(ContactsInfo.byys2, reContactsData.byys2);
				values.put(ContactsInfo.byys3, reContactsData.byys3);
				values.put(ContactsInfo.shzw, reContactsData.shzw);
				values.put(ContactsInfo.shzwms, reContactsData.shzwms);
				values.put(ContactsInfo.mz, reContactsData.mz);
				values.put(ContactsInfo.rzsj, reContactsData.rzsj);
				values.put(ContactsInfo.pesj, reContactsData.pesj);
				values.put(ContactsInfo.pegzdw, reContactsData.pegzdw);

				values.put(ContactsInfo.posr, reContactsData.posr);
				values.put(ContactsInfo.jtqtzycyqk, reContactsData.jtqtzycyqk);
				values.put(ContactsInfo.jtqk, reContactsData.jtqk);
				values.put(ContactsInfo.zycg, reContactsData.zycg);
				values.put(ContactsInfo.gzll, reContactsData.gzll);
				values.put(ContactsInfo.xgtz, reContactsData.xgtz);
				values.put(ContactsInfo.jkqk, reContactsData.jkqk);
				values.put(ContactsInfo.sh, reContactsData.sh);
				values.put(ContactsInfo.zyxxah, reContactsData.zyxxah);
				values.put(ContactsInfo.xhdds, reContactsData.xhdds);
				values.put(ContactsInfo.zjxy, reContactsData.zjxy);
				values.put(ContactsInfo.cykw, reContactsData.cykw);
				values.put(ContactsInfo.khjsfa, reContactsData.khjsfa);
				values.put(ContactsInfo.fzqy, reContactsData.fzqy);
				values.put(ContactsInfo.khwhfa, reContactsData.khwhfa);
				values.put(ContactsInfo.fzqy, reContactsData.fzqy);
				values.put(ContactsInfo.fzcplb, reContactsData.fzcplb);
				values.put(ContactsInfo.zyz, reContactsData.zyz);
				values.put(ContactsInfo.ks, reContactsData.ks);
				values.put(ContactsInfo.sfjfmkzz, reContactsData.sfjfmkzz);
				values.put(ContactsInfo.zw, reContactsData.zw);
				values.put(ContactsInfo.sf, reContactsData.sf);
				values.put(ContactsInfo.ssbm, reContactsData.ssbm);
				values.put(ContactsInfo.lxrfl, reContactsData.lxrfl);

				values.put(ContactsInfo.updateTime, updateTime);
				int index = -1;
				// try {
				// index = db.update(DataTables.CONTACTSINFO, values,
				// "uuid = ?", new String[] { reContactsData.uuid });
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				if (index == -1) {
					try {
						db.insert(DataTables.CONTACTSINFO, null, values);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * @Author huxiaodong
	 * @Title: getContactsInfo
	 * @Description: accountId为空，查询所有联系人，负责查询单个联系人信息
	 * @param accountId
	 * @return 参数
	 * @return List<ReContactsData> 返回类型
	 * @Date 2015 - 下午5:13:46
	 * @throws
	 */
	public synchronized List<ReContactsData> getContactsInfo(String accountId) {
		List<ReContactsData> list = new ArrayList<ReContactsData>();
		Cursor cursor = null;
		try {

			if (db == null || !db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			// db.beginTransaction();

			// String orderBy = "contactName ASC";
			String orderBy = null;
			String selection = null;
			String[] selectionArgs = null;

			if (!TextUtils.isEmpty(accountId)) {
				selection = "accountId = ?";
				selectionArgs = new String[] { accountId };
			}

			cursor = db.query(DataTables.CONTACTSINFO, null, selection, selectionArgs, null, null, orderBy);
			Log.d("zaokun", " getContactsInfo cur size = " + cursor.getCount());
			// if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				ReContactsData contact = new ReContactsData();
				contact.uuid = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.uuid));
				contact.mobile = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.mobile));
				contact.accountId = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.accountId));
				contact.accountName = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.accountName));
				// contact.contactId =
				// cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.contactId));
				contact.contactName = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.contactName));
				contact.contactId = cursor.getInt(cursor.getColumnIndex(DataTables.ContactsInfo.contactId));
				contact.contactType = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.contactType));
				contact.stopFlag = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.stopFlag));
				contact.genderId = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.genderId));
				contact.genderName = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.genderName));
				contact.maritalName = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.maritalName));

				contact.salutationName = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.salutationName));
				contact.birthDate = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.birthDate));
				contact.deptName = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.deptName));
				contact.ownerName = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.ownerName));

				contact.ownerId = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.ownerId));
				contact.position = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.position));
				contact.reportTo = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.reportTo));
				contact.officePhone = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.officePhone));
				contact.homePhone = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.homePhone));
				contact.fax = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.fax));
				contact.email = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.email));
				contact.mailingZipCode = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.mailingZipCode));

				contact.mailingAddress = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.mailingAddress));
				contact.description = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.description));
				contact.zc = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.zc));

				contact.sfcgzjkcy = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.sfcgzjkcy));
				contact.zcmm = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.zcmm));
				contact.qq = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.qq));
				contact.sr = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.sr));
				contact.byzyy = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.byzyy));
				contact.bysjy = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.bysjy));
				contact.bysje = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.bysje));
				contact.byzy3 = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.byzy3));
				contact.bysj3 = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.bysj3));
				contact.byys1 = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.byys1));
				contact.byys2 = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.byys2));
				contact.byys3 = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.byys3));
				contact.shzw = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.shzw));
				contact.shzwms = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.shzwms));
				contact.mz = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.mz));
				contact.rzsj = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.rzsj));
				contact.pexm = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.pexm));
				contact.pesj = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.pesj));
				contact.pegzdw = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.pegzdw));
				contact.posr = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.posr));
				contact.jtqtzycyqk = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.jtqtzycyqk));
				contact.jtqk = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.jtqk));
				contact.zycg = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.zycg));
				contact.gzll = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.gzll));
				contact.xgtz = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.xgtz));
				contact.jkqk = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.jkqk));
				contact.sh = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.sh));
				contact.zyxxah = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.zyxxah));
				contact.xhdds = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.xhdds));
				contact.zjxy = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.zjxy));
				contact.cykw = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.cykw));
				contact.khjsfa = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.khjsfa));
				contact.khwhfa = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.khwhfa));
				contact.fzqy = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.fzqy));
				contact.fzcplb = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.fzcplb));
				contact.zyz = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.zyz));
				contact.ks = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.ks));
				contact.sfjfmkzz = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.sfjfmkzz));
				contact.zw = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.zw));
				contact.sf = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.sf));

				contact.ssbm = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.ssbm));
				contact.lxrfl = cursor.getString(cursor.getColumnIndex(DataTables.ContactsInfo.lxrfl));

				contact.setFirstChar();
				list.add(contact);
			}
			// }
			// db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				// db.endTransaction();
				db.close();
			}
		}
		return list;
	}

	public synchronized void insertActPlanData(List<RePlanActData> data, String updateTime, String isAddNew) {
		try {
			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();

			if (data == null || data.size() == 0) {
				return;
			}
			for (RePlanActData rePlanActData : data) {
				ContentValues values = new ContentValues();
				values.put(DayTask.uuid, rePlanActData.uuid);
				values.put(DayTask.activityId, rePlanActData.activityId);
				values.put(DayTask.name, rePlanActData.name);
				values.put(DayTask.assignedUserId, rePlanActData.assignedUserId);
				values.put(DayTask.assignedUserName, rePlanActData.assignedUserName);
				values.put(DayTask.deptId, rePlanActData.deptId);
				values.put(DayTask.deptName, rePlanActData.deptName);
				values.put(DayTask.accountId, rePlanActData.accountId);
				values.put(DayTask.accountName, rePlanActData.accountName);
				values.put(DayTask.isRemind, rePlanActData.isRemind);
				values.put(DayTask.remindAhead, rePlanActData.remindAhead);
				values.put(DayTask.remindTime, rePlanActData.remindTime);
				values.put(DayTask.createUserId, rePlanActData.createUserId);
				values.put(DayTask.createUserName, rePlanActData.createUserName);
				values.put(DayTask.createTime, rePlanActData.createTime);
				values.put(DayTask.modifyTime, rePlanActData.modifyTime);
				values.put(DayTask.description, rePlanActData.description);
				values.put(DayTask.thnr, rePlanActData.thnr);
				values.put(DayTask.jg, rePlanActData.jg);
				values.put(DayTask.jg, rePlanActData.jg);
				values.put(DayTask.remark, rePlanActData.remark);
				values.put(DayTask.zj, rePlanActData.zj);
				values.put(DayTask.purpose, rePlanActData.purpose);
				values.put(DayTask.planId, rePlanActData.planId);
				values.put(DayTask.planName, rePlanActData.planName);
				values.put(DayTask.closeFlag, rePlanActData.closeFlag);
				values.put(DayTask.eventAddress, rePlanActData.eventAddress);
				values.put(DayTask.finishedTime, rePlanActData.finishedTime);
				values.put(DayTask.actvtStatus, rePlanActData.actvtStatus);
				values.put(DayTask.status, rePlanActData.status);
				values.put(DayTask.planStartTime, rePlanActData.planStartTime);
				values.put(DayTask.planEndTime, rePlanActData.planEndTime);
				values.put(DayTask.startTime, rePlanActData.startTime);
				values.put(DayTask.endTime, rePlanActData.endTime);
				values.put(DayTask.reportTime, rePlanActData.reportTime);
				values.put(DayTask.typeName, rePlanActData.typeName);
				values.put(DayTask.contactName, rePlanActData.contactName);
				values.put(DayTask.isAppAdd, rePlanActData.isAppAdd);
				values.put(DayTask.contactId, rePlanActData.contactId);
				values.put(DayTask.ismodify, isAddNew);
				values.put(DayTask.updateTime, updateTime);
				int index = -1;
				// try {
				// index = db.update(DataTables.DAYTASK, values, "uuid = ?", new
				// String[] { rePlanActData.uuid });
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				if (index == -1) {
					try {
						db.insert(DataTables.DAYTASK, null, values);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * @Author huxiaodong
	 * @Title: getActPlanDataInfo
	 * @Description: 查询一个月 oneMonthActPlans不能为空 格式 201502 查询每天 用activityId
	 *               oneMonthActPlans必须为空
	 * @param uuid
	 * @param activityId
	 * @param oneMonthActPlans
	 * @return 参数
	 * @return List<RePlanActData> 返回类型
	 * @Date 2015 - 下午5:08:53
	 * @throws
	 */
	public synchronized List<RePlanActData> getActPlanDataInfo(String uuid, int activityId, String oneMonthActPlans, String queryKey) {
		List<RePlanActData> list = new ArrayList<RePlanActData>();
		Cursor cursor = null;
		try {
			if (TextUtils.isEmpty(uuid)) {
				return list;
			}

			if (db == null || !db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			// db.beginTransaction();

			String selection = " uuid = ?";
			String[] selectionArgs = new String[] { uuid };
			// String orderBy = "contactName ASC";
			String orderBy = null;

			if (!TextUtils.isEmpty(oneMonthActPlans)) {
				selection = " planStartTime like ?";
				selectionArgs = new String[] { oneMonthActPlans + "%" };
				orderBy = "planStartTime desc";
				// selection = null;
				// selectionArgs = null;
				// orderBy = null;
			} else if (activityId == 0) {
				if (queryKey != null) {
					selection = "planName like ? or deptName like ? or name like ? or accountName like ? or thnr like ? or planStartTime like ?";
					selectionArgs = new String[] { "%" + queryKey + "%", "%" + queryKey + "%", "%" + queryKey + "%", "%" + queryKey + "%",
							"%" + queryKey + "%", "%" + queryKey + "%" };
					orderBy = "planStartTime desc";
				} else {
					selection = null;
					selectionArgs = null;
					orderBy = "planStartTime desc";
				}
			}

			cursor = db.query(DataTables.DAYTASK, null, selection, selectionArgs, null, null, orderBy);
			Log.d("zaokun", " getActPlanDataInfo cur size = " + cursor.getCount());
			// if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				RePlanActData rePlanActData = new RePlanActData();
				rePlanActData.uuid = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.uuid));
				rePlanActData.activityId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.activityId));
				rePlanActData.name = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.name));
				rePlanActData.assignedUserId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.assignedUserId));
				rePlanActData.assignedUserName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.assignedUserName));
				rePlanActData.deptId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.deptId));
				rePlanActData.deptName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.deptName));
				rePlanActData.accountId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.accountId));
				rePlanActData.accountName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.accountName));
				rePlanActData.isRemind = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.isRemind));
				rePlanActData.remindAhead = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.remindAhead));
				rePlanActData.remindTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.remindTime));
				rePlanActData.createUserId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.createUserId));
				rePlanActData.createUserName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.createUserName));
				rePlanActData.createTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.createTime));
				rePlanActData.modifyTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.modifyTime));
				rePlanActData.description = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.description));
				rePlanActData.thnr = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.thnr));
				rePlanActData.jg = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.jg));
				rePlanActData.jg = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.jg));
				rePlanActData.remark = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.remark));
				rePlanActData.zj = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.zj));
				rePlanActData.purpose = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.purpose));
				rePlanActData.planId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.planId));
				rePlanActData.planName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.planName));
				rePlanActData.closeFlag = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.closeFlag));
				rePlanActData.eventAddress = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.eventAddress));
				rePlanActData.finishedTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.finishedTime));
				rePlanActData.actvtStatus = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.actvtStatus));
				rePlanActData.status = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.status));
				rePlanActData.planStartTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.planStartTime));
				rePlanActData.planEndTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.planEndTime));
				rePlanActData.startTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.startTime));
				rePlanActData.endTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.endTime));
				rePlanActData.reportTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.reportTime));
				rePlanActData.typeName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.typeName));
				rePlanActData.contactName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.contactName));
				rePlanActData.isAppAdd = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.isAppAdd));
				rePlanActData.contactId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.contactId));
				list.add(rePlanActData);
			}
			// }
			// db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				// db.endTransaction();
				db.close();
			}
		}
		return list;
	}

	public synchronized void insertAccountsData(List<ReAccountData> data, String updateTime) {
		try {
			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();

			if (data == null || data.size() == 0) {
				return;
			}
			for (ReAccountData reAccountData : data) {
				ContentValues values = new ContentValues();
				values.put(AccountInfo.uuid, reAccountData.uuid);
				values.put(AccountInfo.accountId, reAccountData.accountId);
				values.put(AccountInfo.accountName, reAccountData.accountName);
				values.put(AccountInfo.stopFlag, reAccountData.stopFlag);
				values.put(AccountInfo.updateTime, updateTime);
				int index = -1;
				// try {
				// index = db.update(DataTables.ACCOUNTINFO, values, "uuid = ?",
				// new String[] { reAccountData.uuid });
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				if (index == -1) {
					try {
						db.insert(DataTables.ACCOUNTINFO, null, values);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}

	}

	public synchronized List<ReAccountData> getAccountsDataInfo() {
		List<ReAccountData> list = new ArrayList<ReAccountData>();
		Cursor cursor = null;
		try {
			if (db == null || !db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			// db.beginTransaction();

			String orderBy = "contactName ASC";
			// String orderBy = null;
			cursor = db.query(DataTables.ACCOUNTINFO, null, null, null, null, null, orderBy);
			Log.d("zaokun", " getContactsInfo cur size = " + cursor.getCount());
			// if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				ReAccountData account = new ReAccountData();
				account.uuid = cursor.getString(cursor.getColumnIndex(DataTables.AccountInfo.uuid));
				account.accountId = cursor.getInt(cursor.getColumnIndex(DataTables.AccountInfo.accountId));
				account.accountName = cursor.getString(cursor.getColumnIndex(DataTables.AccountInfo.accountName));
				account.stopFlag = cursor.getInt(cursor.getColumnIndex(DataTables.ContactsInfo.stopFlag));
				list.add(account);
			}
			// }
			// db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				// db.endTransaction();
				db.close();
			}
		}
		return list;
	}

	public synchronized List<RePlanActData> queryOfflineActPlanInfo() {
		List<RePlanActData> list = new ArrayList<RePlanActData>();
		Cursor cursor = null;
		try {
			if (db == null || !db.isOpen()) {
				db = helper.getReadableDatabase();
			}

			String selection = "ismodify = ?";
			String[] selectionArgs = new String[] { "1" };
			String orderBy = null;
			// String orderBy = null;
			cursor = db.query(DataTables.DAYTASK, null, selection, selectionArgs, null, null, orderBy);
			Log.d("zaokun", " queryOfflineActPlanInfo uuid  " + cursor.getCount());
			// if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				RePlanActData rePlanActData = new RePlanActData();
				rePlanActData.uuid = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.uuid));
				rePlanActData.activityId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.activityId));
				rePlanActData.name = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.name));
				rePlanActData.assignedUserId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.assignedUserId));
				rePlanActData.assignedUserName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.assignedUserName));
				rePlanActData.deptId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.deptId));
				rePlanActData.deptName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.deptName));
				rePlanActData.accountId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.accountId));
				rePlanActData.accountName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.accountName));
				rePlanActData.isRemind = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.isRemind));
				rePlanActData.remindAhead = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.remindAhead));
				rePlanActData.remindTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.remindTime));
				rePlanActData.createUserId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.createUserId));
				rePlanActData.createUserName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.createUserName));
				rePlanActData.createTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.createTime));
				rePlanActData.modifyTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.modifyTime));
				rePlanActData.description = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.description));
				rePlanActData.thnr = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.thnr));
				rePlanActData.jg = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.jg));
				rePlanActData.jg = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.jg));
				rePlanActData.remark = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.remark));
				rePlanActData.zj = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.zj));
				rePlanActData.purpose = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.purpose));
				rePlanActData.planId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.planId));
				rePlanActData.planName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.planName));
				rePlanActData.closeFlag = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.closeFlag));
				rePlanActData.eventAddress = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.eventAddress));
				rePlanActData.finishedTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.finishedTime));
				rePlanActData.actvtStatus = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.actvtStatus));
				rePlanActData.status = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.status));
				rePlanActData.planStartTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.planStartTime));
				rePlanActData.planEndTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.planEndTime));
				rePlanActData.startTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.startTime));
				rePlanActData.endTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.endTime));
				rePlanActData.reportTime = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.reportTime));
				rePlanActData.typeName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.typeName));
				rePlanActData.contactName = cursor.getString(cursor.getColumnIndex(DataTables.DayTask.contactName));
				rePlanActData.isAppAdd = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.isAppAdd));
				rePlanActData.contactId = cursor.getInt(cursor.getColumnIndex(DataTables.DayTask.contactId));
				list.add(rePlanActData);
			}
			// }
			// db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				// db.endTransaction();
				db.close();
			}
		}
		return list;
	}

	public synchronized void updateOfflineActPlanInfoSuccess(RePlanActData rePlanActData, int isUpLoadSuccess) {
		try {

			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();

			if (rePlanActData != null) {
				ContentValues values = new ContentValues();
				values.put(DayTask.uuid, rePlanActData.uuid);
				values.put(DayTask.activityId, rePlanActData.activityId);
				values.put(DayTask.name, rePlanActData.name);
				values.put(DayTask.assignedUserId, rePlanActData.assignedUserId);
				values.put(DayTask.assignedUserName, rePlanActData.assignedUserName);
				values.put(DayTask.deptId, rePlanActData.deptId);
				values.put(DayTask.deptName, rePlanActData.deptName);
				values.put(DayTask.accountId, rePlanActData.accountId);
				values.put(DayTask.accountName, rePlanActData.accountName);
				values.put(DayTask.isRemind, rePlanActData.isRemind);
				values.put(DayTask.remindAhead, rePlanActData.remindAhead);
				values.put(DayTask.remindTime, rePlanActData.remindTime);
				values.put(DayTask.createUserId, rePlanActData.createUserId);
				values.put(DayTask.createUserName, rePlanActData.createUserName);
				values.put(DayTask.createTime, rePlanActData.createTime);
				values.put(DayTask.modifyTime, rePlanActData.modifyTime);
				values.put(DayTask.description, rePlanActData.description);
				values.put(DayTask.thnr, rePlanActData.thnr);
				values.put(DayTask.xybjh, rePlanActData.xybjh);
				values.put(DayTask.jg, rePlanActData.jg);
				values.put(DayTask.jg, rePlanActData.jg);
				values.put(DayTask.remark, rePlanActData.remark);
				values.put(DayTask.zj, rePlanActData.zj);
				values.put(DayTask.purpose, rePlanActData.purpose);
				values.put(DayTask.planId, rePlanActData.planId);
				values.put(DayTask.planName, rePlanActData.planName);
				values.put(DayTask.closeFlag, rePlanActData.closeFlag);
				values.put(DayTask.eventAddress, rePlanActData.eventAddress);
				values.put(DayTask.finishedTime, rePlanActData.finishedTime);
				values.put(DayTask.actvtStatus, rePlanActData.actvtStatus);
				values.put(DayTask.status, rePlanActData.status);
				values.put(DayTask.planStartTime, rePlanActData.planStartTime);
				values.put(DayTask.planEndTime, rePlanActData.planEndTime);
				values.put(DayTask.startTime, rePlanActData.startTime);
				values.put(DayTask.endTime, rePlanActData.endTime);
				values.put(DayTask.reportTime, rePlanActData.reportTime);
				values.put(DayTask.typeName, rePlanActData.typeName);
				values.put(DayTask.contactName, rePlanActData.contactName);
				values.put(DayTask.isAppAdd, rePlanActData.isAppAdd);
				values.put(DayTask.contactId, rePlanActData.contactId);
				values.put(DayTask.updateTime, Util.getNowTime());
				
				values.put("ismodify", "" + isUpLoadSuccess);
				db.update(DataTables.DAYTASK, values, "uuid = ?", new String[] { rePlanActData.uuid });
			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
}
