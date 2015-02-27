package com.xf.notes.frament.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.xf.notes.R;
import com.xf.notes.frament.adapter.BillDayAdapter.ViewHolder;
import com.xf.notes.model.ConsumptionModel;
import com.xf.notes.utils.DateUtils;
import com.xf.notes.utils.ImageViewUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * MothFramgent ListView 适配器
 * @author liuyuanxiao
 *
 */
public class MothFragmentAdapter extends BaseAdapter {
	private List<ConsumptionModel> mList;
	private LayoutInflater mInflater;
	public MothFragmentAdapter(List<ConsumptionModel> mList,Context context) {
		this.mList = mList;
		mInflater = LayoutInflater.from(context);
	}
	public void setList(List<ConsumptionModel> mList){
		this.mList = mList;
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
			convertView = mInflater.inflate(R.layout.moth_fragment_item, null);
			holder.iv_commp_type = (ImageView) convertView.findViewById(R.id.iv_conmption_type);
			holder.tv_commp_name = (TextView) convertView.findViewById(R.id.tv_conmption_name);
			holder.tv_commp_count = (TextView) convertView.findViewById(R.id.tv_conmption_count);
			holder.tv_commp_date = (TextView) convertView.findViewById(R.id.tv_conmption_date);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ConsumptionModel model = mList.get(position);
		ImageViewUtils.setIcon(model.getConsumption_type(), holder.iv_commp_type);
		String cmp_name = getCommp_name(model);
		holder.tv_commp_name.setText(cmp_name);
		holder.tv_commp_count.setText(model.getConsumption_number()+"元");
		String date = getCommp_date(model.getConsumption_date());
		holder.tv_commp_date.setText(date);
		return convertView;
	}
	/**
	 * 根据 datestr 解析 时间， 返回  xx号 星期x 
	 * @param datestr
	 * @return
	 */
	private String getCommp_date(String datestr){
		Date date = DateUtils.get_s2d(datestr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		String week = getWeek(weekday);
		String result = day+"号 "+week;
		return result;
	}
	/**
	 * 获取 commp_name 名称，如果有备注 返回备注， 没有备注则直接返回 name
	 * @param model
	 * @return
	 */
	private String getCommp_name(ConsumptionModel model){
		String cmp_name = "";
		if (model.getConsumption_desc()!= null && !model.getConsumption_desc().equals("")) {
			cmp_name = model.getConsumption_desc();
		}else{
			cmp_name = model.getConsumption_name();
		}
		return cmp_name;
	}
	private String getWeek(int week){
		String weekday = "";
		switch (week) {
		case 1:
			weekday ="星期天";
			break;
		case 2:
			weekday ="星期一";
			break;
		case 3:
			weekday ="星期二";
			break;
		case 4:
			weekday ="星期三";
			break;
		case 5:
			weekday ="星期四";
			break;
		case 6:
			weekday ="星期五";
			break;
		case 7:
			weekday ="星期六";
			break;
		}
		return weekday;
	}
	class ViewHolder{
		ImageView iv_commp_type;
		TextView tv_commp_name;
		TextView tv_commp_count;
		TextView tv_commp_date;
	}
}
