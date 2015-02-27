package com.xf.notes.activity;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xf.notes.R;
import com.xf.notes.common.BaseActivity;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.AppInfoModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class APPListActivity extends BaseActivity implements AbsListView.OnScrollListener {
    private final String TAG = "APPListActivity";
    private ListView ad_list_lv;
    private ProgressBar pb_indicator;
    private List<AppInfoModel> mList = new ArrayList<AppInfoModel>();
    private int ad_page = 0;
    private APPListAdapter adapter;
    private boolean is_load = true;//是否加载数据

    @Override
    protected int getContentViewId() {
        return R.layout.adlist_activity;
    }

    @Override
    public void initView() {
        setMain_title_name("精品推荐");
        setMain_right_icon(R.drawable.refresh_title_bg);
        ad_list_lv = (ListView) findViewById(R.id.ad_list_lv);
        pb_indicator = (ProgressBar) findViewById(R.id.pb_indicator);
        adapter = new APPListAdapter(this, mList);
        ad_list_lv.setAdapter(adapter);
        ad_list_lv.setOnScrollListener(this);
//        loadADList();
    }

    /**加载广告数据
    private void loadADList() {
        DevInit.getList(APPListActivity.this, ad_page, 25, new GetAdListListener() {
            @Override
            public void getAdListSucceeded(List list) {
                LogUtil.ld(TAG, "loadADList Size ->" + list.size());
                ad_list_lv.setVisibility(View.VISIBLE);
                pb_indicator.setVisibility(View.GONE);
                mList.addAll(mList.size(), parsingAD(list));
                adapter.notifyDataSetChanged();
                ad_page++;
                is_load = true;
            }

            @Override
            public void getAdListFailed(String s) {
                if (is_load) {
                    Toast.makeText(APPListActivity.this, "亲爱的,精品列表加载失败了~", Toast.LENGTH_SHORT).show();
                }
                is_load = false;
            }
        });
    }*/

    //解析得到的广告List
    private List<AppInfoModel> parsingAD(List<HashMap<String, Object>> list) {
        List<AppInfoModel> adList = new ArrayList<AppInfoModel>();
        AppInfoModel m = null;
        for (HashMap mp : list) {
            m = new AppInfoModel();
            m.setIcon_url(String.valueOf(mp.get("icon")));
            m.setApp_name(String.valueOf(mp.get("name")));
            m.setApp_title(String.valueOf(mp.get("text")));
            m.setApp_size(String.valueOf(mp.get("size")));
            m.setApp_desc(String.valueOf(mp.get("description")));
            List<String> desc_icon = parsingDescIcon(String.valueOf(mp.get("thumbnail")));
            m.setApp_thumbnail(desc_icon);
            adList.add(m);
            m = null;
        }
        return adList;
    }

    //解析详情图片
    private List<String> parsingDescIcon(String obj) {
        List<String> l = new ArrayList<String>();
        try {
            JSONArray arry = new JSONArray(obj);
            for (int y = 0; y < arry.length(); y++) {
                l.add(arry.get(y).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l;
    }

    @Override
    public void actionSettingClick() {
        ad_list_lv.setVisibility(View.GONE);
        pb_indicator.setVisibility(View.VISIBLE);
        mList.clear();
        ad_page = 0;
        is_load = false;
//        loadADList();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        synchronized (APPListActivity.class) {
            if (lastVisibleIndex > (totalItemCount - 10) && is_load) {
                is_load = false;
//                loadADList();
            }
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
