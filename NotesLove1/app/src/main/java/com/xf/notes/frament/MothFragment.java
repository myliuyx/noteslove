package com.xf.notes.frament;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.xf.notes.MainActivity;
import com.xf.notes.R;
import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.frament.adapter.MothFragmentAdapter;
import com.xf.notes.frament.adapter.PopupGridViewAdapter;
import com.xf.notes.model.ConsumptionDetaileModel;
import com.xf.notes.model.ConsumptionModel;
import com.xf.notes.model.ConsumptionTypeModel;
import com.xf.notes.utils.DateUtils;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

/**
 * 
 * @author liuyuanxiao
 */
public class MothFragment extends Fragment implements OnClickListener {
	private final String TAG ="MothFragment";
	/** 全部 */
	private final int SELECT_ALL = 1;
	/** 分类 */
	private final int SELECT_CLASSIFICATION = 2;
	/** 查询成功 */
	private final int QUERY_SUCCESS = 1;
	/** 查询失败 */
	private final int QUERY_FAILURE = -1;
	private MainActivity activity;// 父Activity
	private ConsumptionHelper cHelper;
	// 查询信息 参数 月份 2014-11
	private String date = "-1";
	private TextView bill_type_all_tv;// “全部” TextView
	private RelativeLayout bill_type_classification_rl; // “分类” 控件
	private ImageView bill_type_classification_iv; // “分类” 傍边的小箭头
	private TextView bill_type_classification_tv; // “分类” 文字控件
	private ListView detaile_bill_list;// 显示 所有 明细的ListView
	private ImageView gary_bg_iv;// popopuWindow 弹出时的灰色半透明背景
	private PopupWindow mPopupWindow; // 弹出菜单
	// 是否选择 类型 -1 未选择 1 已选择
	private int select_type = -1;
	private List<ConsumptionModel> mList;// 流水信息List
	private List<ConsumptionModel> tmpList = new ArrayList<ConsumptionModel>();// 临时分类流水List
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case QUERY_SUCCESS:// 本地数据查询结束
				updateUI();
				break;
			case QUERY_FAILURE:// 本地数据查询失败
				Toast.makeText(activity, "小爱在查询您的记录的时候发生了错误~",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private MothFragmentAdapter adapter;

	@Override
	public void onAttach(Activity activity) {
		this.activity = (MainActivity) activity;
		this.activity.isHomeFragment = false;
		cHelper = ConsumptionHelper.getInstance(activity);
		this.activity.isShowBackArrow();
		this.activity.setTitileNma("我的账单");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		date = DateUtils.get_6(null);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.more_fragment, container,
				false);
		initView(rootView);
		 new Thread(new SQLiteThread(date)).start();
		return rootView;
	}

	private void initView(View view) {
		bill_type_all_tv = (TextView) view.findViewById(R.id.bill_type_all_tv);
		bill_type_classification_rl = (RelativeLayout) view
				.findViewById(R.id.bill_type_classification_rl);
		bill_type_classification_iv = (ImageView) view
				.findViewById(R.id.bill_type_classification_iv);
		bill_type_classification_tv = (TextView) view
				.findViewById(R.id.bill_type_classification_tv);
		detaile_bill_list = (ListView) view
				.findViewById(R.id.detaile_bill_list);
		gary_bg_iv = (ImageView) view.findViewById(R.id.gary_bg_iv);
		bill_type_all_tv.setOnClickListener(this);
		bill_type_classification_rl.setOnClickListener(this);
		setBillType(SELECT_ALL);// 默认选择全部
	}

	private void updateUI() {
		adapter = new MothFragmentAdapter(mList, activity);
		detaile_bill_list.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bill_type_all_tv:// “全部” 点击事件
			setBillType(SELECT_ALL);
			if (adapter != null) {
				adapter.setList(mList);
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.bill_type_classification_rl:// “分类” 点击事件
			setBillType(SELECT_CLASSIFICATION);
			showPopupWindow();
			break;
		}
	}

	/**
	 * 显示popupWindow
	 */
	private void showPopupWindow() {
		View v = View.inflate(activity.getApplicationContext(),
				R.layout.more_popup_view, null);
		GridView gridView = (GridView) v.findViewById(R.id.more_popup_grid);
		final List<ConsumptionTypeModel> consumpType = Constants.initPopupConumptionType();
		int count = 3 - (consumpType.size() % 3);
		// 如果 选项不不够行数，则为其补全。
		for (int i = 0; i < count; i++) {
			consumpType.add(getNullConsumption());
		}
		// 给GridView 设置adapter
		if (consumpType != null) {
			PopupGridViewAdapter popupGridViewAdapter = new PopupGridViewAdapter(
					consumpType, activity);
			gridView.setAdapter(popupGridViewAdapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
                    ConsumptionTypeModel model = consumpType.get(position);
					if (model.getConsumption_type() == Constants.CONSUPTION_NULL) {
						return ;
					}
					tmpList = extractList(mList, model.getConsumption_type());
					//获取到所点击的分类之后 更新Adapter
					if (tmpList.size() > 0) {
						bill_type_classification_tv.setText(model
								.getConsumption_name());
						if (adapter != null) {
							adapter.setList(tmpList);
							adapter.notifyDataSetChanged();
							select_type = 1;
							mPopupWindow.dismiss();
						}
					} else {
						Toast.makeText(
								activity,
								"小爱在记录里没发现 " + model.getConsumption_name()
										+ " 分类~", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(activity);
			mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
			// ColorDrawable drawable = new
			// ColorDrawable(getResources().getColor(R.color.gray));
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setFocusable(true);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					gary_bg_iv.setVisibility(View.GONE);
					if (select_type == -1) {
						// setDetaileAdapterAndAllSelect(mList);
						setBillType(SELECT_ALL);
					}
				}
			});
		}
		mPopupWindow.setContentView(v);
		View view = getView().findViewById(R.id.title_type_layout);
		mPopupWindow.showAsDropDown(view, 0, 3);
		gary_bg_iv.setVisibility(View.VISIBLE);
	}

	/**
	 * 头部 全部 分类 控件的点击事件设置
	 * 
	 * @param type
	 *            点击的类型
	 */
	private void setBillType(int type) {
		if (type == SELECT_ALL) {
			bill_type_all_tv
					.setBackgroundResource(R.drawable.more_type_all_bg_press);
			bill_type_all_tv.setTextColor(getResources()
					.getColor(R.color.white));
			bill_type_classification_rl
					.setBackgroundResource(R.drawable.more_type_classification_bg_normal);
			bill_type_classification_tv.setTextColor(getResources().getColor(
					R.color.classification));
			bill_type_classification_iv
					.setBackgroundResource(R.drawable.classification_normal);
			bill_type_classification_tv.setText("分类");
			// 将是否选择 置为 为选择
			select_type = -1;
		} else if (type == SELECT_CLASSIFICATION) {
			bill_type_all_tv
					.setBackgroundResource(R.drawable.more_type_all_bg_normal);
			bill_type_all_tv.setTextColor(getResources().getColor(
					R.color.classification));
			bill_type_classification_rl
					.setBackgroundResource(R.drawable.more_type_classification_bg_press);
			bill_type_classification_tv.setTextColor(getResources().getColor(
					R.color.white));
			bill_type_classification_iv
					.setBackgroundResource(R.drawable.classification_press);
		}
	}
	/**
	 * 根据执行类型提取，List
	 * @param list all List 
	 * @param type 提取类型
	 * @return
	 */
	private List<ConsumptionModel> extractList(List<ConsumptionModel> list,int type){
		tmpList.clear();
		List<ConsumptionModel> tList = new ArrayList<ConsumptionModel>();
		for (ConsumptionModel consumptionModel : list) {
			if (consumptionModel.getConsumption_type() == type) {
				tList.add(consumptionModel);
			}
		}
		return tList;
	}
	/**
	 * @return 返回name 为""的 ConsumptionDetaileModel 对象，用于补全选择。
	 */
	private ConsumptionTypeModel getNullConsumption() {
        ConsumptionTypeModel model = new ConsumptionTypeModel();
		model.setConsumption_type(Constants.CONSUPTION_NULL);
		model.setConsumption_name("");
		return model;
	}
	/**
	 * 本地数据库查询操作
	 * 
	 * @author liuyuanxiao
	 * 
	 */
	class SQLiteThread implements Runnable {
		private String date;

		public SQLiteThread(String date) {
			this.date = date;
		}

		@Override
		public void run() {
			try {
				mList = cHelper.getConsumption(date,ConsumptionHelper.QUERY_MOTH,ConsumptionHelper.SOTRING_TIME);
				LogUtil.ld(TAG, "mList Size -->"+mList.size());
				Message msg = mHandler.obtainMessage();
				msg.what = QUERY_SUCCESS;
				mHandler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = mHandler.obtainMessage();
				msg.what = QUERY_FAILURE;
				mHandler.sendMessage(msg);
			}
		}
	}
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }
}
