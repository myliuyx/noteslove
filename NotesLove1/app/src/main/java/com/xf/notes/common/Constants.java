package com.xf.notes.common;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import com.xf.notes.R;
import com.xf.notes.model.ConsumptionDetaileModel;
import com.xf.notes.model.ConsumptionTypeModel;

public class Constants {
	/**Log 级别 控制Log 输出*/
	public static final int LogLevel = 6;
	/**占位类型*/
	public static final int CONSUPTION_NULL = -99;
	/**吃饭*/
	public static final int CONSUPTION_HAVE_MEAL = 1;
	/**交通*/
	public static final int CONSUPTION_TRAFFIC = 2;
	/**果蔬*/
	public static final int CONSUPTION_FRUITS_VEGEBELES = 3;
	/**日用品*/
	public static final int CONSUPTION_NECESSITES = 4;
	/**食品*/
	public static final int CONSUPTION_FOOT = 5;
	/**衣物*/
	public static final int CONSUPTION_CLOTING = 6;
	/**杂项*/
	public static final int CONSUPTION_MISCELLANEOUS = 7;
    /**添加分类*/
    public static final int ADD_CONSUPTION_TYP = -9;
    /**DIY分类*/
    public static final int CONSUPTION_CUSTOM = 8;
	/**SharedPreferences 配置文件名称*/
	public static final String CONFIG_NOTES="config_notes";
    /***/
    public static final String DB_BACKUP_DIR="/notesLove";
	/**今天*/
	public static final int HOME_QUERY_TODAY=1;
	/**昨天*/
	public static final int HOME_QUERY_YESTERDAY=2;
	/**本月*/
	public static final int HOME_QUERY_THIS_MOTH=3;
	/**更多*/
	public static final int HOME_QUERY_MORE=4;
	/** 日期 */
	public static final int SELECT_DAY = 21;
	/** 月份 */
	public static final int SELECT_MOTH = 22;
	/** 年度 */
	public static final int SELECT_YEARS = 23;
    /**每日记账提醒时间*/
    public static final String REMIND_TIME = "20:00:00";
    /**消费类型每页显示个数*/
    public static final int SET_TYPE_PAGE_SIZE = 10;
    /**是否显示广告*/
    public static int AD_MANAGER = 0;
    /**是否提醒通知 Notification*/
    public static boolean IS_REMIND = true;
    // 配置文件统计日期名称
    public static final String SUMMARY_DATE_NAME = "summary_date";
    /**APK下载位置*/
    public static final String APK_PATH = Environment.getExternalStorageDirectory() + "/Notes/notesLove.apk";
    public static final String TOKEN = "dd30d7a4-c4f1-4644-8239-4450ea82bc22-2015-01-05";
    /**服务器连接地址 */
    public static final String HOST_URL = "http://notes.xbizi.com:8080/notesService/services/rest/webService";
    /**版本检测接口*/
    public static final String INTERFACE_CHECK_VERSION = "/checkVersion";
    /**意见反馈接口*/
    public static final String INTERFACE_FEED_BACK = "/feedBack";
    /**消费类型，再MainActvity启动时赋值*/
    public static List<ConsumptionTypeModel> consumptionTypes;

    public static List<ConsumptionTypeModel> getConsumptionTypes(){
        if (consumptionTypes.size()<=0){
            consumptionTypes = initConsumptionType();
            ConsumptionTypeModel model = new ConsumptionTypeModel();
            model.setConsumption_type(Constants.ADD_CONSUPTION_TYP);
            model.setType_icon_select(Constants.ADD_CONSUPTION_TYP);
            model.setConsumption_name("添加类别");
            consumptionTypes.add(model);
        }
        return  consumptionTypes;
    }

    /**
     * 初始化popupWindow 显示类型
     * @return
     */
    public static List<ConsumptionTypeModel> initPopupConumptionType(){
        List<ConsumptionTypeModel> l =  initConsumptionType();
        ConsumptionTypeModel diyModel = new ConsumptionTypeModel();
        diyModel.setConsumption_type(Constants.CONSUPTION_CUSTOM);
        diyModel.setConsumption_name("DIY");
        l.add(diyModel);
        return l;
    }
    /**
     * 获取初始化默认类型
     * @return
     */
    public static List<ConsumptionTypeModel> initConsumptionType(){
        List<ConsumptionTypeModel> mList = new ArrayList<ConsumptionTypeModel>();
        ConsumptionTypeModel model1 = new ConsumptionTypeModel();
        model1.setConsumption_type(Constants.CONSUPTION_HAVE_MEAL);
        model1.setType_icon_select(Constants.CONSUPTION_HAVE_MEAL);
        model1.setConsumption_name("吃饭");
        ConsumptionTypeModel model2 = new ConsumptionTypeModel();
        model2.setConsumption_type(Constants.CONSUPTION_TRAFFIC);
        model2.setType_icon_select(Constants.CONSUPTION_TRAFFIC);
        model2.setConsumption_name("交通");
        ConsumptionTypeModel model3 = new ConsumptionTypeModel();
        model3.setConsumption_type(Constants.CONSUPTION_FRUITS_VEGEBELES);
        model3.setType_icon_select(Constants.CONSUPTION_FRUITS_VEGEBELES);
        model3.setConsumption_name("果蔬");
        ConsumptionTypeModel model4 = new ConsumptionTypeModel();
        model4.setConsumption_type(Constants.CONSUPTION_NECESSITES);
        model4.setType_icon_select(Constants.CONSUPTION_NECESSITES);
        model4.setConsumption_name("居家日用");
        ConsumptionTypeModel model5 = new ConsumptionTypeModel();
        model5.setConsumption_type(Constants.CONSUPTION_FOOT);
        model5.setType_icon_select(Constants.CONSUPTION_FOOT);
        model5.setConsumption_name("食品");
        ConsumptionTypeModel model6 = new ConsumptionTypeModel();
        model6.setConsumption_type(Constants.CONSUPTION_CLOTING);
        model6.setType_icon_select(Constants.CONSUPTION_CLOTING);
        model6.setConsumption_name("服饰鞋包");
        ConsumptionTypeModel model7 = new ConsumptionTypeModel();
        model7.setConsumption_type(Constants.CONSUPTION_MISCELLANEOUS);
        model7.setType_icon_select(Constants.CONSUPTION_MISCELLANEOUS);
        model7.setConsumption_name("其他");
        mList.add(model1);
        mList.add(model2);
        mList.add(model3);
        mList.add(model5);
        mList.add(model4);
        mList.add(model6);
        mList.add(model7);
        return mList;

    }



    /**
     * 初始化菜单类型
     * @return

	public static List<ConsumptionDetaileModel> initConsumptionType(){
		List<ConsumptionDetaileModel> mList = new ArrayList<ConsumptionDetaileModel>();
		ConsumptionDetaileModel model1 = new ConsumptionDetaileModel();
		model1.setConsumption_type(Constants.CONSUPTION_HAVE_MEAL);
		model1.setConsumption_name("吃饭");
		ConsumptionDetaileModel model2 = new ConsumptionDetaileModel();
		model2.setConsumption_type(Constants.CONSUPTION_TRAFFIC);
		model2.setConsumption_name("交通");
		ConsumptionDetaileModel model3 = new ConsumptionDetaileModel();
		model3.setConsumption_type(Constants.CONSUPTION_FRUITS_VEGEBELES);
		model3.setConsumption_name("果蔬");
		ConsumptionDetaileModel model4 = new ConsumptionDetaileModel();
		model4.setConsumption_type(Constants.CONSUPTION_NECESSITES);
		model4.setConsumption_name("日用品");
		ConsumptionDetaileModel model5 = new ConsumptionDetaileModel();
		model5.setConsumption_type(Constants.CONSUPTION_FOOT);
		model5.setConsumption_name("食品");
		ConsumptionDetaileModel model6 = new ConsumptionDetaileModel();
		model6.setConsumption_type(Constants.CONSUPTION_CLOTING);
		model6.setConsumption_name("衣物");
		ConsumptionDetaileModel model7 = new ConsumptionDetaileModel();
		model7.setConsumption_type(Constants.CONSUPTION_MISCELLANEOUS);
		model7.setConsumption_name("其他");
		mList.add(model1);
		mList.add(model2);
		mList.add(model3);
		mList.add(model4);
		mList.add(model5);
		mList.add(model6);
		mList.add(model7);
		return mList;
	}
     */
}
