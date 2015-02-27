package com.xf.notes.frament.adapter;

import java.util.List;

import com.xf.notes.R;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.ConsumptionDetaileModel;
import com.xf.notes.model.ConsumptionTypeModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopupGridViewAdapter extends BaseAdapter {
    private final String TAG = "PopupGridViewAdapter";
	private LayoutInflater mInflater;
	private List<ConsumptionTypeModel> consumpType;
	
	public PopupGridViewAdapter(List<ConsumptionTypeModel> consumpType,Context context) {
		this.consumpType = consumpType;
		mInflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		return consumpType.size();
	}

	@Override
	public Object getItem(int position) {
		return consumpType.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.more_popup_item, null);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.popup_tv_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
        ConsumptionTypeModel model = consumpType.get(position);
		viewHolder.tv_name.setText(model.getConsumption_name());
		return convertView;
	}
	class ViewHolder{
		TextView tv_name;
	}
}
