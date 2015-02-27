package com.xf.notes;

import android.app.Application;

import com.android.volley.http.RequestManager;
import com.android.volley.toolbox.ImageLoader;
import com.xf.notes.utils.NetworkImageCache;

/**
 * Created by liuyuanxiao on 15/1/20.
 */
public class NotesApplication extends Application {
    private static ImageLoader sImageLoader = null;

    private final NetworkImageCache imageCacheMap = new NetworkImageCache();
    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.getInstance().init(NotesApplication.this);
        sImageLoader = new ImageLoader(RequestManager.getInstance()
                .getRequestQueue(), imageCacheMap);
    }
    public static ImageLoader getImageLoader() {
        return sImageLoader;
    }
}
