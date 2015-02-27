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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xf.notes.R;
import com.xf.notes.model.BackUpFileModel;
import com.xf.notes.utils.DensityUtil;

import java.io.File;

/**
 * 备份弹出Dialog
 * Created by liuyuanxiao on 15/1/18.
 */
public class BackupDialog extends Dialog implements View.OnClickListener {
    private RestoreBackUpListener restoreListener;
    private Context context;
    private BackUpFileModel model;
    private TextView backup_msg_tv;//提示消息
    private LinearLayout select_list_layout;//初次进入3个选项
    private Button restore_backup_bt;// 恢复按钮
    private Button delete_backup_bt;//删除按钮
    private Button desc_backup_bt;//详细按钮
    private Button cancel_backup_bt;//取消按钮
    private Button ok_backup_bt;//确定按钮
    /**选择删除*/
    private final int SELECT_DELETE = 1;
    /**选择恢复*/
    private final int SELECT_RESTORE = 2;
    /**当前是什么选择 (删除，恢复)*/
    private int SELECT_BUTTION = -1;
    public BackupDialog(Context context, int theme,BackUpFileModel model) {
        super(context, theme);
        this.context = context;
        setContentView(R.layout.backup_dialog_layout);
        this.model = model;
        initView();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

//        lp.width = ActionBar.LayoutParams.MATCH_PARENT; // 宽度
        lp.width = DensityUtil.dip2px(context, 280); // 宽度
        lp.height = ActionBar.LayoutParams.WRAP_CONTENT; // 高度
//		lp.alpha = 0.7f; // 透明度
        onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(true);
        super.onCreate(savedInstanceState);
    }
    private void initView(){
        backup_msg_tv = (TextView) findViewById(R.id.backup_msg_tv);
        select_list_layout = (LinearLayout) findViewById(R.id.select_list_layout);
        restore_backup_bt = (Button) findViewById(R.id.restore_backup_bt);
        delete_backup_bt = (Button) findViewById(R.id.delete_backup_bt);
        desc_backup_bt = (Button) findViewById(R.id.desc_backup_bt);
        cancel_backup_bt = (Button) findViewById(R.id.cancel_backup_bt);
        ok_backup_bt = (Button) findViewById(R.id.ok_backup_bt);
        restore_backup_bt.setOnClickListener(this);
        delete_backup_bt.setOnClickListener(this);
        desc_backup_bt.setOnClickListener(this);
        cancel_backup_bt.setOnClickListener(this);
        ok_backup_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restore_backup_bt:
                deleteOrRestoreBackup(SELECT_RESTORE);
                break;
            case R.id.delete_backup_bt:
                deleteOrRestoreBackup(SELECT_DELETE);
                break;
            case R.id.desc_backup_bt:
                descBackup();
                break;
            case R.id.cancel_backup_bt:
                BackupDialog.this.dismiss();
                break;
            case R.id.ok_backup_bt:
                //如果未这只 监听 抛出异常
                if (null == restoreListener){throw new RuntimeException("您需要 restoreListener()~");}
                if (SELECT_BUTTION == SELECT_DELETE){
                    //删除备份
                    restoreListener.deleteBackup(model.getFile());
                }else if(SELECT_BUTTION == SELECT_RESTORE){
                    //恢复备份
                    restoreListener.restoreBackup(model.getFile());
                }
                dismiss();
                break;
        }
    }
    //删除 和 恢复 操作
    private void deleteOrRestoreBackup(int select_bt){
        select_list_layout.setVisibility(View.GONE);
        backup_msg_tv.setVisibility(View.VISIBLE);
        ok_backup_bt.setVisibility(View.VISIBLE);
        if(select_bt == SELECT_DELETE){
            backup_msg_tv.setText("您确定要删除此备份文件吗？");
            SELECT_BUTTION = SELECT_DELETE;
        }else if(select_bt == SELECT_RESTORE){
            backup_msg_tv.setText("您确定要恢复此备份文件吗？\n 注意:恢复将覆盖当前所有数据~");
            SELECT_BUTTION = SELECT_RESTORE;
        }
    }
    private void descBackup(){
        select_list_layout.setVisibility(View.GONE);
        backup_msg_tv.setVisibility(View.VISIBLE);
        String desc = "文件名："+model.getFileName()
                +"\n文件大小："+model.getFileSize()
                +"\n备份时间："+model.getCreateTime()
                +"\n路径："+model.getFile().getParent();
        backup_msg_tv.setText(desc);
        cancel_backup_bt.setText("确定");
    }
    public void setRestoreListener(RestoreBackUpListener restoreListener){
        this.restoreListener = restoreListener;
    }
    public interface RestoreBackUpListener{
        public void restoreBackup(File f);
        public void deleteBackup(File f);
    }
}
