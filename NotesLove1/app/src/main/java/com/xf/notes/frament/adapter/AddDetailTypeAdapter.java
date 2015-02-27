package com.xf.notes.frament.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xf.notes.DBhelper.ConsumptionHelper;
import com.xf.notes.R;
import com.xf.notes.common.Constants;
import com.xf.notes.common.LogUtil;
import com.xf.notes.frament.AddDetaileFragment;
import com.xf.notes.model.ConsumptionTypeModel;
import com.xf.notes.view.AddTpyeDialog;
import com.xf.notes.view.BaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加消费记录 适配器
 * Created by liuyuanxiao on 15/1/14.
 */
public class AddDetailTypeAdapter extends PagerAdapter {
    private final String TAG = "AddDetailTypeAdapter";
    private ConsumptionHelper cHelper;
    private Context context;
    private AddDetaileFragment fragment;
    private List<ConsumptionTypeModel> modelList;
    private List<List<ConsumptionTypeModel>> typeList;
    private GridView mGridView;
    private EditText ed_view;
    public int pages;
    //是否显示键盘
    private boolean isShowInputMoth = false;
    private ConsumptionTypeModel selectMode;

    /**
     * 构造函数 初始化数据库Helper 类，初始化 ViewPager 数据切分。
     *
     * @param context   上下文
     * @param modelList 带切分数据
     * @param view      输入框View
     * @param fragment  当前所在的Fragment
     */
    public AddDetailTypeAdapter(Context context, List<ConsumptionTypeModel> modelList, EditText view, AddDetaileFragment fragment) {
        this.context = context;
        this.modelList = modelList;
        this.ed_view = view;
        this.fragment = fragment;
        cHelper = ConsumptionHelper.getInstance(context);
        getPagerList(this.modelList);
    }

    public ConsumptionTypeModel getSelectMode() {
        return selectMode;
    }

    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        for (int i = 0; i < typeList.size(); i++) {
            if (((GridView) object).equals(typeList.get(i))) {
                return i;
            }
        }
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        List<ConsumptionTypeModel> itemList = typeList.get(position);
        mGridView = (GridView) LayoutInflater.from(context).inflate(R.layout.add_type_gridview, null);
        ConsumptionDetaileAdapter grivdViewAdapter = new ConsumptionDetaileAdapter(itemList, context);
        mGridView.setAdapter(grivdViewAdapter);
        mGridView.setOnItemLongClickListener(new GrivdViewOnItemLongClikListener(typeList.get(position)));
        mGridView.setOnItemClickListener(new GrivdViewOnItemClikListener(itemList, grivdViewAdapter));
        ((ViewPager) container).addView(mGridView);
        return mGridView;
    }

    //GrivdView Item 点击事件
    class GrivdViewOnItemClikListener implements AdapterView.OnItemClickListener {
        List<ConsumptionTypeModel> imList;
        ConsumptionDetaileAdapter grivdViewAdapter;

        GrivdViewOnItemClikListener(List<ConsumptionTypeModel> models, ConsumptionDetaileAdapter grivdViewAdapter) {
            imList = models;
            this.grivdViewAdapter = grivdViewAdapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ConsumptionTypeModel model = imList.get(position);
            selectMode = model;
            ImageView imgV = (ImageView) view.findViewById(R.id.type_icon);
            if (model.getConsumption_type() == Constants.ADD_CONSUPTION_TYP) {
                if (isShowInputMoth) {
                    hidInputMothd();
                }
                setAllDefultIcon(imList);
                showDialog();
            } else {
                clickType(model, imList);
            }
            grivdViewAdapter.notifyDataSetChanged();
        }
    }

    ConsumptionTypeModel vModel;

    //GriView Item的长按动作
    class GrivdViewOnItemLongClikListener implements AdapterView.OnItemLongClickListener {
        List<ConsumptionTypeModel> imList;

        GrivdViewOnItemLongClikListener(List<ConsumptionTypeModel> mLiist) {
            imList = mLiist;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            LogUtil.ld(TAG, "onClik Long Item postion-->" + position);
            vModel = imList.get(position);
            showRemoveTypeDialog();
            return false;
        }
    }

    ConsumptionTypeModel insertModel;

    /**
     * 显示添加分类 Dialog
     */
    private void showDialog() {
        AddTpyeDialog dialog = new AddTpyeDialog(context, R.style.varDialogTheme, null);
        dialog.setCancelable(false);
        dialog.SetListener(new AddTpyeDialog.AletLstener() {
            @Override
            public void cancelClick(AddTpyeDialog diaglog) {
                diaglog.dismiss();
            }

            @Override
            public void okClick(AddTpyeDialog diaglog, String edt_msg) {
                LogUtil.ld(TAG, "select ADD type ~" + edt_msg);
                insertModel = new ConsumptionTypeModel();
                insertModel.setConsumption_name(edt_msg);
                insertModel.setType_icon_select(Constants.CONSUPTION_CUSTOM);
                insertModel.setConsumption_type(Constants.CONSUPTION_CUSTOM);
                modelList.add(modelList.size() - 1, insertModel);
                addModelListAndLayoutRound(modelList);
                AddDetailTypeAdapter.this.notifyDataSetChanged();
                diaglog.dismiss();
                //页面添加完成后将类别添加进本地数据库
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            ConsumptionHelper.getInstance(context).insertConsumptionType(insertModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        dialog.show();
    }

    //处理添加一条或者删除一条类型的时候可能会导致的 页面的 小圆圈发生改变
    private void addModelListAndLayoutRound(List<ConsumptionTypeModel> modelList) {
        int oldPage = pages;
        getPagerList(modelList);
        int newPage = pages;
        if (newPage > oldPage) {
            //ViewPage添加了一页
            if (oldPage <= 1) {
                //如果是从第一页添加到第二页则需要首次添加使用
                fragment.addRound(newPage);
                return;
            }
            LinearLayout.LayoutParams rl = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout pointLayout = fragment.insertRound(rl);
            fragment.round_layout.addView(pointLayout);
        } else if (oldPage > newPage) {
            //ViewPage减少了一页
            if (newPage >= 1) {
                fragment.round_layout.removeAllViews();
                return;
            }
            fragment.removeRound();
        }
        LogUtil.ld(TAG, "addModelListAndLayoutRound oldPage = " + oldPage + "  newPage = " + newPage);
    }

    //选择类型操作
    private void clickType(ConsumptionTypeModel cModel, List<ConsumptionTypeModel> mList) {
        if (!isShowInputMoth) {
            //显示键盘
            showInputMothd();
            isShowInputMoth = true;
        }
        if (cModel.getIsCheckd() == ConsumptionTypeModel.TYPE_CHECKD) {
            //取消当前选择
            cModel.setIsCheckd(ConsumptionTypeModel.TYPE_DEFULT);
            //隐藏键盘
            hidInputMothd();
            //取消选择的时候 将 selectModel 置为null
//            selectMode = null;
            //在被选择状态的时候将显示键盘状态置为false
            isShowInputMoth = false;
        } else {
            setAllDefultIcon(mList);
            //选中得model  设置给  SelectModel 好知道 消费的类型
//            selectMode = cModel;
            cModel.setIsCheckd(ConsumptionTypeModel.TYPE_CHECKD);
        }
    }

    //将所有的图标设置成默认Icon
    private void setAllDefultIcon(List<ConsumptionTypeModel> selectList) {
        for (ConsumptionTypeModel m : selectList) {
            m.setIsCheckd(ConsumptionTypeModel.TYPE_NO_CHECKD);
        }
    }

    //显示键盘
    private void showInputMothd() {
        ed_view.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    //隐藏键盘
    private void hidInputMothd() {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(ed_view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 处理数据显示给ViewPager
     *
     * @param modelList
     * @return
     */
    private void getPagerList(List<ConsumptionTypeModel> modelList) {
        //每页显示的个数
        int size = Constants.SET_TYPE_PAGE_SIZE;
        pages = (int) Math.ceil(modelList.size() / (float) size);
        typeList = new ArrayList<List<ConsumptionTypeModel>>();
        for (int i = 0; i < pages; i++) {
            typeList.add(new ArrayList<ConsumptionTypeModel>());
            for (int y = size * i; y < (size * (i + 1) > modelList.size() ? modelList.size() : size * (i + 1)); y++) {
                //每次进入将所有的选择状态置为默认状态
                modelList.get(y).setIsCheckd(ConsumptionTypeModel.TYPE_DEFULT);
                typeList.get(i).add(modelList.get(y));
            }
        }
    }

    private void showRemoveTypeDialog() {
        BaseDialog dialog = new BaseDialog(fragment.getActivity(),
                R.style.varDialogTheme, "提示", "您确定要删除该分类吗？");
        dialog.SetListener(new BaseDialog.AletLstener() {
            @Override
            public void cancelClick(BaseDialog diaglog) {
                diaglog.dismiss();
            }

            @Override
            public void okClick(BaseDialog diaglog) {
                if (vModel.getConsumption_type() == Constants.CONSUPTION_CUSTOM) {
                    LogUtil.ld(TAG, "onClik Long Item remove-->" + vModel.getConsumption_name());
                    cHelper.deldeteConsumptionType(vModel);
                    modelList.remove(vModel);
                    addModelListAndLayoutRound(modelList);
                    notifyDataSetChanged();
                }
                diaglog.dismiss();
            }
        });
        dialog.show();
    }
}
