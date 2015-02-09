package com.mnker.crm.http.json;

import java.io.Serializable;

public class RePlanData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "data": [
	// {
	// "uuid": "4e45d719-9432-42d6-becb-f8493e2b2fc6",
	// "planId": 28154,
	// "planName": "test-2014-08-04到2014-08-08工作计划",
	// "startDate": "2014-08-04",
	// "endDate": "2014-08-08"
	// },
	// {
	// "uuid": "df85323b-2dba-4eb5-a1a9-aa49a5d603f9",
	// "planId": 28152,
	// "planName": "test-2014-08-03到工作计划",
	// "startDate": "2014-08-03",
	// "endDate": "2014-08-03"
	// }
	// ]
	public String uuid;
	public int planId;
	public String planName;
	public String startDate;
	public String endDate;
}
