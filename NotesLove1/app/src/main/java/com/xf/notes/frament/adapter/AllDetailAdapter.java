package com.xf.notes.frament.adapter;

import java.util.List;

import com.xf.notes.R;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.ConsumptionDetaileModel;
import com.xf.notes.model.HomeMode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * HomeFragment 主页 昨天 本月消费GridView 适配器
 *
 * @author liuyuanxiao
 */
public class AllDetailAdapter extends BaseAdapter {
    private final String TAG = "AllDetailAdapter";
    private List<ConsumptionDetaileModel> mList;
    private LayoutInflater mInflater;
    private int text_color;

    public AllDetailAdapter(List<ConsumptionDetaileModel> mList, Context context, int text_color) {
        this.mList = mList;
        this.text_color = text_color;
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
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_gridview_item, null);
            viewHolder.tv_Name = (TextView) convertView
                    .findViewById(R.id.tv_gridview_text);
            viewHolder.iv_Icon = (ImageView) convertView
                    .findViewById(R.id.iv_gridview_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ConsumptionDetaileModel model = mList.get(position);
        Object consumpNumber = model.getConsumption_count();
        // 如果消费金额大于1000 或者 是整数。均显示整数
        if (!HomeMode.isDouble(model.getConsumption_count()) || model.getConsumption_count() > 1000) {
            consumpNumber = model.getConsumption_count().intValue();
        }
        String typeName = model.getConsumption_name();
        //总体显示只需要2个文字，多了影响美观
        if (typeName.length() > 2) {
            typeName = model.getConsumption_name().substring(0, 2);
        }
        if (model.getConsumption_type() == Constants.CONSUPTION_CUSTOM) {
            typeName = "DIY";
        }
        String item_name = typeName + ":" + consumpNumber + "元";
        LogUtil.ld(TAG, item_name);
        setIcon(model.getConsumption_type(), viewHolder.iv_Icon);
        viewHolder.tv_Name.setText(item_name);
        viewHolder.tv_Name.setTextColor(text_color);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_Name;
        ImageView iv_Icon;
    }

    /**
     * 设置Item 图片和名称
     *
     * @param type
     * @param iv_view
     */
    private void setIcon(int type, ImageView iv_view) {
        switch (type) {
            case Constants.CONSUPTION_CLOTING:
                iv_view.setBackgroundResource(R.color.clothing);
                break;
            case Constants.CONSUPTION_FOOT:
                iv_view.setBackgroundResource(R.color.food);
                break;
            case Constants.CONSUPTION_FRUITS_VEGEBELES:
                iv_view.setBackgroundResource(R.color.fruits_vegetables);
                break;
            case Constants.CONSUPTION_HAVE_MEAL:
                iv_view.setBackgroundResource(R.color.have_meal);
                break;
            case Constants.CONSUPTION_MISCELLANEOUS:
                iv_view.setBackgroundResource(R.color.miscellaneous);
                break;
            case Constants.CONSUPTION_NECESSITES:
                iv_view.setBackgroundResource(R.color.necessites);
                break;
            case Constants.CONSUPTION_TRAFFIC:
                iv_view.setBackgroundResource(R.color.traffic);
                break;
            case Constants.CONSUPTION_CUSTOM:
                iv_view.setBackgroundResource(R.color.custom_type);
                break;
        }
    }
}
