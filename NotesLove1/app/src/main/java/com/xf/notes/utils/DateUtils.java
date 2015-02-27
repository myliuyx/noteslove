package com.xf.notes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
	/**
	 * yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String get_0(Date date) {
		String ss = "yyyy-MM-dd";
		if (date == null) {date = new Date();}
		java.text.DateFormat df = new SimpleDateFormat(ss);
		return get_(date, df);
	}

	/**
	 * yyyy-MM
	 * 
	 * @return
	 */
	public static String get_6(Date date) {
		String ss = "yyyy-MM";
		java.text.DateFormat df = new SimpleDateFormat(ss);
		return get_(date, df);
	}

	private static String get_(Date date, java.text.DateFormat df) {
		if (date != null) {
			String s = df.format(date);
			return s;
		} else {
			String s = df.format(new Date());
			return s;
		}

	}
	/**
	 * yyyy
	 * 
	 * @return
	 */
	public static String get_1(Date date) {
		String ss = "yyyy";
		java.text.DateFormat df = new SimpleDateFormat(ss);
		return get_(date, df);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String get_3(Date date) {
		String ss = "yyyy-MM-dd HH:mm:ss";
		java.text.DateFormat df = new SimpleDateFormat(ss);
        if (date == null){date = new Date();}
		String s = df.format(date);
		return s;
	}
    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String get_7(Date date) {
        String ss = "yyyy-MM-dd HH:mm";
        java.text.DateFormat df = new SimpleDateFormat(ss);
        if (date == null){date = new Date();}
        String s = df.format(date);
        return s;
    }
	public static String get_4() {
		String ss = "HH:mm:ss";
		java.text.DateFormat df = new SimpleDateFormat(ss);
		String s = df.format(new Date());
		return s;
	}
	/**
	 * 获取时间 HH：mm
	 * 
	 * @param time
	 * @return
	 */
	public static String get_8(long time) {
		String ss = "HH:mm";
		java.text.DateFormat df = new SimpleDateFormat(ss);
		if (time <= 0) {
			time = System.currentTimeMillis();
		}
		String s = df.format(new Date(time));
		return s;
	}
	/**
	 * 将传入的String 转换成 日期   yyyy-MM-dd
	 * @param datestr
	 * @return
	 */
	public static Date get_s2d(String datestr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(datestr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
