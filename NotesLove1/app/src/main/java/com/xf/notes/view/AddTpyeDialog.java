package com.xf.notes.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xf.notes.R;
import com.xf.notes.utils.DensityUtil;

/**
 * 添加分类Dialog
 * Created by liuyuanxiao on 15/1/13.
 */
public class AddTpyeDialog extends Dialog implements View.OnClickListener {
    private AletLstener listener;
    private Context context;
    private Button ok, cancle;
    private EditText dialog_content_message;
    public AddTpyeDialog(Context context, int theme, String title) {
        super(context, theme);
        this.context = context;
        setContentView(R.layout.add_type_dialog);
        TextView dialog_title = (TextView) findViewById(R.id.dialog_title);
        dialog_content_message = (EditText) findViewById(R.id.dialog_content_message);
        ok = (Button) findViewById(R.id.dialog_yes_button);
        cancle = (Button)findViewById(R.id.dialog_no_button);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        lp.width = DensityUtil.dip2px(context, 280); // 宽度
        lp.height = ActionBar.LayoutParams.WRAP_CONTENT; // 高度
        onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(true);
        ok.setOnClickListener(this);
        cancle.setOnClickListener(this);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_yes_button:
                listener.okClick(this,dialog_content_message.getText().toString());
                break;
            case R.id.dialog_no_button:
                listener.cancelClick(this);
                break;

        }
    }
    // 设置应该响应onClick事件的接口
    public void SetListener(AletLstener listener) {
        this.listener = listener;
    }

    public interface AletLstener {
        public void cancelClick(AddTpyeDialog diaglog);

        public void okClick(AddTpyeDialog diaglog,String edt_msg);

    }
}
