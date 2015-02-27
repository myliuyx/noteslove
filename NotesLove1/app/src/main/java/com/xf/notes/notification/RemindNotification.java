package com.xf.notes.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

import com.xf.notes.MainActivity;
import com.xf.notes.R;

/**
 * 通知Notification处理类
 * Created by liuyuanxiao on 14/12/31.
 */
public class RemindNotification {
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent mPendingIntent;
    private Context mContext;
    private static RemindNotification  mRemindNotification;
    public static synchronized RemindNotification getInstances(Context context){
        if (null == mRemindNotification){
            mRemindNotification = new RemindNotification(context);
        }
        return mRemindNotification;
    }
    private RemindNotification(Context context){
        this.mContext = context;
        String service = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) context.getSystemService(service);
        Intent intent = new Intent(context, MainActivity.class);
        mPendingIntent = PendingIntent.getActivity(context,0,intent,0);
    }
    public void showNotification(String msg){
        mNotification = new NotificationCompat.Builder(mContext)
                .setContentTitle("记账提醒")
                .setContentText(msg).setContentIntent(mPendingIntent)
                .setSmallIcon(R.drawable.ic_launcher).build();
        mNotification.tickerText = msg;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotification.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6");
//        mNotification.defaults = Notification.DEFAULT_SOUND;
        mNotificationManager.notify(0,mNotification);
    }
}
