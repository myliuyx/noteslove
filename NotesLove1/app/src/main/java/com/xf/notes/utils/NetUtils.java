package com.xf.notes.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by liuyuanxiao on 15/1/7.
 */
public class NetUtils {
    /**
     * 判断当前是否有网络
     * @param context
     * @return
     */
    public static boolean checkNetWorkStatus(Context context)
    {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.getType() == ConnectivityManager.TYPE_WIFI) {
            result = true;
            Log.i("NetStatus", "The net was connected and wifi");
        } else {
            result = false;
            Log.i("NetStatus", "The net was bad!");
        }
        return result;
    }
}
