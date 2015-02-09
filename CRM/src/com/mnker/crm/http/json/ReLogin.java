package com.mnker.crm.http.json;

import java.util.Map;

public class ReLogin extends BaseRequest {
	// "data": {
	// "name": "test",
	// "token": "dzutTnWAIVNy",
	// "deptName": "云南迈克（A）组",
	// "deptId": 1013
	// },
	// "actTypes":{"2683":"计划外工作记录","2366":"工作记录"}
	public ReLgoinData data;
//	public ReActypeData actTypes;
	public Map<String, String> actTypes;
}
