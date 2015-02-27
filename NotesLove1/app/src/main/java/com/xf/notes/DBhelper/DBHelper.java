package com.xf.notes.DBhelper;

import com.xf.notes.common.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 本地数据库操作类
 * 
 * @author liuyuanxiao
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	private final String TAG = "ConsumptionHelper";
	public static final String DATABASE_NAME = "notes.db";
	private static final int DATABASE_VERSION = 12;
    public static final String consumptiontable = "CONSUMPTION_DETAILED";
    public static final String summaryTable = "CONSUMPTION_SUMMARY";
    public static final String consumptionTypeTable = "CONSUMPTION_TYPE";
    public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	/*
	 *  流水记录表
	 * "consumption_type INTEGER	  消费类型
	 * "consumption_name VARCHAR(20)  类型名称
	 * "consumption_number double	  金额
	 * "consumption_desc VARCHAR(100) 备注
	 * "consumption_years VARCHAR(10) 年份
	 * "consumption_moth VARCHAR(10)  月份
	 * "consumption_day VARCHAR(10)	  某日
	 * "consumption_date VARCHAR(20)  年月日
	 */
		String detaileSql = "CREATE TABLE IF NOT EXISTS " + consumptiontable + ""
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "consumption_type INTEGER," 
				+ "consumption_name VARCHAR(20),"
				+ "consumption_number double,"
				+ "consumption_desc VARCHAR(100),"
				+ "consumption_years VARCHAR(10),"
				+ "consumption_moth VARCHAR(10),"
				+ "consumption_date VARCHAR(20),"
				+ "consumption_datetime VARCHAR(40))";
        /**汇总明细表*/
		String sumSQL = "CREATE TABLE IF NOT EXISTS " + summaryTable + ""
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "colthing double,"
				+ "food double,"
				+ "have_meal double,"
				+ "miscellaneous double,"
				+ "necessities double,"
				+ "ruits_vegetables double,"
				+ "traffic double,"
                + "custom double,"
				+ "consumption_years VARCHAR(10),"
				+ "consumption_moth VARCHAR(10),"
				+ "consumption_date VARCHAR(20))";
        /**消费类型表*/
        String consumptionTypeSQL = "CREATE TABLE IF NOT EXISTS " + consumptionTypeTable + ""
                +"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "consumption_name VARCHAR(20),"
                + "consumption_id INTEGER,"
                + "type_icon_select INTEGER)" ;
		db.execSQL(sumSQL);
		db.execSQL(detaileSql);
        db.execSQL(consumptionTypeSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS UPLOAD_QUEUE"+ consumptiontable + "");
		db.execSQL("DROP TABLE IF EXISTS UPLOAD_QUEUE_DETAIL"+ summaryTable +"");
        db.execSQL("ALTER TABLE "+summaryTable+" ADD custom double");

        /**消费类型表*/
        String consumptionTypeSQL = "CREATE TABLE IF NOT EXISTS " + consumptionTypeTable + ""
                +"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "consumption_name VARCHAR(20),"
                + "consumption_id INTEGER,"
                + "type_icon_defult INTEGER,"
                + "type_icon_select INTEGER)" ;
        db.execSQL(consumptionTypeSQL);
	}

}
