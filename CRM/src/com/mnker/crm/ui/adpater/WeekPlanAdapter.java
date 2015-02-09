package com.mnker.crm.ui.adpater;


import java.util.List;
import java.util.Map;

import com.mnker.crm.R;
import com.mnker.crm.http.json.RePlanData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class WeekPlanAdapter extends BaseAdapter{

	private Context context;
	private List<RePlanData> listData;
    
	public WeekPlanAdapter(Context mContext,List<RePlanData> listData) {
		this.context = mContext;
		this.listData = listData;
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RePlanData planData = listData.get(position);
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.weekplanitem, null);
			viewHolder = new ViewHolder();
			viewHolder.planName = (TextView)convertView.findViewById(R.id.planName);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.planName.setText(planData.planName);
		convertView.setTag(viewHolder);
		return convertView;
	}
	
	
	static class ViewHolder{
		TextView planName;
	}


}
