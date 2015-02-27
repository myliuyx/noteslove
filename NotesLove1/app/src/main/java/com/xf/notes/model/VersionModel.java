package com.xf.notes.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 版本更新 Model
 * Created by liuyuanxiao on 15/1/7.
 */
public class VersionModel {
    private int isUpdate = 0;
    private int version_code;
    private String title;
    private String content;
    private String url;
    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public VersionModel parsingJson(String json){
        try {
            JSONObject jsonobect = new JSONObject(json);
            String result =  jsonobect.get("result")==null?"":jsonobect.get("result").toString();
            if (!result.equals("1"))return this;
            String title = jsonobect.get("title") == null ?"":jsonobect.get("title").toString();
            String content = jsonobect.get("content") == null ?"":jsonobect.get("content").toString();
            String version_url = jsonobect.get("version_url") == null ?"":jsonobect.get("version_url").toString();
            this.setIsUpdate(Integer.parseInt(result));
            this.setTitle(title);
            this.setContent(content);
            this.setUrl(version_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
