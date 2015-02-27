package com.xf.notes.utils;

import java.util.Comparator;

import com.xf.notes.model.ConsumptionModel;

/**
 * ConsumptionModel List 比较器
 * @author liuyuanxiao
 *
 */
public class ConmsuptionModelComparator implements Comparator<ConsumptionModel>{


	@Override
	public int compare(ConsumptionModel lhs, ConsumptionModel rhs) {
		ConsumptionModel model1 = (ConsumptionModel) lhs;
		ConsumptionModel model2 = (ConsumptionModel) rhs;
		return model1.getConsumption_name().compareTo(model2.getConsumption_name());
	}
}
