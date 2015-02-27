package com.xf.notes.activity.callback;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.xf.notes.common.LogUtil;

/**
 * Created by liuyuanxiao on 15/1/20.

public class AddpoinsListener implements OnAddPointsListener {
    private final String TAG = "AddpoinsListener";
    private Context context;

    public AddpoinsListener(Context context) {
        this.context = context;
    }

    @Override
    public void addPointsSucceeded(String s, String s2, int i) {
        LogUtil.ld(TAG,"ad_name ->"+s+" ad_pack_name ->"+s2+" number ->"+i);
        MobclickAgent.onEvent(context, "downloadcount");
    }

    @Override
    public void addPointsFailed(String s) {
        LogUtil.ld(TAG," AddpoinsListenr Error ->"+s);
    }

}*/
