package com.xf.notes.model;

import java.io.Serializable;

import com.xf.notes.utils.DateUtils;

public class ConsumptionModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int _id;
	private int consumption_type;//消费类型
	private String consumption_name;//消费类型名称
	private double consumption_number;//消费金额
	private String consumption_desc;//消费备注
	private String consumption_year = DateUtils.get_1(null);//消费所属年
	private String consumption_moth = DateUtils.get_6(null);//消费所属月
	private String consumption_date = DateUtils.get_0(null);//消费日期
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
	public double getConsumption_number() {
		return consumption_number;
	}
	public void setConsumption_number(double consumption_number) {
		this.consumption_number = consumption_number;
	}
	public String getConsumption_desc() {
		return consumption_desc;
	}
	public void setConsumption_desc(String consumption_desc) {
		this.consumption_desc = consumption_desc;
	}
	public String getConsumption_year() {
		return consumption_year;
	}
	public void setConsumption_year(String consumption_year) {
		this.consumption_year = consumption_year;
	}
	public String getConsumption_moth() {
		return consumption_moth;
	}
	public void setConsumption_moth(String consumption_moth) {
		this.consumption_moth = consumption_moth;
	}
	public String getConsumption_date() {
		return consumption_date;
	}
	public void setConsumption_date(String consumption_date) {
		this.consumption_date = consumption_date;
	}

    @Override
    public String toString() {
        return "ConsumptionModel{" +
                ", consumption_type=" + consumption_type +
                ", consumption_name='" + consumption_name + '\'' +
                ", consumption_number=" + consumption_number +
                ", consumption_desc='" + consumption_desc + '\'' +
                ", consumption_year='" + consumption_year + '\'' +
                ", consumption_moth='" + consumption_moth + '\'' +
                ", consumption_date='" + consumption_date + '\'' +
                '}';
    }
}
