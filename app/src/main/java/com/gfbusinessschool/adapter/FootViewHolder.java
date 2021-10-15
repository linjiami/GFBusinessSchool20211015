package com.gfbusinessschool.adapter;


import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;


public class FootViewHolder extends RecyclerView.ViewHolder {
    public View footview_root_layout;
    public TextView loading_text;
    public ProgressBar progress_bar;

    public FootViewHolder(@NonNull FooterLoadingLayoutBinding binding) {
        super(binding.getRoot());
        footview_root_layout =binding.footviewRootLayout;
        loading_text =binding.footviewLoadingText;
        progress_bar =binding.footviewProgressBar;
    }
}
