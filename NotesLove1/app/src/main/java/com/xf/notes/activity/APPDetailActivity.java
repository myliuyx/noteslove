package com.xf.notes.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.umeng.analytics.MobclickAgent;
import com.xf.notes.NotesApplication;
import com.xf.notes.R;
import com.xf.notes.common.BaseActivity;
import com.xf.notes.model.AppInfoModel;

import java.util.List;

/**
 * Created by liuyuanxiao on 15/1/20.
 */
public class APPDetailActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "APPDetailActivity";
    private AppInfoModel model;
    private NetworkImageView app_icon;
    private TextView app_name;
    private TextView app_size;
    private Button install_but;
    private TextView app_describe;
    private LinearLayout horiontaSorollview;

    @Override
    protected int getContentViewId() {
        Intent intent = getIntent();
        model = (AppInfoModel) intent.getSerializableExtra("appinfo");
        isShowRightIcon(false);
        setMain_title_name("应用详情");
        return R.layout.appdatile_layout;
    }

    @Override
    public void initView() {
        app_icon = (NetworkImageView) findViewById(R.id.app_icon);
        app_name = (TextView) findViewById(R.id.app_name);
        app_size = (TextView) findViewById(R.id.app_size);
        app_describe = (TextView) findViewById(R.id.app_describe);
        install_but = (Button) findViewById(R.id.install_but);
        horiontaSorollview = (LinearLayout) findViewById(R.id.horiontaSorollview);
        setViewProperty();
        install_but.setOnClickListener(this);
    }

    private void setViewProperty() {
        if (null != model) {
            app_icon.setImageUrl(model.getIcon_url(), NotesApplication.getImageLoader());
            initHorizontalScroView();
            app_name.setText(model.getApp_name());
            app_size.setText("大小：" + model.getApp_size());
            app_describe.setText(Html.fromHtml(model.getApp_desc()));
        }
    }

    private void initHorizontalScroView() {
        List<String> urls = model.getApp_thumbnail();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (String s : urls) {
            NetworkImageView view = new NetworkImageView(this);
            view.setLayoutParams(params);
            view.setImageUrl(s, NotesApplication.getImageLoader());
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            horiontaSorollview.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.install_but){
            //下载按钮被点击了
            MobclickAgent.onEvent(this, "adclick");
//            DevInit.download(APPDetailActivity.this, model.getApp_name(),AdType.ADLIST, new AddpoinsListener(this));
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }
}
