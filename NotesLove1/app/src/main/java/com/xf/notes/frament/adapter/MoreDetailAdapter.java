package com.xf.notes.frament.adapter;

import java.util.List;

import com.xf.notes.R;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.SummaryModel;
import com.xf.notes.utils.DensityUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class MoreDetailAdapter extends BaseAdapter {
	private final String TAG = "MoreFragment";
	private List<SummaryModel> mList;
	private LayoutInflater mInflater;
	private Context context;
	private int qury_type;
	public MoreDetailAdapter(List<SummaryModel> mList, Context context,int qury_type) {
		this.mList = mList;
		this.context = context;
		this.qury_type = qury_type;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.more_listview_item, null);
			holder.tv_day = (TextView) convertView.findViewById(R.id.consumption_day);
			holder.tv_moth = (TextView) convertView.findViewById(R.id.consumption_moth);
			holder.tv_years = (TextView) convertView.findViewById(R.id.consumption_year);
			holder.tv_title = (TextView) convertView.findViewById(R.id.title_year);
			holder.tv_number = (TextView) convertView.findViewById(R.id.titile_money);
			holder.gd_dateile = (GridView) convertView.findViewById(R.id.grd_more_detaile);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.gd_dateile.setVerticalSpacing(DensityUtil.dip2px(context, 6));
		SummaryModel model = mList.get(position);
		String consumption_date = model.getConsumption_date();
		String moth =consumption_date.substring(5,7);
		String day = consumption_date.substring(8,consumption_date.length());
//		String moth_day = consumption_date.substring(5,consumption_date.length());
		holder.tv_day.setText(day+"号");
		holder.tv_moth.setText(moth+"月");
		holder.tv_years.setText(model.getConsumption_year()+"年");
		visibileDate(qury_type, holder,consumption_date);
		holder.tv_number.setText(model.getConsumptionCount()+"￥");
		AllDetailAdapter adapter = new AllDetailAdapter(model.getConsumptionNumberMap(),
				context,context.getResources().getColor(R.color.gray_text));
		LogUtil.ld(TAG, "List Size -->" +model.getConsumptionNumberMap().size());
		holder.gd_dateile.setAdapter(adapter);
		return convertView;
	}
	private void visibileDate(int q_type,ViewHolder holder,String date){
		if (q_type == Constants.SELECT_DAY) {
			String titleDate = date.substring(5,date.length());
			holder.tv_title.setText(titleDate+"号共支出 ");
			holder.tv_day.setVisibility(View.VISIBLE);
			holder.tv_moth.setVisibility(View.VISIBLE);
			holder.tv_years.setVisibility(View.VISIBLE);
		}else if(q_type == Constants.SELECT_MOTH){
			String titleDate = date.substring(5,7);;
			holder.tv_title.setText(titleDate+"月共支出 ");
			holder.tv_day.setVisibility(View.GONE);
			holder.tv_moth.setVisibility(View.VISIBLE);
			holder.tv_years.setVisibility(View.VISIBLE);
		}else if(q_type == Constants.SELECT_YEARS){
			String titleDate = date.substring(0,4);;
			holder.tv_title.setText(titleDate+"年共支出 ");
			holder.tv_day.setVisibility(View.GONE);
			holder.tv_moth.setVisibility(View.GONE);
			holder.tv_years.setVisibility(View.VISIBLE);
		}
	}
	class ViewHolder {
		TextView tv_years;
		TextView tv_moth;
		TextView tv_day;
		TextView tv_title;
		TextView tv_number;
		GridView gd_dateile;
	}
}
