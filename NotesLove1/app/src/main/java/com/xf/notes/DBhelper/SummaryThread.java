package com.xf.notes.DBhelper;

import java.util.List;
import java.util.Map;

import com.xf.notes.common.Constants;
import com.xf.notes.model.ConsumptionModel;
import com.xf.notes.model.SummaryModel;

public class SummaryThread implements Runnable {
	private String summaryDate;
	private ConsumptionHelper cHelper;
	public SummaryThread(String summaryDate, ConsumptionHelper cHelper) {
		this.summaryDate = summaryDate;
		this.cHelper = cHelper;
	}
	@Override
	public void run() {
		try {
			List<SummaryModel> dayCount = cHelper.mInstance.getSummaryConsumption(summaryDate, ConsumptionHelper.QUERY_DAY, cHelper);
			// 删除 所有查询到日期 汇总信息
			for (SummaryModel sm : dayCount) {
				cHelper.deleteSummary(sm.get_id());
			}
			// 查询给定日期流水信息
			List<ConsumptionModel> dayWaterList = cHelper.getConsumption(
					summaryDate, ConsumptionHelper.QUERY_DAY,
					ConsumptionHelper.SOTRING_NAME);
			// 汇总流水信息
			Map<Integer, Double> dayMap = cHelper.statisticalConsumption(dayWaterList);
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
            model.setConsumption_custm(custom_number);
			model.setColthing_number(colthing_number);
			model.setTraffic_number(traffic_number);
			model.setRuits_vegetables_number(ruits_vegetables_number);
			model.setNecessities_number(necessities_number);
			model.setFood_number(food_number);
			model.setHave_meal_number(have_meal_number);
			model.setMiscellaneous_number(miscellaneous_number);
			model.setConsumption_date(summaryDate);
			cHelper.addConsumptionSummary(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
