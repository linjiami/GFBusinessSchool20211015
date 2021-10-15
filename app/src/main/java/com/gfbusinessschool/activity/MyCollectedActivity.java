package com.gfbusinessschool.activity;

import androidx.fragment.app.Fragment;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ClassDetailPagerAdapter;
import com.gfbusinessschool.databinding.ActivityLiveListBinding;
import com.gfbusinessschool.databinding.ActivityMycollectedBinding;
import com.gfbusinessschool.fragment.MyCollectedCourseFragment;
import com.gfbusinessschool.fragment.MyCollectedReadColFragment;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的-收藏课程列表
 */
@Route(path = ARouterPath.ACTIVITY_MyCollected)
public class MyCollectedActivity extends BaseActivity<ActivityMycollectedBinding>{

    @Override
    protected void initView() {
        viewBinding.iconBackLive.setOnClickListener(v -> {
            finish();
        });
        viewBinding.tabLayout.setSelectedTabIndicatorColor(Utils.getThemeColor(getApplicationContext()));
        Utils.setImageViewTint(getApplicationContext(),viewBinding.iconBackTitleBar);
        String[] arrTitle = getResources().getStringArray(R.array.myCollectedTab);
        List<Fragment> fragmentList =new ArrayList<>();
        fragmentList.add(new MyCollectedCourseFragment());
        fragmentList.add(new MyCollectedReadColFragment());
        viewBinding.viewPager.setAdapter(new ClassDetailPagerAdapter(getSupportFragmentManager(),fragmentList,arrTitle));
        viewBinding.tabLayout.setupWithViewPager(viewBinding.viewPager);
    }

    @Override
    protected void initData() {

    }
}