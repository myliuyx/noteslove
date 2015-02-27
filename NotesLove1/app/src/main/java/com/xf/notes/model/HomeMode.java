package com.xf.notes.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class HomeMode implements Serializable {
	private static final long serialVersionUID = -1420414796357628663L;
	private Double todayCount = 0.0;
	private Double yesterdayCount = 0.0;
	private Double thisMothCount = 0.0;
	private List<ConsumptionDetaileModel> yesterdayDetailed;
	private List<ConsumptionDetaileModel> thisMothDetailed;

	public List<ConsumptionDetaileModel> getYesterdayDetailed() {
		return yesterdayDetailed;
	}

	public void setYesterdayDetailed(
			List<ConsumptionDetaileModel> yesterdayDetailed) {
		this.yesterdayDetailed = yesterdayDetailed;
	}

	public List<ConsumptionDetaileModel> getThisMothDetailed() {
		return thisMothDetailed;
	}

	public void setThisMothDetailed(
			List<ConsumptionDetaileModel> thisMothDetailed) {
		this.thisMothDetailed = thisMothDetailed;
	}

	public Object getTodayCount() {
		if (isDouble(todayCount)) {
			return todayCount;
		} else {
			return todayCount.intValue();
		}
	}

	public void setTodayCount(double todayCount) {
		this.todayCount = todayCount;
	}

	public Object getYesterdayCount() {
		if (isDouble(yesterdayCount)) {
			return yesterdayCount;
		} else {
			return yesterdayCount.intValue();
		}
	}

	public void setYesterdayCount(double yesterdayCount) {
		this.yesterdayCount = yesterdayCount;
	}

	public Object getThisMothCount() {
		if (isDouble(thisMothCount)) {
			return thisMothCount;
		} else {
			return thisMothCount.intValue();
		}
	}

	public void setThisMothCount(double thisMothCount) {
		this.thisMothCount = thisMothCount;
	}

	/**
	 * 判断传入参数是否是 double 类型
	 * 
	 * @param d
	 * @return
	 */
	public static boolean isDouble(Double d) {
		int i = d.intValue();
		if (d > i) {
			return true;
		} else {
			return false;
		}
	}
}
