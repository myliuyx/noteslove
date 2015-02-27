package com.xf.notes.DBhelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.model.ConsumptionDetaileModel;
import com.xf.notes.model.ConsumptionModel;
import com.xf.notes.model.ConsumptionTypeModel;
import com.xf.notes.model.SummaryModel;
import com.xf.notes.utils.DateUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 消费记录本地数据库处理类
 *
 * @author liuyuanxiao
 */
public class ConsumptionHelper {
    private final String TAG = "ConsumptionHelper";
    /**
     * 按天查询
     */
    public static int QUERY_DAY = 1;
    /**
     * 按月查询
     */
    public static int QUERY_MOTH = 2;
    /**
     * 按年查询
     */
    public static int QUERY_YEAR = 3;
    /**
     * 无查询条件
     */
    public static int QUERY_NULL = -999;
    /**
     * 名称
     */
    public static int SOTRING_NAME = 4;
    /**
     * 时间排序
     */
    public static int SOTRING_TIME = 5;

    public static ConsumptionHelper mInstance = null;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    // 消费明细表
    private final String CONSUMPUTIONTABLE = DBHelper.consumptiontable;
    // 消费汇总表
    private final String SUMMARY_TABLE = DBHelper.summaryTable;
    //消费类型表
    private final String CONSUMPTION_TYPE = DBHelper.consumptionTypeTable;
    //    private final String
    private Context context;

    // 需要查询字段的SQL
    private ConsumptionHelper(Context context) {
        super();
        this.context = context;
        // this.mContext = context;
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static ConsumptionHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConsumptionHelper(context);
        }
        return mInstance;
    }

    public void restartDB() {
        if (db.isOpen()) {
            db.close();
        }
        dbHelper.close();
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 流水表查询SQL
     *
     * @return
     */
    private String geDetaileFieldSQL() {
        String mSQL = "SELECT " + "_id," + "consumption_type,"
                + "consumption_name," + "consumption_number,"
                + "consumption_desc," + "consumption_date " + "FROM "
                + CONSUMPUTIONTABLE + " ";
        return mSQL;
    }

    private String getSummrayFieldSQL() {
        String mSQL = "SELECT " + "_id," + "colthing," + "food," + "have_meal,"
                + "miscellaneous," + "necessities," + "ruits_vegetables,"
                + "traffic,custom,"
                + "consumption_years,consumption_moth,consumption_date"
                + " FROM " + SUMMARY_TABLE + " ";
        return mSQL;
    }

    /**
     * 添加一条消费信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public boolean addConsumption(ConsumptionModel model) throws Exception {
        boolean result = false;
//        db.beginTransaction();
        LogUtil.ld(TAG,"Add Consumption -->"+model.toString());
        try {
            ContentValues values = new ContentValues();
            values.put("consumption_type", model.getConsumption_type());
            values.put("consumption_name", model.getConsumption_name());
            values.put("consumption_desc", model.getConsumption_desc());
            values.put("consumption_number", model.getConsumption_number());
            values.put("consumption_years", model.getConsumption_year());
            values.put("consumption_moth", model.getConsumption_moth());
            values.put("consumption_date", model.getConsumption_date());
            values.put("consumption_datetime", DateUtils.get_3(null));
            db.insert(CONSUMPUTIONTABLE, null, values);
//            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            throw e;
        } finally {
//            db.endTransaction();
        }
        return result;
    }

    /**
     * 删除 CONSUMPTION_DETAILED 表里某条数据
     *
     * @param _id 删除的ID
     * @return
     */
    public boolean deleteConsumption(int _id) {
        try {
            db.delete(CONSUMPUTIONTABLE, "_id = ?",
                    new String[]{String.valueOf(_id)});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询消费记录流水信息
     *
     * @param date 日期 2014-11-06、2014-11、2014
     * @param type 查询类型
     * @return
     */
    public List<ConsumptionModel> getConsumption(String date, int type,
                                                 int sorting) {
        String sorting_field = "consumption_datetime";
        if (sorting == SOTRING_NAME) {
            sorting_field = "consumption_type";
        } else if (sorting == SOTRING_TIME) {
            sorting_field = "consumption_datetime";
        }
        LogUtil.ld(TAG, "param date：" + date + "----type: " + type);
        String whereCount = setConditons(type);
        String SQL = geDetaileFieldSQL() + whereCount + " order by "
                + sorting_field + " desc";
        LogUtil.ld(TAG,"getConsumption SQL -->"+SQL);
        Cursor c;
        // 如果查询条件不是空则添加条件
        if (type != QUERY_NULL) {
            c = db.rawQuery(SQL, new String[]{date});
        } else {
            c = db.rawQuery(SQL, null);
        }
        return parsingDetaileCursor(c);
    }

    /**
     * 修改某一天的日期
     *
     * @param oldDate
     * @param newDate
     */
    public void updateSummaryDate(String oldDate, String newDate) {
        ContentValues values = new ContentValues();
        values.put("consumption_moth", newDate);
        db.update(SUMMARY_TABLE, values, "consumption_date=?", new String[]{oldDate});
    }

    /**
     * 根据给定日期，和排序方式查询流水表信息
     *
     * @param date    日期 2014-11 按月查询
     * @param sorting 排序方式 SOTRING_NAME 名称排序，SOTRING_TIME时间排序
     * @return
     */
    public List<ConsumptionModel> getMothConsumption(String date, int sorting) {
        String sorting_field = "consumption_datetime";
        if (sorting == SOTRING_NAME) {
            sorting_field = "consumption_type";
        } else if (sorting == SOTRING_TIME) {
            sorting_field = "consumption_datetime";
        }
        String whereCount = " WHERE consumption_moth = ? ";
        String SQL = geDetaileFieldSQL() + whereCount + " order by "
                + sorting_field + " desc";
        Cursor c = db.rawQuery(SQL, new String[]{date});
        return parsingDetaileCursor(c);
    }

    /**
     * 解析统计 消费记录流水，统计成Map格式
     *
     * @param models
     * @return
     */
    public Map<Integer, Double> statisticalConsumption(
            List<ConsumptionModel> models) {
        Map<Integer, Double> statisMap = new HashMap<Integer, Double>();
        Integer consumptionType = -1;
        double count_number = 0;
        for (ConsumptionModel m : models) {
            if (consumptionType != m.getConsumption_type()) {
                count_number = 0;
            }
            count_number += m.getConsumption_number();
            consumptionType = m.getConsumption_type();
            statisMap.put(consumptionType, count_number);
        }
        return statisMap;
    }

    /**
     * 解析统计 消费记录流水，统计成List格式
     *
     * @param models
     * @return
     */
    public List<ConsumptionDetaileModel> statisticalConsumptionList(
            List<ConsumptionModel> models) {
        List<ConsumptionDetaileModel> mList = new ArrayList<ConsumptionDetaileModel>();
        ConsumptionDetaileModel model = null;
        int consumptionType = -9999;
        double count_number = 0;
        for (ConsumptionModel m : models) {
            if (consumptionType != m.getConsumption_type()) {
                count_number = 0;
                consumptionType = m.getConsumption_type();
                model = new ConsumptionDetaileModel();
                model.setConsumption_name(m.getConsumption_name());
                model.setConsumption_type(consumptionType);
                mList.add(model);
            }
            count_number += m.getConsumption_number();
            consumptionType = m.getConsumption_type();
            if (null != model) {
                model.setConsumption_count(count_number);
            }
        }
        return mList;

    }

    /**
     * 按条件查询消费总金额
     *
     * @param date 2014-11-06、2014-11、2014
     * @return
     */
    public Double getDayCountNumber(String date, int type) {
        String whereCount = setConditons(type);
        double countNumber = 0.0;
        String SQL = "SELECT sum(consumption_number) FROM " + CONSUMPUTIONTABLE
                + whereCount;
        Cursor c = db.rawQuery(SQL, new String[]{date});
        if (c.moveToNext()) {
            countNumber = c.getDouble(0);
        }
        //SQLite 查询出 100.000001的数,顾需要处理成 小数点后1位的 double
        BigDecimal b = new BigDecimal(countNumber);
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 按条件查询消费总金额
     *
     * @param date 2014-11-06、2014-11、2014
     * @return
     */
    public Double getMothCountNumber(String date, int type) {
        String whereCount = " WHERE consumption_moth = ? ";
        double countNumber = 0.0;
        String SQL = "SELECT sum(consumption_number) FROM " + CONSUMPUTIONTABLE
                + whereCount;
        Cursor c = db.rawQuery(SQL, new String[]{date});
        if (c.moveToNext()) {
            countNumber = c.getDouble(0);
        }
        //SQLite 查询出 100.000001的数,顾需要处理成 小数点后1位的 double
        BigDecimal b = new BigDecimal(countNumber);
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 根据日期获取当日添加账单数量
     *
     * @param date 日期 2014-12-31
     * @return
     */
    public int getTodayCount(String date) {
        String SQL = "SELECT sum(consumption_number) FROM " + CONSUMPUTIONTABLE
                + " WHERE consumption_date = ?";
        Cursor c = db.rawQuery(SQL, new String[]{date});
        int count = 0;
        if (c.moveToNext()) {
            count = c.getInt(0);
        }
        return count;
    }

    /**
     * 根据查询类型 选择查询条件，按日 按月 按年
     *
     * @param type 查询类型
     * @return
     */
    private String setConditons(int type) {
        String whereCount = " WHERE consumption_date = ?";
        if (type == QUERY_DAY) {
            whereCount = " WHERE consumption_date = ?";
        } else if (type == QUERY_MOTH) {
            whereCount = " WHERE consumption_moth = ?";
        } else if (type == QUERY_YEAR) {
            whereCount = " WHERE consumption_years = ?";
        } else {
            whereCount = " ";
        }
        return whereCount;
    }

    /**
     * 解析 SQL 执行完得到的Coursor
     *
     * @param c Coursor
     * @return 返回解析完的List
     */
    private List<ConsumptionModel> parsingDetaileCursor(Cursor c) {
        if (null == c) {
            return new ArrayList<ConsumptionModel>();
        }
        LogUtil.ld(TAG,"parsingDetaileCursor c Count -->"+c.getCount());
        List<ConsumptionModel> mList = new ArrayList<ConsumptionModel>();
        ConsumptionModel model;
        while (c.moveToNext()) {
            model = new ConsumptionModel();
            model.set_id(c.getInt(c.getColumnIndex("_id")));
            model.setConsumption_type(c.getInt(c
                    .getColumnIndex("consumption_type")));
            model.setConsumption_name(c.getString(c
                    .getColumnIndex("consumption_name")));
            model.setConsumption_number(c.getDouble(c
                    .getColumnIndex("consumption_number")));
            model.setConsumption_desc(c.getString(c
                    .getColumnIndex("consumption_desc")));
            model.setConsumption_date(c.getString(c
                    .getColumnIndex("consumption_date")));
            mList.add(model);
            model = null;
        }
        return mList;
    }

    /**
     * 添加一条消费汇总信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public boolean addConsumptionSummary(SummaryModel model) throws Exception {
        boolean result = false;
        try {
            ContentValues values = new ContentValues();
            values.put("colthing", model.getColthing_number());
            values.put("food", model.getFood_number());
            values.put("have_meal", model.getHave_meal_number());
            values.put("miscellaneous", model.getMiscellaneous_number());
            values.put("necessities", model.getNecessities_number());
            values.put("ruits_vegetables", model.getRuits_vegetables_number());
            values.put("traffic", model.getTraffic_number());
            values.put("custom", model.getConsumption_custm());
            values.put("consumption_years", model.getConsumption_year());
            values.put("consumption_moth", model.getConsumption_moth());
            values.put("consumption_date", model.getConsumption_date());
            //先删除当日汇总
//            deleteSummaryDate(model.getConsumption_date());
            db.insert(SUMMARY_TABLE, null, values);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public void deleteSummaryAll() {
        db.delete(SUMMARY_TABLE, null, null);
    }

    //根据日期删除一条汇总信息
    public void deleteSummaryDate(String date) {
        try {
            db.delete(SUMMARY_TABLE, "consumption_date = ?",
                    new String[]{date});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据ID 删除一条消费汇总信息
     *
     * @param _id
     */
    public void deleteSummary(int _id) {
        try {
            db.delete(SUMMARY_TABLE, "_id = ?",
                    new String[]{String.valueOf(_id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条消费类型
     *
     * @param model
     * @return
     * @throws Exception
     */
    public boolean insertConsumptionType(ConsumptionTypeModel model) throws Exception {
        boolean result = false;
        try {
            ContentValues values = new ContentValues();
            values.put("consumption_id", model.getConsumption_type());
            values.put("consumption_name", model.getConsumption_name());
            values.put("type_icon_select", model.getType_icon_select());
            db.insert(CONSUMPTION_TYPE, null, values);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    /**
     * 删除一条消费类型
     *
     * @param model
     */
    public void deldeteConsumptionType(ConsumptionTypeModel model) {
        try {
            db.delete(CONSUMPTION_TYPE, "consumption_name = ? AND consumption_id = " + Constants.CONSUPTION_CUSTOM,
                    new String[]{String.valueOf(model.getConsumption_name())});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有消费类型
     *
     * @return
     */
    public List<ConsumptionTypeModel> getConsumptionType() {
        List<ConsumptionTypeModel> list = new ArrayList<ConsumptionTypeModel>();
        String SQL = "SELECT _id, consumption_name,consumption_id,type_icon_select FROM " + CONSUMPTION_TYPE;
        Cursor c = db.rawQuery(SQL, null);
        ConsumptionTypeModel model;
        while (c.moveToNext()) {
            model = new ConsumptionTypeModel();
            model.set_id(c.getInt(c.getColumnIndex("_id")));
            model.setConsumption_name(c.getString(c.getColumnIndex("consumption_name")));
            model.setConsumption_type(c.getInt(c.getColumnIndex("consumption_id")));
            model.setType_icon_select(c.getInt(c.getColumnIndex("type_icon_select")));
            list.add(model);
            model = null;
        }
        return list;
    }

    /**
     * 解析 SQL 执行完得到的Coursor
     *
     * @param c Coursor
     * @return 返回解析完的List
     */
    private List<SummaryModel> parsingSummaryCursor(Cursor c) {
        if (null == c) {
            LogUtil.ld(TAG, "Parame Cursor is null~");
            return new ArrayList<SummaryModel>();
        }
        List<SummaryModel> mList = new ArrayList<SummaryModel>();
        SummaryModel model = null;
        while (c.moveToNext()) {
            model = new SummaryModel();
            model.set_id(c.getInt(c.getColumnIndex("_id")));
            model.setConsumption_custm(c.getDouble(c.getColumnIndex("custom")));
            model.setColthing_number(c.getDouble(c.getColumnIndex("colthing")));
            model.setFood_number(c.getDouble(c.getColumnIndex("food")));
            model.setHave_meal_number(c.getDouble(c.getColumnIndex("have_meal")));
            model.setMiscellaneous_number(c.getDouble(c.getColumnIndex("miscellaneous")));
            model.setNecessities_number(c.getDouble(c.getColumnIndex("necessities")));
            model.setRuits_vegetables_number(c.getDouble(c.getColumnIndex("ruits_vegetables")));
            model.setTraffic_number(c.getDouble(c.getColumnIndex("traffic")));
            model.setConsumption_year(c.getString(c.getColumnIndex("consumption_years")));
            model.setConsumption_moth(c.getString(c
                    .getColumnIndex("consumption_moth")));
            model.setConsumption_date(c.getString(c
                    .getColumnIndex("consumption_date")));
            mList.add(model);
            model = null;
        }
        LogUtil.ld(TAG, "Cursor paress List Size " + mList.size());
        return mList;
    }

    /**
     * 从指定的 汇总信息中 提取 月份信息
     *
     * @param daylList
     * @return
     */
    public List<SummaryModel> extractSummaryMoth(List<SummaryModel> daylList) {
        List<SummaryModel> list = new ArrayList<SummaryModel>();
        SummaryModel model = null;
        String date_moth = "";
        double colthing = 0.0;
        double food = 0.0;
        double have_meal = 0.0;
        double miscellaneous = 0.0;
        double necessities = 0.0;
        double ruits_vegetables = 0.0;
        double traffic = 0.0;
        double custom = 0.0;
        for (SummaryModel l : daylList) {
            if (!date_moth.equals(l.getConsumption_moth())) {
                colthing = 0.0;
                food = 0.0;
                have_meal = 0.0;
                miscellaneous = 0.0;
                necessities = 0.0;
                ruits_vegetables = 0.0;
                traffic = 0.0;
                date_moth = l.getConsumption_moth();
                model = new SummaryModel();
                model.setConsumption_moth(l.getConsumption_moth());
                model.setConsumption_date(l.getConsumption_date());
                model.setConsumption_year(l.getConsumption_year());
                list.add(model);
            }
            colthing += l.getColthing_number();
            food += l.getFood_number();
            have_meal += l.getHave_meal_number();
            miscellaneous += l.getMiscellaneous_number();
            necessities += l.getNecessities_number();
            ruits_vegetables += l.getRuits_vegetables_number();
            traffic += l.getTraffic_number();
            custom += l.getConsumption_custm();
            if (null != model) {
                model.setColthing_number(colthing);
                model.setFood_number(food);
                model.setHave_meal_number(have_meal);
                model.setMiscellaneous_number(miscellaneous);
                model.setNecessities_number(necessities);
                model.setRuits_vegetables_number(ruits_vegetables);
                model.setTraffic_number(traffic);
                model.setConsumption_custm(custom);
            }
        }
        return list;
    }

    /**
     * 从指定的 汇总信息中 提取 年度信息
     *
     * @param daylList
     * @return
     */
    public List<SummaryModel> extractSummaryYears(List<SummaryModel> daylList) {
        List<SummaryModel> list = new ArrayList<SummaryModel>();
        SummaryModel model = null;
        String date_years = "";
        double colthing = 0.0;
        double food = 0.0;
        double have_meal = 0.0;
        double miscellaneous = 0.0;
        double necessities = 0.0;
        double ruits_vegetables = 0.0;
        double traffic = 0.0;
        for (SummaryModel l : daylList) {
            if (!date_years.equals(l.getConsumption_year())) {
                colthing = 0.0;
                food = 0.0;
                have_meal = 0.0;
                miscellaneous = 0.0;
                necessities = 0.0;
                ruits_vegetables = 0.0;
                traffic = 0.0;
                date_years = l.getConsumption_year();
                model = new SummaryModel();
                model.setConsumption_moth(l.getConsumption_moth());
                model.setConsumption_date(l.getConsumption_date());
                model.setConsumption_year(l.getConsumption_year());

                list.add(model);
            }
            colthing += l.getColthing_number();
            food += l.getFood_number();
            have_meal += l.getHave_meal_number();
            miscellaneous += l.getMiscellaneous_number();
            necessities += l.getNecessities_number();
            ruits_vegetables += l.getRuits_vegetables_number();
            traffic += l.getTraffic_number();
            if (null != model) {
                model.setColthing_number(colthing);
                model.setFood_number(food);
                model.setHave_meal_number(have_meal);
                model.setMiscellaneous_number(miscellaneous);
                model.setNecessities_number(necessities);
                model.setRuits_vegetables_number(ruits_vegetables);
                model.setTraffic_number(traffic);
            }
        }
        return list;
    }

    /**
     * 插入 指定汇总信息
     * @param summaryDate
     * @return
     * @throws Exception
     */
    public boolean insertToday(String summaryDate) throws Exception {
        boolean result = false;
        SummaryModel model = getDateSummary(summaryDate);
        if(null != model){
            result = addConsumptionSummary(model);
        }
        return result;
    }

    /**
     * 获取制定日期的汇总信息
     * @param summaryDate
     * @return
     */
    public SummaryModel getDateSummary(String summaryDate) {
        List<ConsumptionModel> dayWaterList = getConsumption(summaryDate,
                ConsumptionHelper.QUERY_DAY,
                ConsumptionHelper.SOTRING_NAME);
        if (dayWaterList.size() > 0) {
            // 汇总流水信息
            Map<Integer, Double> dayMap = statisticalConsumption(dayWaterList);
            SummaryModel model = new SummaryModel();
            //获取对应统计值
            Double have_meal_number = dayMap.get(Constants.CONSUPTION_HAVE_MEAL);
            Double traffic_number = dayMap.get(Constants.CONSUPTION_TRAFFIC);
            Double ruits_vegetables_number = dayMap.get(Constants.CONSUPTION_FRUITS_VEGEBELES);
            Double necessities_number = dayMap.get(Constants.CONSUPTION_NECESSITES);
            Double food_number = dayMap.get(Constants.CONSUPTION_FOOT);
            Double colthing_number = dayMap.get(Constants.CONSUPTION_CLOTING);
            Double miscellaneous_number = dayMap.get(Constants.CONSUPTION_MISCELLANEOUS);
            Double custom_number = dayMap.get(Constants.CONSUPTION_CUSTOM);
            // 设置统计值
            model.setColthing_number(colthing_number);
            model.setTraffic_number(traffic_number);
            model.setRuits_vegetables_number(ruits_vegetables_number);
            model.setNecessities_number(necessities_number);
            model.setFood_number(food_number);
            model.setHave_meal_number(have_meal_number);
            model.setMiscellaneous_number(miscellaneous_number);
            model.setConsumption_custm(custom_number);
            model.setConsumption_date(summaryDate);
            model.setConsumption_moth(summaryDate.substring(0, 7));
            model.setConsumption_year(summaryDate.substring(0, 4));
            return model;
        }
        return null;
    }

    /**
     * 获取 消费汇总信息
     *
     * @param date 消费日期 2014、2014-11、2014-11-14
     * @param type 查询条件 按 年查询 月查询 日查询
     * @param consumptionHelper
     * @return
     */
    public List<SummaryModel> getSummaryConsumption(String date, int type, ConsumptionHelper consumptionHelper) {
        String whereCount = consumptionHelper.setConditons(type);
        String SQL = consumptionHelper.getSummrayFieldSQL() + whereCount
                + " order by consumption_date desc";
        Cursor c;
        // 如果查询条件不是空则添加条件
        if (type != QUERY_NULL) {
            c = consumptionHelper.db.rawQuery(SQL, new String[]{date});
        } else {
            c = consumptionHelper.db.rawQuery(SQL, null);
        }
        return consumptionHelper.parsingSummaryCursor(c);
    }
}
