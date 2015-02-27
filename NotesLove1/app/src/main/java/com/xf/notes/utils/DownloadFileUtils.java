package com.xf.notes.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.Tag;
import android.widget.Toast;

import com.xf.notes.common.LogUtil;

import java.io.File;

/**
 * Created by liuyuanxiao on 15/1/7.
 */
public class DownloadFileUtils {
    private final String TAG = "DownloadFileUtils";
    private final String INTENT_TYPE = "application/vnd.android.package-archive";
    private DownloadManager downloadManager;
    private Context context;
    public DownloadFileUtils(DownloadManager downloadManager,Context context){
        this.downloadManager = downloadManager;
        this.context = context;
    }
    public boolean downloadFile(String url,String title,String content,String path){
        boolean result = false;
        try {
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(title);
            request.setDescription(content);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                LogUtil.ld(TAG, "APK 存在。。");
            }
            request.setDestinationUri(Uri.fromFile(file));
            long downlodeReference = downloadManager.enqueue(request);
            operationAPK(file, downlodeReference, context);
            result = true;
        } catch (Exception e) {
            Toast.makeText(context, "发生了一点点小意外,下载失败了~",
                    Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;

    }
    /**
     * 下载完成操作APK
     */
    public void operationAPK(final File file, final long downlodeReference,Context context) {

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//		filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        LogUtil.ld(TAG, "File path : " + file.getPath());
        BroadcastReceiver receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                LogUtil.ld(TAG, "Click Install APK IF ");
                long reference = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downlodeReference == reference) {
                    LogUtil.ld(TAG, "Download complete~");
                    Intent inten = new Intent();
                    inten.setAction(Intent.ACTION_VIEW);
                    inten.setDataAndType(Uri.fromFile(file),INTENT_TYPE);
                    context.startActivity(inten);
                }
            }
        };
        context.registerReceiver(receiver, filter);
    }
}
