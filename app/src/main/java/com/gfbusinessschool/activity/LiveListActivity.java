package com.gfbusinessschool.activity;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gfbusinessschool.R;
import com.gfbusinessschool.fragment.LiveFragment;
import com.gfbusinessschool.adapter.ClassDetailPagerAdapter;
import com.gfbusinessschool.databinding.ActivityLiveListBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;
@Route(path = ARouterPath.ACTIVITY_LiveList)
public class LiveListActivity extends BaseActivity<ActivityLiveListBinding>{

    @Override
    protected void initView() {
        super.initView();
        viewBinding.iconBackLive.setOnClickListener(v -> {
            finish();
        });
        viewBinding.tabLayout.setSelectedTabIndicatorColor(Utils.getThemeColor(getApplicationContext()));
        Utils.setImageViewTint(getApplicationContext(),viewBinding.iconBackTitleBar);
        String[] arrTitle = getResources().getStringArray(R.array.liveTab);
        List<Fragment> fragmentList =new ArrayList<>();
        fragmentList.add(new LiveFragment());
        LiveFragment historyLiveFragment =new LiveFragment();
        historyLiveFragment.setTypeView(LiveFragment.TYPEVIEW_HISTORY_LIVE);
        fragmentList.add(historyLiveFragment);
        viewBinding.viewPager.setAdapter(new ClassDetailPagerAdapter(getSupportFragmentManager(),fragmentList,arrTitle));
        viewBinding.tabLayout.setupWithViewPager(viewBinding.viewPager);
    }

    @Override
    protected void initData() {
        super.initData();
    }
}