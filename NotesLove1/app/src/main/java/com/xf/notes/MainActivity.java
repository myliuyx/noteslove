package com.xf.notes;

import com.android.volley.http.RequestManager;
import com.umeng.analytics.MobclickAgent;
import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.frament.AboutFragment;
import com.xf.notes.frament.BackUpFragment;
import com.xf.notes.frament.FeedBackFragment;
import com.xf.notes.frament.HomeFragment;
import com.xf.notes.model.ConsumptionTypeModel;
import com.xf.notes.model.VersionModel;
import com.xf.notes.utils.DateUtils;
import com.xf.notes.utils.DownloadFileUtils;
import com.xf.notes.utils.NetUtils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private final String TAG = "MainActivity";
    /**, QJFNotifier
     * 保存本地version下载记录
     */
    private final String VERSION_CHECK_DATE = "version_date";
    public boolean isHomeFragment = false;
    private ImageButton imgbut_home_back;//返回 Button
    private ImageButton main_setting_iv;//设置 Button
    private TextView main_title_name;
    private PopupWindow mPopupWindow; // 弹出菜单
    private DownloadManager downloadManager;
    public SharedPreferences ps;
    private DownloadFileUtils downloadFile;
    public ConsumptionHelper dbHelper;
    public boolean is_check_version = false;// 是否手动点击刷新
    public int IS_FRAGMENT = -1;
    public int ABOUT_FRAGMENT = 1;//关于我们Fragment
    public int FEEDBACK_FRAGMENT = 2;//意见反馈Fragment
    public int BACKUP_FRAGMENT = 3;//备份Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            LogUtil.le(TAG, "super.onCreate(savedInstanceState) Error ~" + e.getMessage());
            e.printStackTrace();
        }
        Constants.IS_REMIND = false;
        IS_FRAGMENT = -1;
        LogUtil.ld(TAG, "MainActivity onCreate~");
        setContentView(R.layout.activity_main);
        //初始化文件下载
        ps = getSharedPreferences(Constants.CONFIG_NOTES, Context.MODE_PRIVATE);
        dbHelper = ConsumptionHelper.getInstance(MainActivity.this);
        initView();
        getAllConsumptionType();
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = beginTransa();
        fragmentTransaction.replace(R.id.home_fragment, fragment);
        main_title_name = (TextView) findViewById(R.id.main_title_name);
        fragmentTransaction.commit();
        MobclickAgent.updateOnlineConfig(this);
        String adManager = MobclickAgent.getConfigParams(this, "adManager");
        LogUtil.ld(TAG, "On Line Config adManager -->" + adManager);
        if (null != adManager && !adManager.equals("")) {
            Constants.AD_MANAGER = Integer.valueOf(adManager);
        }
        if (NetUtils.checkNetWorkStatus(MainActivity.this)) {
            //检查版本更新
            checkVersion();
        }
    }

    private void checkVersion() {
        String version_param = checkVersionParam();
        if (null != version_param) {
            String url = Constants.HOST_URL + Constants.INTERFACE_CHECK_VERSION;
            RequestManager.getInstance().post(url, version_param, new VersionRequsetListenr(), 0);
        }
    }

    private String checkVersionParam() {
        try {
            String packageName = getPackageName();
            int version_code = getPackageManager().getPackageInfo(packageName, 0).versionCode;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", Constants.TOKEN);
            jsonObject.put("packagename", packageName);
            jsonObject.put("versioncode", String.valueOf(version_code));
            return jsonObject.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTitileNma(String titleName) {
        main_title_name.setText(titleName);
    }

    public void initView() {
        imgbut_home_back = (ImageButton) findViewById(R.id.imgbut_home_back);
        main_setting_iv = (ImageButton) findViewById(R.id.main_setting_iv);
        main_setting_iv.setOnClickListener(this);
        imgbut_home_back.setOnClickListener(this);
    }

    //是否显示灰色
    public void isShowBackArrow() {
        if (isHomeFragment) {
            imgbut_home_back.setVisibility(View.GONE);
        } else {
            imgbut_home_back.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 切换 Fragment
     *
     * @param fragment 切换到得Fragment
     * @param bundle   传递参数
     */
    public void startFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction fragmentTransaction = beginTransa();
        fragmentTransaction.replace(R.id.home_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        if (null != bundle) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.commit();
    }

    public void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = beginTransa();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    public FragmentTransaction beginTransa() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        return fragmentTransaction;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.ld(TAG, "MainActivity onRestoreInstanceState~");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.ld(TAG, "MainActivity onSaveInstanceState~");
        Constants.IS_REMIND = true;
//        finish();
    }

    long outTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHomeFragment) {
                if ((System.currentTimeMillis() - outTime) > 2000) {
                    Toast.makeText(this, "再按一次退出~", Toast.LENGTH_SHORT).show();
                    outTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            } else {
                IS_FRAGMENT = -1;
                isHomeFragment = true;
                setTitileNma("记账");
                isShowBackArrow();
                super.onKeyDown(keyCode, event);
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /**
     * 显示popupWindow
     */
    private void showPopupWindow(VersionModel model) {
        View v = View.inflate(getApplicationContext(), R.layout.version_popup_view, null);
        TextView tile_tv = (TextView) v.findViewById(R.id.version_title);
        TextView tile_content = (TextView) v.findViewById(R.id.version_content);
        Button version_cancle = (Button) v.findViewById(R.id.version_cancel_bt);
        Button version_ok = (Button) v.findViewById(R.id.version_ok_bt);
        PopupWindowClick pClickListener = new PopupWindowClick(model);
        version_cancle.setOnClickListener(pClickListener);
        version_ok.setOnClickListener(pClickListener);
        tile_tv.setText(model.getTitle());
        tile_content.setText(Html.fromHtml(model.getContent()));
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(MainActivity.this);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
        }
        mPopupWindow.setContentView(v);
        View lv = this.findViewById(R.id.container);
        mPopupWindow.showAtLocation(lv, Gravity.BOTTOM, 0, 0);
    }

    //设置弹出 设置PopupWindow
    private void showSettingPopupWindow() {
        View v = View.inflate(getApplicationContext(), R.layout.setting_main_layout, null);
        RelativeLayout feedBack = (RelativeLayout) v.findViewById(R.id.feddback_layout);
        RelativeLayout check_version = (RelativeLayout) v.findViewById(R.id.check_version_layout);
        RelativeLayout backup = (RelativeLayout) v.findViewById(R.id.backup_layout);
        RelativeLayout about = (RelativeLayout) v.findViewById(R.id.about_layout);
        PopupWindowClick lisenter = new PopupWindowClick(null);
        feedBack.setOnClickListener(lisenter);
        check_version.setOnClickListener(lisenter);
        backup.setOnClickListener(lisenter);
        about.setOnClickListener(lisenter);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(MainActivity.this);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
        }
        mPopupWindow.setContentView(v);
        View view = findViewById(R.id.linearLayout1);
        mPopupWindow.showAsDropDown(view, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbut_home_back:
                if (!isHomeFragment) {
                    IS_FRAGMENT = -1;
                    startFragment(new HomeFragment(), null);
                    //隐藏键盘
                    InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    View view = MainActivity.this.getCurrentFocus();
                    if (imm != null && view != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                break;
            case R.id.main_setting_iv:
                showSettingPopupWindow();
                break;

        }
    }

    class PopupWindowClick implements View.OnClickListener {
        private VersionModel model;

        public PopupWindowClick(VersionModel model) {
            this.model = model;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            switch (v.getId()) {
                /***********设置 PopupWindow 4个按钮************/
                case R.id.feddback_layout:
                    if (IS_FRAGMENT != FEEDBACK_FRAGMENT)
                        startFragment(new FeedBackFragment(), null);
                    break;
                case R.id.check_version_layout:
                    mPopupWindow.dismiss();
                    if (NetUtils.checkNetWorkStatus(MainActivity.this)) {
                        is_check_version = true;
                        checkVersion();
                    }
                    break;
                case R.id.backup_layout:
                    if (IS_FRAGMENT != BACKUP_FRAGMENT)
                        startFragment(new BackUpFragment(), null);
                    break;
                case R.id.about_layout:
                    if (IS_FRAGMENT != ABOUT_FRAGMENT)
                        startFragment(new AboutFragment(), null);
                    break;
                /***********版本检测 确定  和 取消 按钮************/
                case R.id.version_cancel_bt:
//                    if (null != mPopupWindow) mPopupWindow.dismiss();
                    break;
                case R.id.version_ok_bt:
                    new Thread() {
                        @Override
                        public void run() {
                            downloadFile.downloadFile(model.getUrl(), "记账爱", "爱生活,爱记账,记账爱记录生活点滴", Constants.APK_PATH);
                        }
                    }.start();
                    break;
            }
            if (null != mPopupWindow) {
                mPopupWindow.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.IS_REMIND = true;
        LogUtil.ld(TAG, "MainActvity Destory~");
    }

    private class VersionRequsetListenr implements RequestManager.RequestListener {

        @Override
        public void onRequest() {
        }

        @Override
        public void onSuccess(String response, String url, int actionId) {
            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadFile = new DownloadFileUtils(downloadManager, MainActivity.this);
            VersionModel model = new VersionModel();
            model.parsingJson(response);
            LogUtil.ld(TAG, "Version Title -->" + model.getTitle());
            if (model.getIsUpdate() == 1) {
                String check_date = ps.getString(VERSION_CHECK_DATE, "");
                String today = DateUtils.get_0(null);
                //检查日期不是今天就弹出popupWindow 并且将今日 日期写入 Sharedpreferences
                if (!check_date.equals(today) || is_check_version) {
                    try {
                        showPopupWindow(model);
                        SharedPreferences.Editor editor = ps.edit();
                        editor.putString(VERSION_CHECK_DATE, DateUtils.get_0(null));
                        editor.apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //如果是手动检查更新，没有新版本 则给 一个Toast 提示
                if (is_check_version) {
                    Toast.makeText(MainActivity.this, "当前已经是最新版本了喔~", Toast.LENGTH_SHORT).show();
                }
            }
            is_check_version = false;
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            LogUtil.le(TAG, "VersionRequsetListenr Error errorMsg -->" + errorMsg);
        }
    }

    public void getAllConsumptionType() {
        final String summaryDate = ps.getString(Constants.SUMMARY_DATE_NAME, "");
        new Thread() {
            @Override
            public void run() {
                Constants.consumptionTypes = dbHelper.getConsumptionType();
                if (Constants.consumptionTypes.size() <= 0) {
                    Constants.consumptionTypes = Constants.initConsumptionType();
                    for (ConsumptionTypeModel mode : Constants.consumptionTypes) {
                        try {
                            dbHelper.insertConsumptionType(mode);
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.le(TAG, "insert ConsumptionType error ~" + e.getMessage());
                        }
                    }
                }
                ConsumptionTypeModel model = new ConsumptionTypeModel();
                model.setConsumption_type(Constants.ADD_CONSUPTION_TYP);
                model.setType_icon_select(Constants.ADD_CONSUPTION_TYP);
                model.setConsumption_name("添加类别");
                Constants.consumptionTypes.add(model);
                //首页再次检测是否汇总昨日消费
                String today = DateUtils.get_0(null);
                if (!today.equals(summaryDate) && !summaryDate.equals("")) {
                    boolean result = false;
                    try {
                        result = dbHelper.insertToday(summaryDate);
                        LogUtil.ld(TAG, "insert yesterday " + summaryDate + " count ->" + result);
                        // 将今日 日期写入文件，方便 下次汇总
                        SharedPreferences.Editor editor = ps.edit();
                        editor.putString(Constants.SUMMARY_DATE_NAME, today);
                        editor.apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }.start();
    }
//
//    @Override
//    public void getPoints(int i) {
//        LogUtil.ld(TAG, "getPoints ->" + i);
//    }
//
//    @Override
//    public void getPointsFailed(String s) {
//        LogUtil.lw(TAG, "getPointsFailed ->" + s);
//    }
//
//    @Override
//    public void earnedPoints(int i, int i2) {
//        LogUtil.ld(TAG, "earnedPoints ->" + i + " i2 ->" + i2);
//
//    }
}
