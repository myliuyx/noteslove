package com.xf.notes.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 消费详细类
 * @author liuyuanxiao
 *
 */
public class ConsumptionDetaileModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int consumption_type;// 消费类型
	private String consumption_name;//消费名称
	private Double consumption_count;//消费金额
//	private int isCheckd = TYPE_DEFULT;// 是否选中
//	public int getIsCheckd() {
//		return isCheckd;
//	}
//	public void setIsCheckd(int isCheckd) {
//		this.isCheckd = isCheckd;
//	}
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
	public Double getConsumption_count() {
        BigDecimal b = new BigDecimal(consumption_count);
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}
	public void setConsumption_count(Double consumption_count) {
		this.consumption_count = consumption_count;
	}
}
