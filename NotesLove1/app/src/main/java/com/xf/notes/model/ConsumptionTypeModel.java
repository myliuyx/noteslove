package com.xf.notes.model;

/**
 * 消费类型 Model
 * Created by liuyuanxiao on 15/1/8.
 */
public class ConsumptionTypeModel {
    /**未选择状态*/
    public static final int TYPE_NO_CHECKD = 0;
    /**默认状态*/
    public static final int TYPE_DEFULT = 1;
    /**选择状态*/
    public static final int TYPE_CHECKD = 2;
    private int _id;
    private int consumption_type;// 消费类型
    private String consumption_name;//消费名称
    private int type_icon_select;//选中时的照片
    private int isCheckd = TYPE_DEFULT;// 是否选中

    public int getIsCheckd() {
        return isCheckd;
    }

    public void setIsCheckd(int isCheckd) {
        this.isCheckd = isCheckd;
    }

    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }

    public int getConsumption_type() {
        return consumption_type;
    }

    public void setConsumption_type(int consumption_type) {
        this.consumption_type = consumption_type;
    }

    public String getConsumption_name() {
        return consumption_name;
    }

    public void setConsumption_name(String consumption_name) {
        this.consumption_name = consumption_name;
    }
    public int getType_icon_select() {
        return type_icon_select;
    }

    public void setType_icon_select(int type_icon_select) {
        this.type_icon_select = type_icon_select;
    }
}
