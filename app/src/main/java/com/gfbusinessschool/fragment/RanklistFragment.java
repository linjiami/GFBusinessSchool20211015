package com.gfbusinessschool.fragment;

import androidx.fragment.app.Fragment;

import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ClassDetailPagerAdapter;
import com.gfbusinessschool.databinding.FragmentRanklistBinding;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RanklistFragment extends BaseFragment<FragmentRanklistBinding>{

    @Override
    protected void initView() {
        viewBinding.introductionTabCourse.setSelectedTabIndicatorColor(Utils.getThemeColor(getContext()));
        String[] arrTitle = getResources().getStringArray(R.array.ranklistTab);
        List<Fragment> fragmentList =new ArrayList<>();
        fragmentList.add(new RanklistStoreFragment());
        fragmentList.add(new RanklistCourseFragment());
        RanklistStoreFragment personalFragment =new RanklistStoreFragment();
        personalFragment.setTypeView(RanklistStoreFragment.TYPEVIEW_RANKLIST_PERSONEL);
        fragmentList.add(personalFragment);
        viewBinding.viewPagerCourse.setAdapter(new ClassDetailPagerAdapter(getFragmentManager(),fragmentList,arrTitle));
        viewBinding.introductionTabCourse.setupWithViewPager(viewBinding.viewPagerCourse);
    }

    @Override
    protected void initData() {

    }
}