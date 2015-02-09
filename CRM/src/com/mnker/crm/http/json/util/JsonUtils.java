package com.mnker.crm.http.json.util;

import com.google.gson.Gson;
import com.mnker.crm.http.json.ReAccount;
import com.mnker.crm.http.json.ReContacts;
import com.mnker.crm.http.json.ReLogin;
import com.mnker.crm.http.json.RePlan;
import com.mnker.crm.http.json.RePlanAct;

public class JsonUtils {
	public static ReLogin getLoginResult(String reLogin) {
		Gson gson = new Gson();
		ReLogin reLgoin = gson.fromJson(reLogin, ReLogin.class);

		return reLgoin;
	}

	public static RePlanAct getPlanActResult(String planAct) {
		Gson gson = new Gson();
		RePlanAct rePlanAct = gson.fromJson(planAct, RePlanAct.class);

		return rePlanAct;
	}

	public static ReContacts getContactsResult(String reContact) {
		Gson gson = new Gson();
		ReContacts reContacts = gson.fromJson(reContact, ReContacts.class);

		return reContacts;
	}

	public static RePlan getPlanResult(String reContact) {
		Gson gson = new Gson();
		RePlan rePlan = gson.fromJson(reContact, RePlan.class);

		return rePlan;
	}

	public static ReAccount getAccountResult(String reContact) {
		Gson gson = new Gson();
		ReAccount reAccount = gson.fromJson(reContact, ReAccount.class);

		return reAccount;
	}
}
