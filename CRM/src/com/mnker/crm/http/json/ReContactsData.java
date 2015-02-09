package com.mnker.crm.http.json;

import java.io.Serializable;

import com.mnker.crm.tool.Tool;

public class ReContactsData implements Serializable,Comparable<ReContactsData>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String uuid;
	public int contactId;
	public String contactName;
	public String contactType;
	public String stopFlag;
	public String genderId;
	public String genderName;
	public String maritalName;
	public String salutationName;
	public String birthDate;
	public String deptName;
	public String ownerName;
	public String ownerId;
	public String position;
	public String accountName;
	public String accountId;
	public String reportTo;
	public String mobile;
	public String officePhone;
	public String homePhone;
	public String fax;
	public String email;
	public String mailingZipCode;
	public String mailingAddress;
	public String description;
	public String zc;
	public String sfcgzjkcy;
	public String zcmm;
	public String qq;
	public String sr;
	public String byzyy;
	public String bysjy;
	public String bysje;
	public String byzy3;
	public String bysj3;
	public String byys1;
	public String byys2;
	public String byys3;
	public String shzw;
	public String shzwms;
	public String mz;
	public String rzsj;
	public String pexm;
	public String pesj;
	public String pegzdw;
	public String posr;
	public String jtqtzycyqk;
	public String jtqk;
	public String zycg;
	public String gzll;
	public String xgtz;
	public String jkqk;
	public String sh;
	public String zyxxah;
	public String xhdds;
	public String zjxy;
	public String cykw;
	public String khjsfa;
	public String khwhfa;
	public String fzqy;
	public String fzcplb;
	public String zyz;
	public String ks;
	public String sfjfmkzz;
	public String zw;
	public String sf;
	public String ssbm;
	public String lxrfl;
	public String firstChar;
	public void setFirstChar(){
		firstChar=Tool.converterToFirstSpell(this.contactName).toUpperCase();
	}
	@Override
	public int compareTo(ReContactsData another) {
//		String sort1 = this.;
//		String sort2 = Tool.converterToFirstSpell(another.contactName).toUpperCase();
		return this.firstChar.compareTo(another.firstChar);  
	}
}
