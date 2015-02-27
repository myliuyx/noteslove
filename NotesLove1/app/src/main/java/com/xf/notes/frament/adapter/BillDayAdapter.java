package com.xf.notes.frament.adapter;

import java.util.List;

import com.xf.notes.R;
import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.DBhelper.SummaryThread;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.frament.DayFragment;
import com.xf.notes.model.ConsumptionModel;
import com.xf.notes.utils.ImageViewUtils;
import com.xf.notes.view.BaseDialog;
import com.xf.notes.view.BaseDialog.AletLstener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BillDayAdapter extends BaseAdapter {
	private final String TAG = "BillDayAdapter";
	private List<ConsumptionModel> mList;
	private LayoutInflater mInflater;
	private ConsumptionHelper cHelper;
	private DayFragment fragment;
	public BillDayAdapter(List<ConsumptionModel> mList,DayFragment fragment) {
		this.mList = mList;
		this.fragment = fragment;
		mInflater = LayoutInflater.from(fragment.getActivity());
		cHelper = ConsumptionHelper.getInstance(fragment.getActivity());
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.more_day_list_item, null);
			viewHolder.bill_ic = (ImageView) convertView
					.findViewById(R.id.consumption_type_iv);
			viewHolder.bill_name = (TextView) convertView
					.findViewById(R.id.consumption_type_name_tv);
			viewHolder.bill_number = (TextView) convertView
					.findViewById(R.id.consumption_type_numb_tv);
			viewHolder.bill_remove_bt = (Button) convertView
					.findViewById(R.id.bill_remove_bt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ConsumptionModel model = mList.get(position);
		LogUtil.ld(TAG, model.getConsumption_name() + "---->" + model.getConsumption_number()+"-->"+position);
		if (model != null) {
			ImageViewUtils.setIcon(model.getConsumption_type(), viewHolder.bill_ic);
		}
		String bill_number = model.getConsumption_number() + "元";
		viewHolder.bill_number.setText(bill_number);
		String bill_name;
		if (model.getConsumption_desc() != null
				&& !model.getConsumption_desc().equals("")) {
			bill_name = model.getConsumption_desc();
		} else {
			bill_name = model.getConsumption_name();
		}
		viewHolder.bill_name.setText(bill_name);
		viewHolder.bill_remove_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final ConsumptionModel removeModel = mList.get(position);
				String msg = "";
				if (removeModel.getConsumption_desc()!= null &&!"".equals(removeModel.getConsumption_desc()) ) {
					msg = "您确定删除："+removeModel.getConsumption_desc()+" 这笔账单？";
				}else{
					msg = "您确定删除："+removeModel.getConsumption_name()+" 这笔账单？";
				}
				BaseDialog dialog = new BaseDialog(fragment.getActivity(),
						R.style.varDialogTheme, null, msg);
				dialog.SetListener(new AletLstener() {
					
					@Override
					public void okClick(BaseDialog diaglog) {
						boolean reslut = cHelper.deleteConsumption(removeModel.get_id());
						if (reslut) {
							// 如果删除的时昨天的信息,则重新汇总昨天信息
							if (fragment.isToday == Constants.HOME_QUERY_YESTERDAY) {
								new Thread(new SummaryThread
										(removeModel.getConsumption_date(), 
												cHelper)).start();
							}
							// 删除成功后，将现有的查询结果里面 删除 此记录
							mList.remove(position);
							fragment.setDetaile_Adatper(mList, fragment);
						}
						diaglog.dismiss();
					}
					@Override
					public void cancelClick(BaseDialog diaglog) {
						diaglog.dismiss();
					}
				});
				dialog.show();
			}
		});
		return convertView;
	}
	
	class ViewHolder {
		ImageView bill_ic;
		TextView bill_name;
		TextView bill_number;
		Button bill_remove_bt;
	}
}
