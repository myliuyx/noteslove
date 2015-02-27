package com.xf.notes.frament;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.DBhelper.DBHelper;
import com.xf.notes.MainActivity;
import com.xf.notes.R;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.BackUpFileModel;
import com.xf.notes.utils.DateUtils;
import com.xf.notes.utils.FileManager;
import com.xf.notes.view.BackupDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuyuanxiao on 15/1/16.
 */
public class BackUpFragment extends Fragment implements View.OnClickListener, Handler.Callback {
    private final String TAG = "BackUpFragment";
    private MainActivity activity;// 父Activity
    private File DB_DIRECTORY;//data/data目录 数据库位置
    private File backup_dir;// SD卡存储根目录位置
    private ConsumptionHelper helper;
    private Button location_back_bt;//本地备份按钮
    private ListView backup_list_lv;
    private LinearLayout no_backup_layout;// 没有备份 Layout
    private LinearLayout backup_list_layou;// 显示备份Layout
    private Handler handler = new Handler(this);
    private final int BACKUP_SUCCESS = 1;//备份成功
    private final int BACKUP_ERROR = -1;//备份失败
    private List<BackUpFileModel> backList;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case BACKUP_SUCCESS:
                showListOrNoBackup(getLocaBackup());
                Toast.makeText(activity, "备份成功~", Toast.LENGTH_SHORT).show();
                break;
            case BACKUP_ERROR:
                Toast.makeText(activity, "备份失败~", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = (MainActivity) activity;
        this.activity.isHomeFragment = false;
        this.activity.IS_FRAGMENT = this.activity.BACKUP_FRAGMENT;
        this.activity.isShowBackArrow();
        this.activity.setTitileNma("数据备份");
        helper = ConsumptionHelper.getInstance(activity);
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.backup_fragment, container, false);
        DB_DIRECTORY = activity.getDatabasePath(DBHelper.DATABASE_NAME);
        backup_dir = Environment.getExternalStorageDirectory();
        LogUtil.ld(TAG, "backup_dir Path -->" + backup_dir.getPath());
        initView(rootView);
        return rootView;
    }

    private void initView(View v) {
        location_back_bt = (Button) v.findViewById(R.id.location_back_bt);
        backup_list_lv = (ListView) v.findViewById(R.id.backup_list_lv);
        no_backup_layout = (LinearLayout) v.findViewById(R.id.no_backup_layout);
        backup_list_layou = (LinearLayout) v.findViewById(R.id.backup_list_layou);
        location_back_bt.setOnClickListener(this);
        backList = getLocaBackup();
        showListOrNoBackup(backList);
        //ListView Item 点击事件
        backup_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    BackUpFileModel model = backList.get(position);
                    BackupDialog dialg = new BackupDialog(activity, R.style.varDialogTheme, model);
                    dialg.setRestoreListener(new RestoreListenr());
                    dialg.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class RestoreListenr implements BackupDialog.RestoreBackUpListener {

        @Override
        public void restoreBackup(File f) {
            LogUtil.ld(TAG, "restoreBackup File path -->" + f.getPath());
            try {
                reductionDB(f);
                //删除当日汇总
                helper.deleteSummaryDate(DateUtils.get_0(null));
                activity.startFragment(new HomeFragment(), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deleteBackup(File f) {
            LogUtil.ld(TAG, "deleteBackup File path -->" + f.getPath());
            f.delete();
            showListOrNoBackup(getLocaBackup());
        }
    }

    //是否显示没有备份或者List
    private void showListOrNoBackup(List<BackUpFileModel> backList) {
        if (backList != null && backList.size() > 0) {
            backup_list_layou.setVisibility(View.VISIBLE);
            no_backup_layout.setVisibility(View.GONE);
            setAdapter(backList);
        } else {
            backup_list_layou.setVisibility(View.GONE);
            no_backup_layout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置Adapter
     */
    private void setAdapter(List<BackUpFileModel> backList) {
        BackUpListAdapter adapter = new BackUpListAdapter(backList);
        backup_list_lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.location_back_bt) {
            new Thread() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    try {
                        //备份前将今日 汇总信息 统计出来
                        String today = DateUtils.get_0(null);
                        helper.insertToday(today);
                        File backFile = generateBackUpDir("Notes_" + DateUtils.get_0(null) + ".db");
                        backDB(backFile);
                        //备份结束将 今日汇总删除
                        helper.deleteSummaryDate(today);
                        msg.what = BACKUP_SUCCESS;
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.le(TAG, e.getMessage());
                        msg.what = BACKUP_ERROR;
                    }
                    handler.sendMessage(msg);
                }
            }.start();
        }
    }

    //获取本地所有的备份文件
    private List<BackUpFileModel> getLocaBackup() {
        List<BackUpFileModel> backModels = null;
        File backup_path = new File(backup_dir, Constants.DB_BACKUP_DIR);
        LogUtil.ld(TAG, "backup_path PATH -->" + backup_path.getPath());
        if (!backup_path.exists()) {
            backup_path.mkdirs();
        }
        File[] files = backup_path.listFiles();
        if (null != files && files.length > 0) {
            backModels = new ArrayList<BackUpFileModel>();
            BackUpFileModel model = null;
            for (File f : files) {
                model = new BackUpFileModel();
                model.setFileName(f.getName());
                model.setCreateTime(DateUtils.get_7(new Date(f.lastModified())));
                float size = f.length() / 1024;
                model.setFileSize(String.valueOf(size) + "KB");
                model.setFile(f);
                backModels.add(model);
                LogUtil.ld(TAG, model.toString());
                model = null;
            }
        }
        return backModels;
    }

    /**
     * 备份本地数据库
     *
     * @throws IOException
     */
    private void backDB(File backFile) throws IOException {
        FileManager m = new FileManager();
//        File backFile = generateBackUpDir();
        if (backFile.exists()) {
            backFile.delete();
        }
        backFile.createNewFile();
        m.backUPDB(DB_DIRECTORY, backFile);
    }

    /**
     * 还原本地数据库
     *
     * @throws IOException
     */
    private void reductionDB(File backFile) throws IOException {
        FileManager m = new FileManager();
        m.reductionDB(DB_DIRECTORY, backFile);
        helper.restartDB();
        activity.getAllConsumptionType();
    }

    //生成备份目录并返回File
    private File generateBackUpDir(String fileName) throws IOException {
        File backup_path = new File(backup_dir, Constants.DB_BACKUP_DIR);
        if (!backup_path.exists()) {
            backup_path.mkdirs();
        }
        File newFile = new File(backup_path, fileName);
        return newFile;
    }

    /**
     * *********************************************
     * ***********************备份列表List 适配器************
     * **********************************************
     */
    private class BackUpListAdapter extends BaseAdapter {
        private List<BackUpFileModel> backList;
        private LayoutInflater mInflater;

        BackUpListAdapter(List<BackUpFileModel> backList) {
            this.backList = backList;
            mInflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return backList.size();
        }

        @Override
        public Object getItem(int position) {
            return backList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (v == null) {
                v = mInflater.inflate(R.layout.backup_list_item, null);
                viewHolder.fileName = (TextView) v.findViewById(R.id.backup_file_name);
                viewHolder.fileSize = (TextView) v.findViewById(R.id.backup_size);
                viewHolder.createTime = (TextView) v.findViewById(R.id.backup_date);
                v.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) v.getTag();
            }
            BackUpFileModel model = backList.get(position);
            viewHolder.fileName.setText(model.getFileName());
            viewHolder.createTime.setText(model.getCreateTime());
            viewHolder.fileSize.setText(model.getFileSize());
            return v;
        }
    }

    class ViewHolder {
        TextView fileName;
        TextView fileSize;
        TextView createTime;
    }
}
