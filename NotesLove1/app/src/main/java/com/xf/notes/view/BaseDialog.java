package com.xf.notes.view;

import com.xf.notes.R;
import com.xf.notes.utils.DensityUtil;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 自定义Dialog
 * 
 * @author Administrator
 * 
 */
public class BaseDialog extends Dialog implements OnClickListener {

	private AletLstener listener;
	private Button ok, cancle;
	private Context context;
	public BaseDialog(Context context, int theme, String title, String msg) {
		super(context, theme);
		this.context = context;
		setContentView(R.layout.base_dialog);
		TextView dialog_title = (TextView) findViewById(R.id.dialog_title);
		TextView dialog_content_message = (TextView) findViewById(R.id.dialog_content_message);
		if (null != title) {
			dialog_title.setText(title);
		}
		if (null != msg) {
			dialog_content_message.setText(msg);
		}
		ok = (Button) findViewById(R.id.dialog_yes_button);
		cancle = (Button)findViewById(R.id.dialog_no_button);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window dialogWindow = getWindow();       
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.CENTER);
		
		lp.width = DensityUtil.dip2px(context, 280); // 宽度
		lp.height = LayoutParams.WRAP_CONTENT; // 高度
//		lp.alpha = 0.7f; // 透明度
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
			listener.okClick(this);
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
		public void cancelClick(BaseDialog diaglog);

		public void okClick(BaseDialog diaglog);

	}
}
