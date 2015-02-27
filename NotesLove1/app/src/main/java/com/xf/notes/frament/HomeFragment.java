package com.xf.notes.frament;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xf.notes.MainActivity;
import com.xf.notes.R;
import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.activity.APPListActivity;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.frament.adapter.AllDetailAdapter;
import com.xf.notes.model.ConsumptionDetaileModel;
import com.xf.notes.model.ConsumptionModel;
import com.xf.notes.model.HomeMode;
import com.xf.notes.utils.DateUtils;

public class HomeFragment extends Fragment implements OnClickListener {
    private final String TAG = "HomeFragment";
    private final int FIND_SQLITE_DATA_SUCCESSFUL = 1;
    private final int FIND_SQLITE_DATA_ERROR = -1;

    MainActivity activity;// 父Activity
    private TextView tv_today_count;// 今日消费
    private Button add_account_linearlayout; //记一笔layout
    private RelativeLayout record_yesterday;// 昨天支出layout
    private RelativeLayout record_this_month;// 本月支出 layout
    private Button today_detail_bt; // 明细按钮
    private Button more_button;// 查看更早的
    private HomeMode homeMode;
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case FIND_SQLITE_DATA_SUCCESSFUL:
                    updateUI();
                    break;
                case FIND_SQLITE_DATA_ERROR:
                    Toast.makeText(activity, "小爱在查询您的记录的时候发生了错误~", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    public void onAttach(Activity activity) {
        LogUtil.ld(TAG, "activity == NULL");
        this.activity = (MainActivity) activity;
        this.activity.isHomeFragment = true;
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity.isShowBackArrow();
        this.activity.setTitileNma("记账");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(rootView);
        //启动线程获取记账信息
        new Thread(new SQLiteThread()).start();
        return rootView;
    }

    //初始化控件操作
    private void initView(View v) {
        tv_today_count = (TextView) v.findViewById(R.id.tv_today_count);
        add_account_linearlayout = (Button) v.findViewById(R.id.add_account_linearlayout);
        record_yesterday = (RelativeLayout) v.findViewById(R.id.record_yesterday);
        record_this_month = (RelativeLayout) v.findViewById(R.id.record_this_month);
        more_button = (Button) v.findViewById(R.id.more_button);
        today_detail_bt = (Button) v.findViewById(R.id.today_detail_bt);
        today_detail_bt.setOnClickListener(this);
        add_account_linearlayout.setOnClickListener(this);
        more_button.setOnClickListener(this);
        record_yesterday.setOnClickListener(this);// 昨天支出
        record_this_month.setOnClickListener(this);// 本月支出
        adManager(v);

    }

    private void adManager(View v) {
        ImageButton boutique_bt = (ImageButton) v.findViewById(R.id.boutique_bt);
        ImageButton tuijian_bt = (ImageButton) v.findViewById(R.id.tuijian_bt);
        String today = DateUtils.get_0(null);

//        if (Constants.AD_MANAGER < 1 ) {
        tuijian_bt.setVisibility(View.GONE);
        boutique_bt.setVisibility(View.GONE);
//        }
        tuijian_bt.setOnClickListener(new AdManagerClick());
        boutique_bt.setOnClickListener(new AdManagerClick());
    }

    private class AdManagerClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            showAdManager(v);
        }
    }

    //根据自定义参数显示什么广告
    private void showAdManager(View v) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_account_linearlayout:
                AddDetaileFragment addDetaileFragment = new AddDetaileFragment();
                activity.startFragment(addDetaileFragment, null);
                break;
            case R.id.more_button:
                MoreFragment moreFragment = new MoreFragment();
                activity.startFragment(moreFragment, null);
                break;
            case R.id.today_detail_bt:
                double todayNumber = Double.parseDouble(homeMode.getTodayCount().toString());
                if (todayNumber > 0) {
                    DayFragment dayFragment = new DayFragment();
                    Bundle today = new Bundle();
                    today.putInt("isToDay", Constants.HOME_QUERY_TODAY);
                    activity.startFragment(dayFragment, today);
                } else {
                    Toast.makeText(activity, "小爱没有找到您今天的详细记录,记一笔吧！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.record_yesterday:
                DayFragment dayFragment = new DayFragment();
                Bundle today = new Bundle();
                today.putInt("isToDay", Constants.HOME_QUERY_YESTERDAY);
                activity.startFragment(dayFragment, today);
                break;
            case R.id.record_this_month:
                MothFragment mothFragment = new MothFragment();
                activity.startFragment(mothFragment, null);
                break;
        }

    }

    /**
     * 数据加载完毕后更新UI
     */
    private void updateUI() {
        Object today_count = homeMode.getTodayCount();
        tv_today_count.setText("今日支出:" + today_count + "元");
        initDetaileView(record_yesterday,
                homeMode.getYesterdayDetailed(),
                "昨天共支出", homeMode.getYesterdayCount() + "￥");
        initDetaileView(record_this_month,
                homeMode.getThisMothDetailed(),
                "本月共支出",
                homeMode.getThisMothCount() + "￥");

    }

    private void initDetaileView(View view, List<ConsumptionDetaileModel> dataMap, String title, String monye) {
        LogUtil.ld(TAG, title + "List Size : " + dataMap.size());
        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        GridView detail_notes = (GridView) view.findViewById(R.id.detail_notes);
        tv_date.setText(title);
        tv_money.setText(monye);
        int colorInt = activity.getResources().getColor(R.color.white);
        AllDetailAdapter adapter = new AllDetailAdapter(dataMap, activity, colorInt);
        detail_notes.setAdapter(adapter);

    }

    /**
     * 本地数据库数据获取类
     *
     * @author liuyuanxiao
     */
    class SQLiteThread implements Runnable {
        public void run() {
            homeMode = new HomeMode();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            Message msg = mHandler.obtainMessage();
            try {
                cal.set(year, month, day - 1);
                Double todayCount = activity.dbHelper.getDayCountNumber(DateUtils.get_0(new Date()), ConsumptionHelper.QUERY_DAY);
                Date date = new Date(cal.getTimeInMillis());
                Double yesterdayCount = activity.dbHelper.getDayCountNumber(DateUtils.get_0(date), ConsumptionHelper.QUERY_DAY);
                Double thisMothCount = activity.dbHelper.getMothCountNumber(DateUtils.get_6(new Date()),
                        ConsumptionHelper.QUERY_MOTH);
                List<ConsumptionModel> yesterDayWaterList = activity.dbHelper.getConsumption(DateUtils.get_0(date),
                        ConsumptionHelper.QUERY_DAY,
                        ConsumptionHelper.SOTRING_NAME);
                List<ConsumptionModel> thisMothWaterList = activity.dbHelper.getMothConsumption(DateUtils.get_6(new Date()),
                        ConsumptionHelper.SOTRING_NAME);
                LogUtil.ld(TAG, "thisMothWaterList List Size : " + thisMothWaterList.size());
                List<ConsumptionDetaileModel> yesteDayMap = activity.dbHelper.statisticalConsumptionList(yesterDayWaterList);
                List<ConsumptionDetaileModel> thisMothMap = activity.dbHelper.statisticalConsumptionList(thisMothWaterList);
                LogUtil.ld(TAG, "thisMothMap List Size : " + thisMothMap.size());
                homeMode.setTodayCount(todayCount);//今天消费总金额
                homeMode.setYesterdayCount(yesterdayCount);//昨天消费总金额
                homeMode.setThisMothCount(thisMothCount);//本月消费总金额
                homeMode.setYesterdayDetailed(yesteDayMap);//昨天消费金额统计
                homeMode.setThisMothDetailed(thisMothMap);//本月消费金额统计
                msg.what = FIND_SQLITE_DATA_SUCCESSFUL;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.le(TAG, e.getMessage());
                msg.what = FIND_SQLITE_DATA_ERROR;
                mHandler.sendMessage(msg);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }
}