package com.xf.notes.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.util.Log;

import com.xf.notes.common.LogUtil;

public class FileManager {
    private final String TAG ="FileManager";
    //还原
    public void reductionDB(File dbFile,File backFile){
        try {
            if (dbFile.exists()){
                dbFile.delete();
                dbFile.createNewFile();
            }
            fileCopy(backFile,dbFile);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.le(TAG,e.getMessage());
        }
    }
    //备份
    public void backUPDB(File dbFile,File backFile)  {
        try {
            backFile.createNewFile();
            fileCopy(dbFile,backFile);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.le(TAG,e.getMessage());
        }
    }
    private void fileCopy(File dbFile, File backup) throws IOException {
        Log.e("FileManager", "dbFile path -->"+dbFile.getPath());
        Log.e("FileManager", "backup path -->"+backup.getPath());
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}