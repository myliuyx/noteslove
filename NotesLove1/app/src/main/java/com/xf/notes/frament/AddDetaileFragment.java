package com.xf.notes.frament;

import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.xf.notes.MainActivity;
import com.xf.notes.R;
import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.frament.adapter.AddDetailTypeAdapter;
import com.xf.notes.model.ConsumptionModel;
import com.xf.notes.model.ConsumptionTypeModel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AddDetaileFragment extends Fragment {
	private String TAG = "AddDetaileFragment";
	private ConsumptionHelper cHelper;
	private MainActivity activity;// 父Activity 
	private List<ConsumptionTypeModel> consumpType;
	private ViewPager detail_notes;// 消费 类型 控件
	private EditText ed_consump_number;// 消费 金额
	private EditText ed_consump_detail;// 消费 备注
	private Button add_detaile_determine;// 确认 添加 消费
    public LinearLayout round_layout;
	private AddDetailTypeAdapter adapter;
	@Override
	public void onAttach(Activity activity) {
		this.activity = (MainActivity) activity;
		this.activity.isHomeFragment =false;
		cHelper = this.activity.dbHelper;
		this.activity.isShowBackArrow();
		this.activity.setTitileNma("记账");
		super.onAttach(activity);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
        consumpType = Constants.getConsumptionTypes();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_detail_fragment, container,false);
		initView(view);
		return view;
	}
	/**
	 * 初始化控件
	 * @param view
	 */
	private void initView(View view){
        round_layout = (LinearLayout) view.findViewById(R.id.round_layout);
		detail_notes = (ViewPager) view.findViewById(R.id.detail_notes);
		ed_consump_number = (EditText) view.findViewById(R.id.ed_consump_number);
		ed_consump_detail = (EditText) view.findViewById(R.id.ed_consump_detail);
		add_detaile_determine = (Button) view.findViewById(R.id.add_detaile_determine);
		adapter = new AddDetailTypeAdapter(activity,consumpType,ed_consump_number,AddDetaileFragment.this);
		detail_notes.setAdapter(adapter);
        //添加 小圆圈
        addRound(adapter.pages);
        detail_notes.setOnPageChangeListener( new OnPageChangeListener());
		add_detaile_determine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String add_number = ed_consump_number.getText().toString();
				String add_detail = ed_consump_detail.getText().toString();
                ConsumptionTypeModel selectModel = adapter.getSelectMode();
				ConsumptionModel model = new ConsumptionModel();
				if (null == add_number || add_number.equals("")) {
					Toast.makeText(activity, "小爱觉得，起码您得告诉我花了多少钱~", Toast.LENGTH_SHORT).show();
					return ;
				}else if(null == selectModel){
                    Toast.makeText(activity, "小爱还需要知道你怎么花了这笔钱~", Toast.LENGTH_SHORT).show();
                    return ;
                }
				model.setConsumption_type(selectModel.getConsumption_type());
				model.setConsumption_name(selectModel.getConsumption_name());
				model.setConsumption_desc(add_detail);
				model.setConsumption_number(Double.valueOf(add_number));
				try {
					cHelper.addConsumption(model);
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.ld(TAG, e.getMessage());
				}
                //提交完了将选中状态还原
                selectModel.setIsCheckd(ConsumptionTypeModel.TYPE_DEFULT);
				activity.startFragment(new HomeFragment(),null);
				((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ed_consump_number.getWindowToken(), 
						InputMethodManager.HIDE_NOT_ALWAYS); 
			}
		});
	}
    //创建底部小圆圈
    public void addRound(int pagesCount){
        if(pagesCount <= 1 ) return ;
        LinearLayout.LayoutParams rl = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        for(int i = 0;i< pagesCount ; i++) {
            RelativeLayout pointLayout = insertRound(rl);
            if (i == 0) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 0);
                pointLayout.getChildAt(0).setLayoutParams(lp);
            }
            round_layout.addView(pointLayout);
            setCurPage(0);
            LogUtil.ld(TAG,"addRound Round Count i-->"+i);
        }
    }
    //创建一个小圆圈
    public RelativeLayout insertRound(LinearLayout.LayoutParams rl){
        RelativeLayout pointLayout = new RelativeLayout(activity);
        pointLayout.setLayoutParams(rl);
        ImageView circle = new ImageView(activity);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        circle.setLayoutParams(lp);
        circle.setPadding(6, 6, 6, 6);
        lp.setMargins(8, 0, 0, 0);
        circle.setBackgroundResource(R.drawable.dot_normal);
        pointLayout.addView(circle);
        return pointLayout;
    }
    //删除一个小圆圈
    public void removeRound(){
        round_layout.removeViewAt(round_layout.getChildCount()-1);
    }
    private int oldPosition = -1;
    //设置当前所在Page
    private void setCurPage(int page){
        RelativeLayout newV = (RelativeLayout) round_layout.getChildAt(page);
        if(null == newV){return;}
        RelativeLayout oldV = (RelativeLayout) round_layout.getChildAt(oldPosition);
        if (oldPosition > -1){oldV.getChildAt(0).setBackgroundResource(R.drawable.dot_normal);}
        newV.getChildAt(0).setBackgroundResource(R.drawable.dot_focused);
        oldPosition = page;
    }
    class OnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setCurPage(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

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
