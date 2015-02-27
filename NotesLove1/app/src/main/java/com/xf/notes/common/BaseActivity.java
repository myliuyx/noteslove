package com.xf.notes.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xf.notes.R;

/**
 * Created by liuyuanxiao on 15/1/16.
 */
public abstract class BaseActivity extends FragmentActivity {
    private ImageButton imgbut_home_back;// 返回按钮
    private ImageButton main_setting_iv;//设置按钮
    private TextView main_title_name;// Titile
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnTitleClickListener titleClickListener = new OnTitleClickListener();
        LinearLayout layout = (LinearLayout) findViewById(R.id.home_fragment);
        imgbut_home_back = (ImageButton) findViewById(R.id.imgbut_home_back);
        main_setting_iv = (ImageButton) findViewById(R.id.main_setting_iv);
        main_title_name = (TextView) findViewById(R.id.main_title_name);
        imgbut_home_back.setOnClickListener(titleClickListener);
        main_setting_iv.setOnClickListener(titleClickListener);
        LayoutInflater inflater = LayoutInflater.from(this);
        View content = inflater.inflate(getContentViewId(), null);
        layout.addView(content, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        initView();
    }
    public void initView(){}
    /**
     * 左边点击时间
     * defult  finish 当前页
     * */
    public void actionBackClick(){finish();}
    /**
     *  右边点击事件
     */
    public void actionSettingClick(){}
    /**
     * 子类回调内容布局ID
     *
     * @return 布局id
     */
    protected abstract int getContentViewId();

    public void setMain_title_name(String titleName){main_title_name.setText(titleName);}

    private class OnTitleClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgbut_home_back:
                    actionBackClick();
                    break;
                case  R.id.main_setting_iv:
                    actionSettingClick();
                    break;
            }

        }
    }

    /**
     * 设置右边图标
     * @param resour
     */
    public void setMain_right_icon(int resour){
        main_setting_iv.setBackgroundResource(resour);
    }
    public void isShowRightIcon(boolean isShow){
        if(isShow){
            main_setting_iv.setVisibility(View.VISIBLE);
        }else{
            main_setting_iv.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
