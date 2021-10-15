package com.gfbusinessschool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.PieChatEntity;
import com.gfbusinessschool.databinding.ItemPiechatBinding;
import com.gfbusinessschool.utils.Utils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PieChatAdapter extends BassRecyclerAdapter{
    private List<PieChatEntity> mlist =new ArrayList<>();
    private List<Integer> listColor =new ArrayList<>();

    public PieChatAdapter(Context mContext) {
        super(mContext);
    }

    public void setMlist(List<PieChatEntity> mlist) {
        this.mlist = mlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPiechatBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        super.onBindViewHolder(_holder, position);
        if (_holder instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) _holder;
            PieChatEntity entity =mlist.get(position);
            if (entity==null) return;
            List<PieChatEntity.TableBean> list =entity.getList();
            if (list==null) return;
            List<PieEntry> pieEntryList = new ArrayList<>();
            int totalTable =0;
            for (PieChatEntity.TableBean tableBean : list){
                totalTable =totalTable+tableBean.getCnt();
            }
            if (list.size()>40){
                int lessThan1=0;//<2.5%数据
                for (PieChatEntity.TableBean tableBean : list){
                    if (tableBean.getCnt()*1.0/totalTable>0.025) {
                        pieEntryList.add(new PieEntry(tableBean.getCnt(), Utils.getString(tableBean.getParams())
                                + "(" + tableBean.getCnt() + ")"));
                    }else {
                        lessThan1 =lessThan1+tableBean.getCnt();
                    }
                }
                if (lessThan1>0){
                    pieEntryList.add(new PieEntry(lessThan1, "其他(" + lessThan1 + ")"));
                }
            }else {
                for (PieChatEntity.TableBean tableBean : list){
                    pieEntryList.add(new PieEntry(tableBean.getCnt(),Utils.getString(tableBean.getParams())
                            +"("+tableBean.getCnt()+")"));
                }
            }
            PieData pieData = setPieData(pieEntryList,totalTable);//设置PieData
            holder.binding.pieChartStaff.setCenterText(entity.getTitle());
            setLegend(holder.binding.pieChartStaff);//设置图例描述
            holder.binding.pieChartStaff.setData(pieData);
            holder.binding.pieChartStaff.invalidate();
        }
    }

    @Override
    public int getItemCount() {
        return mlist==null?0:mlist.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        ItemPiechatBinding binding;
        public ViewHolder(@NonNull @NotNull ItemPiechatBinding itemView) {
            super(itemView.getRoot());
            this.binding=itemView;
            listColor.add(Color.rgb(238, 190, 111));
            listColor.add(Color.rgb(241, 203, 141));
            listColor.add(Color.rgb(244, 212, 161));
            listColor.add(Color.rgb(250, 235, 212));
            for (int c : ColorTemplate.LIBERTY_COLORS)
                listColor.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                listColor.add(c);
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                listColor.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                listColor.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                listColor.add(c);
            initParams(binding.pieChartStaff);
        }
    }

    private void initParams(PieChart pieChartStaff) {
        double width = MyApplication.getInstance().screenWidth*500.0/750;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pieChartStaff.getLayoutParams();
        params.width = (int) width;
        params.height = (int) width;
        pieChartStaff.setLayoutParams(params);
    }

    /**
     * 设置PieData
     * @param pieEntryList 表格数据
     * @param totalTable 总数
     * @return
     */
    private PieData setPieData(List<PieEntry> pieEntryList, int totalTable){
        PieDataSet set = new PieDataSet(pieEntryList,"");
        List<Integer> colors = new ArrayList<>();
        for (int i=0;i<pieEntryList.size();i++){
            colors.add(listColor.get(i));
        }
        set.setColors(colors);//添加颜色

        set.setSliceSpace(2f);//切割空间
        set.setValueTextSize(10f);
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//值在图表中的位置
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int)value*100/totalTable)+"%";
            }

        });
        PieData pieData = new PieData(set);

        return pieData;
    }

    /**
     * 设置图例描述
     */
    private void setLegend(PieChart pieChart){
        Legend legend = pieChart.getLegend();//获得图例的描述
        legend.setEnabled(false);//隐藏颜色描述
        pieChart.setRotationEnabled(false);//禁止转动
        pieChart.setDrawHoleEnabled(true);//中间留空洞
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(true);//使用label
        pieChart.setEntryLabelTextSize(10f);
        pieChart.setExtraOffsets(2f, 10f, 2f, 10f);//距离
        pieChart.setHoleRadius(45f);
        pieChart.setDrawCenterText(true);//饼状图中间可以添加文字
        Description description = pieChart.getDescription();//获得图表的描述
        description.setText("");
    }
}