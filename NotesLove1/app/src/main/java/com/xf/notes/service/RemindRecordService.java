package com.xf.notes.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.notification.RemindNotification;
import com.xf.notes.utils.DateUtils;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时提醒服务
 */
public class RemindRecordService extends Service implements Handler.Callback {
    private final String TAG = getClass().getSimpleName();
    private final String REMIND_KEY = "remind_time";
    private SharedPreferences sp;
    private final String receiverFilter = "com.xf.notes.service.ReminRecordReceiver";
    private ReminRecordReceiver receiver;
    private final int TIMERTASK_CALLBACK = 1;
    private final Timer timer = new Timer();
    private Handler mHandler = new Handler(this);
    private ConsumptionHelper helper;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.ld(TAG, TAG + " onCreate ~");
        sp = getSharedPreferences(Constants.CONFIG_NOTES, Context.MODE_PRIVATE);
        helper = ConsumptionHelper.getInstance(RemindRecordService.this);
        receiver = new ReminRecordReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(receiverFilter);
        getApplication().registerReceiver(receiver, filter);
        //每100分钟检查一次时间
        long period = 70 * 60 * 1000;
        timer.schedule(timerTask, 300 * 1000, period);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.ld(TAG, TAG + " onStartCommand ~");
//        onStartCommand(intent,startId);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == TIMERTASK_CALLBACK) {
            String contentMsg = (String) msg.obj;
            Intent intent = new Intent();
            intent.setAction(receiverFilter);
            intent.putExtra("massage", contentMsg);
            sendBroadcast(intent);
        }
        return false;
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                String remind_time = sp.getString(REMIND_KEY, "");
                //如果今日已经提醒过了， 直接返回
                LogUtil.ld(TAG, "TimerTask remind_time --->" + remind_time + " equals " + DateUtils.get_0(null).equals(remind_time));
                if (DateUtils.get_0(null).equals(remind_time) || !Constants.IS_REMIND) {
                    return;
                }
                //查询数据库今日账单数量
                int todayCount = helper.getTodayCount(DateUtils.get_0(null));
                String contentMsg = getNotifiMassage(todayCount);
                Message msg = mHandler.obtainMessage();
                msg.what = TIMERTASK_CALLBACK;
                msg.obj = contentMsg;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 获取通知提示语。
     *
     * @param count 今日记账数
     * @return 提示语信息
     */
    private String getNotifiMassage(int count) {
        String result = "";
        String reminTime = Constants.REMIND_TIME;
        String currtTime = DateUtils.get_4();
        //判断是否到了 提醒时间
        int compare = Time.valueOf(currtTime).compareTo(Time.valueOf(reminTime));
        LogUtil.ld(TAG, "Remind time --> " + compare);
        if (compare > 0 && count == 0) {
            result = "亲爱的,今天您还没有记账,别忘了哦~";
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(REMIND_KEY, DateUtils.get_0(null));
            editor.commit();
        }
        LogUtil.ld(TAG, "Remind result --> " + result);
        return result;
    }
    private class ReminRecordReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.ld(TAG, "Time Changed receiver~" + intent.getAction());
            String msg = intent.getStringExtra("massage");
            if (null != msg && !msg.equals(""))
                RemindNotification.getInstances(RemindRecordService.this).showNotification(msg);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplication().unregisterReceiver(receiver);
        LogUtil.ld(TAG, TAG + " onDestroy ~");
    }
}
