package com.gfbusinessschool.fragment;

import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.TeacherClassAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FragmentChampionIntroduceBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

/**
 * 冠军分享介绍
 */
public class ChampionIntroduceFragment extends BaseFragment<FragmentChampionIntroduceBinding>{

    @Override
    protected void initView() {
        super.initView();
    }

    public void refreshDataView(CourseBean courseDetailsBean) {
        viewBinding.titleCourseDetails.setText(Utils.getString(courseDetailsBean.getTitle()));
        viewBinding.shareContentCourse.setText(String.format(getString(R.string.place_championshare_msg),
                Utils.getString(courseDetailsBean.getName()),Utils.getString(courseDetailsBean.getPositionName()),
                Utils.getString(courseDetailsBean.getProvince()+"-"+ Utils.getString(courseDetailsBean.getCity())),
                Utils.getString(courseDetailsBean.getStoreName())));
        viewBinding.shareContent.setText(Utils.getString(courseDetailsBean.getContent()));
    }
}