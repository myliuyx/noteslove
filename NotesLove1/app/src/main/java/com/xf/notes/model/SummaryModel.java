package com.xf.notes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.xf.notes.common.Constants;
import com.xf.notes.utils.DateUtils;

/**
 * 流水汇总类。
 * 注意： 所有Set给 该类的 字段， 都必须不为null  Set 方法已做处理， 如 Set Null  将不会执行 set 方法
 */
public class SummaryModel implements Serializable {
    private final String TAG = "MoreFragment";
    private static final long serialVersionUID = 1L;
    private int _id;
    private Double colthing_number = 0.0;
    private Double food_number = 0.0;
    private Double have_meal_number = 0.0;
    private Double miscellaneous_number = 0.0;
    private Double necessities_number = 0.0;
    private Double ruits_vegetables_number = 0.0;
    private Double traffic_number = 0.0;
    private Double consumption_custm = 0.0;
    private String consumption_year = DateUtils.get_1(null);// 消费所属年
    private String consumption_moth = DateUtils.get_6(null);// 消费所属月
    private String consumption_date = DateUtils.get_0(null);// 消费日期

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Double getColthing_number() {
        return colthing_number;
    }

    public void setColthing_number(Double colthing_number) {
        if (colthing_number != null) {
            this.colthing_number = colthing_number;
        }
    }

    public Double getFood_number() {
        return food_number;
    }

    public void setFood_number(Double food_number) {
        if (food_number != null) {
            this.food_number = food_number;
        }
    }

    public Double getHave_meal_number() {
        return have_meal_number;
    }

    public void setHave_meal_number(Double have_meal_number) {
        if (have_meal_number != null) {
            this.have_meal_number = have_meal_number;
        }
    }

    public Double getMiscellaneous_number() {
        return miscellaneous_number;
    }

    public void setMiscellaneous_number(Double miscellaneous_number) {
        if (miscellaneous_number != null) {
            this.miscellaneous_number = miscellaneous_number;
        }
    }

    public Double getNecessities_number() {
        return necessities_number;
    }

    public void setNecessities_number(Double necessities_number) {
        if (necessities_number != null) {
            this.necessities_number = necessities_number;
        }
    }

    public Double getRuits_vegetables_number() {
        return ruits_vegetables_number;
    }

    public void setRuits_vegetables_number(Double ruits_vegetables_number) {
        if (ruits_vegetables_number != null) {
            this.ruits_vegetables_number = ruits_vegetables_number;
        }
    }

    public Double getTraffic_number() {
        return traffic_number;
    }

    public void setTraffic_number(Double traffic_number) {
        if (traffic_number != null) {
            this.traffic_number = traffic_number;
        }
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

    public Double getConsumption_custm() {
        return consumption_custm;
    }

    public void setConsumption_custm(Double consumption_custm) {
        if(null != consumption_custm){this.consumption_custm = consumption_custm;}
    }

    public Object getConsumptionCount() {
        Double count = colthing_number + food_number + have_meal_number
                + miscellaneous_number + necessities_number
                + ruits_vegetables_number + traffic_number+consumption_custm;
        BigDecimal b = new BigDecimal(count);
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 将各种消费类型整理成List 用于适配GridView。
     *
     * @return
     */
    public List<ConsumptionDetaileModel> getConsumptionNumberMap() {
        List<ConsumptionDetaileModel> mList = new ArrayList<ConsumptionDetaileModel>();
        ConsumptionDetaileModel model1 = new ConsumptionDetaileModel();
        model1.setConsumption_type(Constants.CONSUPTION_HAVE_MEAL);
        model1.setConsumption_name("吃饭");
        model1.setConsumption_count(have_meal_number);
        if (have_meal_number > 0) {
            mList.add(model1);
        }
        ConsumptionDetaileModel model2 = new ConsumptionDetaileModel();
        model2.setConsumption_type(Constants.CONSUPTION_TRAFFIC);
        model2.setConsumption_name("交通");
        model2.setConsumption_count(traffic_number);
        if (traffic_number > 0) {
            mList.add(model2);
        }
        ConsumptionDetaileModel model3 = new ConsumptionDetaileModel();
        model3.setConsumption_type(Constants.CONSUPTION_FRUITS_VEGEBELES);
        model3.setConsumption_name("果蔬");
        model3.setConsumption_count(ruits_vegetables_number);
        if (ruits_vegetables_number > 0) {
            mList.add(model3);
        }
        ConsumptionDetaileModel model4 = new ConsumptionDetaileModel();
        model4.setConsumption_type(Constants.CONSUPTION_NECESSITES);
        model4.setConsumption_name("日用");
        model4.setConsumption_count(necessities_number);
        if (necessities_number > 0) {
            mList.add(model4);
        }
        ConsumptionDetaileModel model5 = new ConsumptionDetaileModel();
        model5.setConsumption_type(Constants.CONSUPTION_FOOT);
        model5.setConsumption_name("食品");
        model5.setConsumption_count(food_number);
        if (food_number > 0) {
            mList.add(model5);
        }
        ConsumptionDetaileModel model6 = new ConsumptionDetaileModel();
        model6.setConsumption_type(Constants.CONSUPTION_CLOTING);
        model6.setConsumption_name("服饰");
        model6.setConsumption_count(colthing_number);
        if (colthing_number > 0) {
            mList.add(model6);
        }
        ConsumptionDetaileModel model7 = new ConsumptionDetaileModel();
        model7.setConsumption_type(Constants.CONSUPTION_MISCELLANEOUS);
        model7.setConsumption_name("其他");
        model7.setConsumption_count(miscellaneous_number);
        if (miscellaneous_number > 0) {
            mList.add(model7);
        }
        ConsumptionDetaileModel model8 = new ConsumptionDetaileModel();
        model8.setConsumption_type(Constants.CONSUPTION_CUSTOM);
        model8.setConsumption_name("DIY");
        model8.setConsumption_count(consumption_custm);
        if (consumption_custm > 0) {
            mList.add(model8);
        }
        return mList;
    }
}
