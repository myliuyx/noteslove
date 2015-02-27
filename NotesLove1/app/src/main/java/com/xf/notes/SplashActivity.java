package com.xf.notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.ConsumptionModel;
import com.xf.notes.service.RemindRecordService;
import com.xf.notes.utils.ConmsuptionModelComparator;
import com.xf.notes.utils.DateUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {
    private final String TAG = "SplashActivity";
    /**
     * 统计成功
     */
    private final int SUMMARY_SUCCESS = 1;
    /**
     * 统计失败
     */
    private final int SUMMARY_FAILURE = 0;

    /**
     * 上次汇总日期
     */
    private String summaryDate = "";
    private ConsumptionHelper cHelper;
    private SharedPreferences ps;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SUMMARY_SUCCESS:
                    LogUtil.ld(TAG, "统计" + msg.obj + "成功~");
                    break;
                case SUMMARY_FAILURE:
                    LogUtil.ld(TAG, "统计" + msg.obj + "失败~");
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /******************Init AD**********************/
//        DevInit.initGoogleContext(this, "d0e871026e6f76cae7651e430f4e9343");
//        ManagerInit.inittools(this, "19a50ec97fbf14ea", "216b4a9b75a03959");
//        ManagerInit.getInitInstance(this).initOfferAd(this);
        /**********************************************/
        setContentView(R.layout.splash_activity);
        cHelper = ConsumptionHelper.getInstance(this);
        ps = getSharedPreferences(Constants.CONFIG_NOTES, Context.MODE_PRIVATE);
        summaryDate = ps.getString(Constants.SUMMARY_DATE_NAME, "");
        LogUtil.ld(TAG, "summaryDate -->" + summaryDate);
        // 如果获取到了 存储时间则去比对时间，如果没有则添加时间
//		initSummaryConsumption(summaryDate);
        if (!summaryDate.equals("")) {
            LogUtil.ld(TAG, "init Summary date ->" + summaryDate);
            initSummaryConsumption(summaryDate);
        } else {
            Editor editor = ps.edit();
            editor.putString(Constants.SUMMARY_DATE_NAME, DateUtils.get_0(null));
            editor.commit();
        }
        /***********************Umeng**************************/
        MobclickAgent.setDebugMode(false);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        /***********************Umeng**************************/
//		修改计算错误日期
//		cHelper.updateSummaryDate("2014-11-30", "2014-11");
        ImageView splashImag = (ImageView) findViewById(R.id.splash_imag);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.loding);
        splashImag.setAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(getApplicationContext(), RemindRecordService.class);
        startService(intent);
    }

    private void initSummaryConsumption(final String summaryDate) {
        final String today = DateUtils.get_0(null);
        //汇总日期是今天则直接返回
        if (today.equals(summaryDate)) {
            LogUtil.ld(TAG, "today  equals true -->" + summaryDate);
            return;
        }
        // 子线程循环数据库 汇总信息
        new Thread() {
            public void run() {
                try {
                    boolean result = cHelper.insertToday(summaryDate);
                    LogUtil.ld(TAG, "insert yesterday count ->" + result);
                    // 将今日 日期写入文件，方便 下次汇总
                    Editor editor = ps.edit();
                    editor.putString(Constants.SUMMARY_DATE_NAME, today);
                    editor.commit();
                    Message message = handler.obtainMessage();
                    message.obj = summaryDate;
                    message.what = SUMMARY_SUCCESS;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = handler.obtainMessage();
                    message.obj = summaryDate;
                    message.what = SUMMARY_FAILURE;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 从指定的List 中按时间提取
     *
     * @param list
     * @return
     */
    private List<List<ConsumptionModel>> extractDay(List<ConsumptionModel> list) {
        List<List<ConsumptionModel>> dayList = new ArrayList<List<ConsumptionModel>>();
        List<ConsumptionModel> mList = null;
        String dateTime = "";
        for (ConsumptionModel m : list) {
            if (!m.getConsumption_date().equals(dateTime)) {
                if (mList != null) {
                    Collections.sort(mList, new ConmsuptionModelComparator());
                }
                dateTime = m.getConsumption_date();
                mList = new ArrayList<ConsumptionModel>();
                dayList.add(mList);
            }
            mList.add(m);
        }
        return dayList;
    }

}
