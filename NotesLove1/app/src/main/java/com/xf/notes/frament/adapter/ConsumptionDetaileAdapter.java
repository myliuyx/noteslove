package com.xf.notes.frament.adapter;

import java.util.List;

import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.R;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.ConsumptionTypeModel;
import com.xf.notes.utils.ImageViewUtils;
import com.xf.notes.view.AddTpyeDialog;
import com.xf.notes.view.BaseDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConsumptionDetaileAdapter extends BaseAdapter {
    private final String TAG = "ConsumptionDetaileAdapter";
    private List<ConsumptionTypeModel> mList;
    private LayoutInflater mInflater;
//    private ConsumptionTypeModel cModel;
//    private Context context;
    //是否显示键盘
//    private boolean isShowInputMoth = false;


    public ConsumptionDetaileAdapter(List<ConsumptionTypeModel> mList,Context context) {
        this.mList = mList;
//        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.add_detail_item, null);
            viewHolder.ly_all_item = (LinearLayout) convertView
                    .findViewById(R.id.ly_imte_all);
            viewHolder.tv_Name = (TextView) convertView
                    .findViewById(R.id.type_name);
            viewHolder.iv_Icon = (ImageView) convertView
                    .findViewById(R.id.type_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ConsumptionTypeModel model = mList.get(position);
        viewHolder.tv_Name.setText(model.getConsumption_name());
        ImageViewUtils.setIcon(model, viewHolder.iv_Icon);
        return convertView;
    }

    private class ViewHolder {
        LinearLayout ly_all_item;
        TextView tv_Name;
        ImageView iv_Icon;
    }
}
