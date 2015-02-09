package com.mnker.crm.tool;

public class UndoWorkCollectInfo {
	public String month;
	public int total;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "UndoWorkCollectInfo [month=" + month + ", total=" + total + "]";
	}

}
