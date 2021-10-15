package com.gfbusinessschool.adapter;

import android.content.Context;
import android.widget.TextView;

import com.gfbusinessschool.R;
import com.xj.marqueeview.base.CommonAdapter;
import com.xj.marqueeview.base.ViewHolder;

import java.util.List;

public class MarqueeViewAdapter extends CommonAdapter<String> {

    public MarqueeViewAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_marqueeview_text, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        TextView title = viewHolder.getView(R.id.title);
        title.setText(item);

    }
}