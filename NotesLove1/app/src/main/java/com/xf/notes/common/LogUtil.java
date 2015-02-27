package com.xf.notes.common;



import android.util.Log;
/**
 * 日志管理类
 * @author liuyuanxiao
 *
 */
public class LogUtil {
	//默认TAG
	private static final String TAG = "NoetsLoveLogUtil";
	public static void ld(String tag, String msg) {
		tag = tagNULL(tag);
		if (Constants.LogLevel > 0) {
			Log.d(tag, msg);
		}
	}
	public static void le(String tag, String msg) {
		tag = tagNULL(tag);
		if (Constants.LogLevel > 5) {
			Log.e(tag, msg);
		}
	}
    public static void lw(String tag, String msg) {
        tag = tagNULL(tag);
        if (Constants.LogLevel > 4) {
            Log.w(tag, msg);
        }
    }
	private static String tagNULL(String tag){
		if (null == tag || tag.equals("")) {
			tag = TAG;
		}
		return tag;
	}
}
