package com.gfbusinessschool.view;

import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ClassDetailPagerAdapter;
import com.gfbusinessschool.bean.ListPagerDataBean;
import com.gfbusinessschool.fragment.ListPagerReadCollectionFragment;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerListManager {
    public static final String TYPE_READCOLLECTION_NEW ="首页读书汇上新";
    private FragmentActivity activity;
    private ViewPager mViewPager;
    private PageIndicatorView pageIndicatorView;
    /**
     * 行数
     */
    private int countLines;
    private List<Fragment> listFragment =new ArrayList<>();
    private String typeLayout =TYPE_READCOLLECTION_NEW;//布局类型，1.TYPE_FIRSTPAGER ="首页" 2.TYPE_SXY ="商学院";
    private OnClickCallBack onClickCallBack;

    public ViewPagerListManager(FragmentActivity activity, ViewPager mViewPager, PageIndicatorView pageIndicatorView,
                                int countLines, String typeLayout, OnClickCallBack onClickCallBack) {
        this.activity = activity;
        this.mViewPager = mViewPager;
        this.pageIndicatorView = pageIndicatorView;
        this.countLines = countLines;
        this.typeLayout = typeLayout;
        this.onClickCallBack = onClickCallBack;
    }

    public void setmList(List<ListPagerDataBean> list) {
        listFragment.clear();
        //根据列表总数和行数，求列数
        int countColumns=0;
        if (list.size()%countLines==0)
            countColumns =list.size()/countLines;
        else
            countColumns =list.size()/countLines+1;

        pageIndicatorView.initIndicator(countColumns);
        pageIndicatorView.setSelectedPage(0);
        if (countLines>=list.size()){
            countLines =list.size();
        }
        if (Utils.getString(typeLayout).equals(TYPE_READCOLLECTION_NEW)){
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mViewPager.getLayoutParams();
            params.height = (int) (activity.getResources().getDimension(R.dimen.px17)
                    +activity.getResources().getDimension(R.dimen.px25)
                    +activity.getResources().getDimension(R.dimen.px16)
                    +activity.getResources().getDimension(R.dimen.px34)
                    +activity.getResources().getDimension(R.dimen.px25)
                    +activity.getResources().getDimension(R.dimen.px18)*2
                    +activity.getResources().getDimension(R.dimen.px28)
                    +activity.getResources().getDimension(R.dimen.px43)
                    +activity.getResources().getDimension(R.dimen.px17)
                    +activity.getResources().getDimension(R.dimen.px10)*2
            )*countLines;
            mViewPager.setLayoutParams(params);
        }
        String[] arrTitle =new String[countColumns];
        for (int i=0;i<countColumns;i++){
            List<ListPagerDataBean> itemList =new ArrayList<>();
            for (int j=0;j<countLines;j++){
                if ((j+i*countLines)<list.size())
                    itemList.add(list.get(j+i*countLines));
            }
            if (Utils.getString(typeLayout).equals(TYPE_READCOLLECTION_NEW)){
                ListPagerReadCollectionFragment listPagerFragment =new ListPagerReadCollectionFragment(onClickCallBack);
                listPagerFragment.setmList(itemList);
                listFragment.add(listPagerFragment);
            }
            arrTitle[i] =""+i;
        }
        mViewPager.setOffscreenPageLimit(countColumns);
        mViewPager.setAdapter(new ClassDetailPagerAdapter(activity.getSupportFragmentManager(),listFragment,arrTitle));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelectedPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



}
