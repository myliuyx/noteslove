package com.xf.notes.frament;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.http.RequestManager;
import com.xf.notes.MainActivity;
import com.xf.notes.R;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 意见反馈
 * Created by liuyuanxiao on 15/1/16.
 */
public class FeedBackFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "FeedBackFragment";
    private MainActivity activity;// 父Activity
    private EditText contact_et;
    private EditText desc_ed;
    private Button submit_feedback;
    @Override
    public void onAttach(Activity activity) {
        this.activity = (MainActivity) activity;
        this.activity.isHomeFragment = false;
        this.activity.IS_FRAGMENT = this.activity.FEEDBACK_FRAGMENT;
        this.activity.isShowBackArrow();
        this.activity.setTitileNma("意见反馈");
        super.onAttach(activity);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_feed_back, container,false);
        initView(rootView);
        return rootView;
    }
    private void initView(View v){
        contact_et = (EditText) v.findViewById(R.id.contact_et);
        desc_ed = (EditText) v.findViewById(R.id.desc_ed);
        submit_feedback = (Button) v.findViewById(R.id.submit_feedback);
        submit_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_feedback){
            if (!NetUtils.checkNetWorkStatus(activity)) {
                Toast.makeText(activity,"当前网络情况不太好~",Toast.LENGTH_SHORT).show();
                return ;
            }
            String contact = contact_et.getText().toString();
            String feed_desc = desc_ed.getText().toString();
            if(null == feed_desc || feed_desc.equals("")){
                Toast.makeText(activity,"亲,您需要写一些意见才能提交的哦~",Toast.LENGTH_SHORT).show();
                return;
            }
            String param = feedBackParam(contact,feed_desc,"无");
            String url = Constants.HOST_URL + Constants.INTERFACE_FEED_BACK;
            RequestManager.getInstance().post(url, param, new FeedbackRequsetListenr(), 0);
            Toast.makeText(activity,"已收录,非常感谢您的宝贵意见!",Toast.LENGTH_SHORT).show();
            activity.IS_FRAGMENT = -1;
            activity.startFragment(new HomeFragment(),null);
        }
    }
    //组合 接口参数
    private String feedBackParam(String contact,String feed_desc,String mobile){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Constants.TOKEN);
            jsonObject.put("packagename", activity.getPackageName());
            jsonObject.put("mobile", mobile);
            jsonObject.put("email", contact);
            jsonObject.put("content", feed_desc);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**意见反馈回调接口*/
    private class FeedbackRequsetListenr implements RequestManager.RequestListener {
        @Override
        public void onSuccess(String response, String url, int actionId) {
            LogUtil.ld(TAG,"FeedBack visit Success ");
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            LogUtil.ld(TAG,"FeedBack visit Error ");
        }
        @Override
        public void onRequest() {

        }
    }
}
