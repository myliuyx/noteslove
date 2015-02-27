package com.xf.notes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuyuanxiao on 15/1/19.
 */
public class AppInfoModel implements Serializable{
    private String app_name;
    private String app_title;
    private String icon_url;
    private String app_size;
    private String app_desc;
    private List<String> app_thumbnail;

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_title() {
        return app_title;
    }

    public void setApp_title(String app_title) {
        this.app_title = app_title;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getApp_size() {
        return app_size;
    }

    public void setApp_size(String app_size) {
        this.app_size = app_size;
    }

    public String getApp_desc() {
        return app_desc;
    }

    public void setApp_desc(String app_desc) {
        this.app_desc = app_desc;
    }

    public List<String> getApp_thumbnail() {
        return app_thumbnail;
    }

    public void setApp_thumbnail(List<String> app_thumbnail) {
        this.app_thumbnail = app_thumbnail;
    }

    @Override
    public String toString() {
        return "AppInfoModel{" +
                "app_name='" + app_name + '\'' +
                ", app_title='" + app_title + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", app_size='" + app_size + '\'' +
                ", app_desc='" + app_desc + '\'' +
                ", app_thumbnail=" + app_thumbnail +
                '}';
    }
}
