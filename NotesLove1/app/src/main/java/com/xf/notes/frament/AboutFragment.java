package com.xf.notes.frament;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.MainActivity;
import com.xf.notes.R;

/**
 * 关于我们
 * Created by liuyuanxiao on 15/1/16.
 */
public class AboutFragment extends Fragment {
    private MainActivity activity;// 父Activity
    @Override
    public void onAttach(Activity activity) {
        this.activity = (MainActivity) activity;
        this.activity.isHomeFragment = false;
        this.activity.IS_FRAGMENT = this.activity.ABOUT_FRAGMENT;
        this.activity.isShowBackArrow();
        this.activity.setTitileNma("关于我们");
        super.onAttach(activity);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container,false);
        return rootView;
    }
}
