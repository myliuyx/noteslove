package com.xf.notes.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.xf.notes.NotesApplication;
import com.xf.notes.R;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.AppInfoModel;

import java.util.List;

/**
 * Created by liuyuanxiao on 15/1/20.
 */
public class APPListAdapter extends BaseAdapter {
    private final String TAG = "AdListAdapter";
    private Context context;
    private List<AppInfoModel> mList;
    private LayoutInflater mInflater;

    public APPListAdapter(Context context, List<AppInfoModel> mList) {
        this.context = context;
        this.mList = mList;
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
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (v == null) {
            v = mInflater.inflate(R.layout.applist_item, null);
            viewHolder.app_Icon = (NetworkImageView) v.findViewById(R.id.app_icon_iv);
            viewHolder.app_Name = (TextView) v.findViewById(R.id.app_name_tv);
            viewHolder.app_size = (TextView) v.findViewById(R.id.app_size_tv);
            viewHolder.app_title = (TextView) v.findViewById(R.id.app_summarize);
            viewHolder.donwload_bt = (Button) v.findViewById(R.id.download_bt);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        final AppInfoModel model = mList.get(position);
        String url = model.getIcon_url();
        if(null != url && !url.equals("")){
            viewHolder.app_Icon.setImageUrl(url,NotesApplication.getImageLoader());
        }
        viewHolder.app_Name.setText(model.getApp_name());
        viewHolder.app_size.setText(model.getApp_size());
        viewHolder.app_title.setText(model.getApp_title());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,APPDetailActivity.class);
                intent.putExtra("appinfo",model);
                context.startActivity(intent);
            }
        });
        viewHolder.donwload_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.ld("AddpoinsListener","download ->"+model.getApp_name());
                //下载按钮被点击了
//                MobclickAgent.onEvent(context, "adclick");
//                DevInit.download(context,model.getApp_name() ,AdType.ADLIST, new AddpoinsListener(context));
            }
        });
        return v;
    }

    private class ViewHolder {
        NetworkImageView app_Icon;
        Button donwload_bt;
        TextView app_Name;
        TextView app_size;
        TextView app_title;
    }
}
