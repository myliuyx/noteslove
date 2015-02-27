package com.xf.notes.model;

import java.io.File;

/**
 * Created by liuyuanxiao on 15/1/18.
 */
public class BackUpFileModel {
    private String fileName;
    private String createTime;
    private String fileSize;
    private File file;
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "BackUpFileModel{" +
                "fileName='" + fileName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", fileSize='" + fileSize + '\'' +
                '}';
    }
}
