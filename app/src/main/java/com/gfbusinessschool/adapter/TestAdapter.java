package com.gfbusinessschool.adapter;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.TestQuestionsBean;
import com.gfbusinessschool.databinding.ItemTestBinding;
import com.gfbusinessschool.databinding.ItemTestQuestionansewerBinding;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页分类（课程中心、冠军分享等分类）
 */
public class TestAdapter extends BassRecyclerAdapter {
    private List<TestQuestionsBean> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;

    public TestAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setmList(List<TestQuestionsBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList!=null && mList.get(position)!=null && Utils.getString(mList.get(position).getType()).equals("3"))//问答题
            return NORMAL_VIEW2;
        else
            return NORMAL_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==NORMAL_VIEW)
            return new MyViewHodler(ItemTestBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new RequestAnswerViewHodler(ItemTestQuestionansewerBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            TestQuestionsBean bean =mList.get(position);
            if (bean==null) return;
            //问题类型（1单选2多选3简答）
            if (Utils.getString(bean.getType()).equals("1")){//单选题
                myHolder.binding.singleSelectRG.setVisibility(View.VISIBLE);
                myHolder.binding.singleSelectRG.removeAllViews();
                myHolder.binding.moreSelect.setVisibility(View.GONE);
                myHolder.binding.titleTest.setText(String.format(mContext.getString(R.string.test_title),
                        ""+(position+1)+"、",Utils.getString(bean.getName()),
                        mContext.getString(R.string.test_single_select)));
               if (bean.getQuestionSelectList()!=null && bean.getQuestionSelectList().size()>0){
                   List<RadioButton> radioButtonList =new ArrayList<>();
                   for (int i=0;i<bean.getQuestionSelectList().size();i++){
                       RadioButton button =new RadioButton(mContext);
                       button.setButtonDrawable(null);
                       button.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext,
                               R.drawable.selector_radio),null,null,null);
                       button.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.px20));
                       button.setText(Utils.getString(bean.getQuestionSelectList().get(i)));
                       button.setTextColor(mContext.getResources().getColor(R.color.color_969595));
                       button.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.SP15));
                       RadioGroup.LayoutParams params =new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                       params.leftMargin = (int) mContext.getResources().getDimension(R.dimen.px60);
                       params.rightMargin = (int) mContext.getResources().getDimension(R.dimen.px60);
                       button.setLayoutParams(params);
                       myHolder.binding.singleSelectRG.addView(button);
                       radioButtonList.add(button);
                   }
                   for (int i=0;i<radioButtonList.size();i++){
                       RadioButton button = radioButtonList.get(i);
                       if (bean.getCheckList().get(i))
                           button.setChecked(true);
                       else
                           button.setChecked(false);
                       int finalI = i;
                       button.setOnClickListener(v -> {
                           bean.getCheckList().set(finalI,button.isChecked());
                           if (onClickCallBack!=null) onClickCallBack.onClickTestAnswer(position, finalI,button.isChecked());
                       });
                   }
               }
            }else if (Utils.getString(bean.getType()).equals("2")){//多选
                myHolder.binding.singleSelectRG.setVisibility(View.GONE);
                myHolder.binding.moreSelect.setVisibility(View.VISIBLE);
                myHolder.binding.moreSelect.removeAllViews();
                myHolder.binding.titleTest.setText(String.format(mContext.getString(R.string.test_title),
                        ""+(position+1)+"、",Utils.getString(bean.getName()),
                        mContext.getString(R.string.test_more_select)));
                if (bean.getQuestionSelectList()!=null && bean.getQuestionSelectList().size()>0){
                    List<CheckBox> radioButtonList =new ArrayList<>();
                    for (int i=0;i<bean.getQuestionSelectList().size();i++){
                        CheckBox button =new CheckBox(mContext);
                        button.setTextColor(mContext.getResources().getColor(R.color.color_969595));
                        button.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.SP15));
                        button.setButtonDrawable(null);
                        button.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext,
                                R.drawable.selector_radio),null,null,null);
                        button.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.px20));
                        button.setText(Utils.getString(bean.getQuestionSelectList().get(i)));
                        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = (int) mContext.getResources().getDimension(R.dimen.px60);
                        params.rightMargin = (int) mContext.getResources().getDimension(R.dimen.px60);
                        button.setLayoutParams(params);
                        myHolder.binding.moreSelect.addView(button);
                        radioButtonList.add(button);
                    }
                    for (int i=0;i<radioButtonList.size();i++){
                        CheckBox button = radioButtonList.get(i);
                        if (bean.getCheckList().get(i))
                            button.setChecked(true);
                        else
                            button.setChecked(false);
                        int finalI = i;
                        button.setOnClickListener(v -> {
                            bean.getCheckList().set(finalI,button.isChecked());
                            if (onClickCallBack!=null) onClickCallBack.onClickTestAnswer(position, finalI,button.isChecked());
                        });
                    }
                }
            }

        } else {
            RequestAnswerViewHodler myHolder = (RequestAnswerViewHodler) holder;
            TestQuestionsBean bean =mList.get(position);
            if (bean==null) return;
            myHolder.binding.titleTest.setText(String.format(mContext.getString(R.string.test_title),
                    ""+(position+1)+"、",Utils.getString(bean.getName()),
                    mContext.getString(R.string.test_questansewer)));
            myHolder.binding.contentTest.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (onClickCallBack!=null) onClickCallBack.onClickEdittext(position, myHolder.binding.contentTest.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        ItemTestBinding binding;

        public MyViewHodler(@NonNull ItemTestBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }
    }

    private class RequestAnswerViewHodler extends RecyclerView.ViewHolder {
        ItemTestQuestionansewerBinding binding;

        public RequestAnswerViewHodler(@NonNull ItemTestQuestionansewerBinding binding) {
            super(binding.getRoot());
           this.binding =binding;
        }
    }

}
