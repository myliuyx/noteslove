package com.xf.notes.utils;

import android.widget.ImageView;

import com.xf.notes.R;
import com.xf.notes.common.Constants;
import com.xf.notes.model.ConsumptionTypeModel;

public class ImageViewUtils {
    /**
     * 分类设置View 背景图片
     * @param model   消费类型
     * @param iv_view 消费图标
     */
    public static void setIcon(ConsumptionTypeModel model, ImageView iv_view) {
        switch (model.getType_icon_select()) {
            case Constants.CONSUPTION_HAVE_MEAL:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.have_meal_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.have_meal_defult);
                }
                break;
            case Constants.CONSUPTION_TRAFFIC:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.traffic_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.traffic_defult);
                }
                break;
            case Constants.CONSUPTION_FRUITS_VEGEBELES:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.ruits_vegetables_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.ruits_vegetables_defult);
                }
                break;
            case Constants.CONSUPTION_NECESSITES:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.necessities_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.necessities_defult);
                }
                break;
            case Constants.CONSUPTION_FOOT:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.food_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.food_defult);
                }
                break;
            case Constants.CONSUPTION_CLOTING:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.clothing_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.clothing_defult);
                }
                break;
            case Constants.CONSUPTION_MISCELLANEOUS:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.miscellaneous_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.miscellaneous_defult);
                }
                break;
            case Constants.ADD_CONSUPTION_TYP:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.add_type_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.add_type_defult);
                }
                break;
            case Constants.CONSUPTION_CUSTOM:
                if (model.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
                    iv_view.setBackgroundResource(R.drawable.diy_add_press);
                } else {
                    iv_view.setBackgroundResource(R.drawable.diy_add_defult);
                }
                break;
        }
    }
	/**
	 * 分类设置View 背景图片
	 * 
	 * @param type
	 *            消费类型
	 * @param iv_view
	 *            消费图标
	 * 
	 */
	public static void setIcon(int type, ImageView iv_view) {
		if (iv_view != null) {
			switch (type) {
			case Constants.CONSUPTION_CLOTING:
				iv_view.setBackgroundResource(R.drawable.clothing_show);
				break;
			case Constants.CONSUPTION_FOOT:
				iv_view.setBackgroundResource(R.drawable.food_show);
				break;
			case Constants.CONSUPTION_FRUITS_VEGEBELES:
				iv_view.setBackgroundResource(R.drawable.ruits_vegetables_show);
				break;
			case Constants.CONSUPTION_HAVE_MEAL:
				iv_view.setBackgroundResource(R.drawable.have_meal_show);
				break;
			case Constants.CONSUPTION_MISCELLANEOUS:
				iv_view.setBackgroundResource(R.drawable.miscellaneous_show);
				break;
			case Constants.CONSUPTION_NECESSITES:
				iv_view.setBackgroundResource(R.drawable.necessities_show);
				break;
			case Constants.CONSUPTION_TRAFFIC:
				iv_view.setBackgroundResource(R.drawable.traffic_show);
				break;
                case Constants.CONSUPTION_CUSTOM:
                    iv_view.setBackgroundResource(R.drawable.diy_type_show);
                    break;
			}
		}
	}
}
