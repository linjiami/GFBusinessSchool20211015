package com.gfbusinessschool.fragment;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.CourseCenterActivity;
import com.gfbusinessschool.activity.CourseDetailsActivity;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.adapter.ClassifyFirstpageAdapter;
import com.gfbusinessschool.adapter.CourseGridAdapter;
import com.gfbusinessschool.adapter.CourseHorizontalAdapter;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.adapter.GoodStaffAdapter;
import com.gfbusinessschool.adapter.MarqueeViewAdapter;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.BannerBean;
import com.gfbusinessschool.bean.ClassifyFirstpageBean;
import com.gfbusinessschool.bean.CompanyNoticeEntity;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.bean.GoodStaffBean;
import com.gfbusinessschool.bean.ListPagerDataBean;
import com.gfbusinessschool.bean.ReadCollectionEntity;
import com.gfbusinessschool.bean.VoteEntity;
import com.gfbusinessschool.databinding.FragmentFirstpageBinding;
import com.gfbusinessschool.dialog.PictureLookDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.FileTypeUtils;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import com.gfbusinessschool.view.ViewPagerListManager;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 */
public class FirstPageFragment extends BaseFragment<FragmentFirstpageBinding> implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener {
    private List<CourseBean> mlistStudy =new ArrayList<>();
    private CourseVerticalAdapter courseStudyAdapter;
    private int currentPage;
    List<ClassifyFirstpageBean> mList = new ArrayList<>();
    private int newResearchMsg;
    private ClassifyFirstpageAdapter classifyFirstpageAdapter;

    @Override
    protected void initView() {
        viewBinding.firstpageBg.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.lineHotVote.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.lineCourseRecommend.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.lineCourseNew.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.linechampionShareNew.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.lineGoodPersonal.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.linestudyingTitle.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.itemVoteSelection.hotVoteIcon.setCornerRadius(getResources().getDimension(R.dimen.px12));
        viewBinding.swipeRefreshFirstPager.setOnRefreshListener(this);
        viewBinding.moreCourseRecommend.setOnClickListener(this);
        viewBinding.searchBtn.setOnClickListener(this);
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        if (appUserEntity!=null)viewBinding.companyName.setText(appUserEntity.getCompanyName());
        if (appUserEntity!=null) GlideLoadUtils.load(getContext(), Utils.getString(appUserEntity.getCompanyLogo()),
                viewBinding.logoBusinessSchool,GlideLoadUtils.TYPE_PLACE_HOLDER_COMPANY_LOGO);

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) viewBinding.bannerFirstPage.getLayoutParams();
        int width = (int) (MyApplication.getInstance().screenWidth-getResources().getDimension(R.dimen.left_app)*2);
        lp.width =width;
        lp.height = width * 264 / 697;
        viewBinding.bannerFirstPage.setLayoutParams(lp);

        ConstraintLayout.LayoutParams lpVote = (ConstraintLayout.LayoutParams) viewBinding.itemVoteSelection.hotVoteIcon.getLayoutParams();
        lpVote.height = width * 264 / 697;
        viewBinding.itemVoteSelection.hotVoteIcon.setLayoutParams(lpVote);

        GridLayoutManager managerCourseRecommend =new GridLayoutManager(getContext(),2);
        managerCourseRecommend.setOrientation(RecyclerView.VERTICAL);
        viewBinding.courseRecommendRV.setLayoutManager(managerCourseRecommend);

        LinearLayoutManager managerCourseNew =new LinearLayoutManager(getContext());
        managerCourseNew.setOrientation(RecyclerView.HORIZONTAL);
        viewBinding.courseNewRV.setLayoutManager(managerCourseNew);

        LinearLayoutManager managerChampionShareNew =new LinearLayoutManager(getContext());
        managerChampionShareNew.setOrientation(RecyclerView.HORIZONTAL);
        viewBinding.championShareRV.setLayoutManager(managerChampionShareNew);

        LinearLayoutManager managerGoodPersonal =new LinearLayoutManager(getContext());
        managerGoodPersonal.setOrientation(RecyclerView.HORIZONTAL);
        viewBinding.goodPersonalRV.setLayoutManager(managerGoodPersonal);

        LinearLayoutManager managerStudy =new LinearLayoutManager(getContext());
        managerStudy.setOrientation(RecyclerView.VERTICAL);
        viewBinding.studyingRV.setLayoutManager(managerStudy);
        courseStudyAdapter =new CourseVerticalAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                        .withString("courseId",mlistStudy.get(position).getId()).navigation();
            }
        });
        viewBinding.studyingRV.setAdapter(courseStudyAdapter);

        viewBinding.nestedScrollViewFirstPage.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                viewBinding.studyingRV.setNestedScrollingEnabled(false);
                viewBinding.studyingRV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewBinding.studyingRV.setNestedScrollingEnabled(true);
                    }
                },1000);
                if (courseStudyAdapter!=null) courseStudyAdapter.setProgressBarVisiable(true);
                currentPage++;
                getCourseList(3,currentPage);
            }
        });
        viewBinding.layoutNoData.centerLayout.setOnClickListener(this);
        classifyFirstpageAdapter =new ClassifyFirstpageAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                if (mList.get(position)==null || Utils.isEmpty(mList.get(position).getType())) return;
                if (Utils.getString(mList.get(position).getType()).equals("1"))
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseCenter)
                            .withString("title", mList.get(position).getIconName()).navigation();
                else if (Utils.getString(mList.get(position).getType()).equals("2"))
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseCenter)
                            .withString("title", mList.get(position).getIconName())
                            .withString("typeView", CourseCenterActivity.TYPE_COURSE_CHAMPION_SHARING).navigation();
                else if (Utils.getString(mList.get(position).getType()).equals("3"))
                    ARouter.getInstance().build(ARouterPath.VoteSelectionActivity).navigation();
                else if (Utils.getString(mList.get(position).getType()).equals("4"))//读书汇
                    ARouter.getInstance().build(ARouterPath.ReadCollectionActivity).navigation();
                else if (Utils.getString(mList.get(position).getType()).equals("5"))
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_LiveList).navigation();
                else if (Utils.getString(mList.get(position).getType()).equals("6"))
                    ARouter.getInstance().build(ARouterPath.ResearchActivity).navigation();
                else if (Utils.getString(mList.get(position).getType()).equals("7"))
                    ARouter.getInstance().build(ARouterPath.StudyMapActivity).navigation();
                else if (Utils.getString(mList.get(position).getType()).equals("8"))
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseCenter)
                            .withString("title", mList.get(position).getIconName())
                            .withString("typeView", CourseCenterActivity.TYPEVIEW_AUDIO).navigation();
            }
        });
        viewBinding.classifyFirstpage.setAdapter(classifyFirstpageAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.centerLayout:
                initData();
                break;
            case R.id.moreCourseRecommend:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseCenter).navigation();
                break;
            case R.id.searchBtn:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseSearch).navigation();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getResearchNotice();
    }

    @Override
    protected void initData() {
        getClassifyData();
        requestBannerData();
        getCompanyNotice();
        getGoodStaff();
        getHotVote();
        currentPage=1;
        getCourseList(1,currentPage);
        getCourseList(2,currentPage);
        getCourseList(3,currentPage);
        getCourseList(4,currentPage);
        getReadCollectionList();
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshFirstPager.setRefreshing(true);
        initData();
        getResearchNotice();
    }
    /**
     * 获取banner
     */
    private void requestBannerData(){
        Map<String, String> map = new HashMap<>();
        map.put("showposition", "2");//1开屏展示 2首页顶部
        NetWorkUtils.getRequest(getContext(), InterfaceClass.FIRSTPAGE_BANNER, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.isEmpty(response)) return;
                List<BannerBean> recruitBeanList = JSONArray.parseArray(response, BannerBean.class);
                if (recruitBeanList==null || recruitBeanList.size()==0){
                    viewBinding.bannerFirstPage.setVisibility(View.GONE);
                    return;
                }
                viewBinding.bannerFirstPage.setVisibility(View.VISIBLE);
                viewBinding.bannerFirstPage.setAdapter(new BannerImageAdapter<BannerBean>(recruitBeanList) {
                    @Override
                    public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
                        GlideLoadUtils.load(getContext(),data.getImgUrl(), holder.imageView,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                    }
                })
                        .addBannerLifecycleObserver(getActivity())//添加生命周期观察者
                        .setIndicator(new CircleIndicator(getContext()));
                viewBinding.bannerFirstPage.setOnBannerListener((data, position) -> {
                    if (data==null)return;
                    if (data instanceof BannerBean){
                        BannerBean bannerBean = (BannerBean) data;
                        if (bannerBean ==null || Utils.isEmpty(bannerBean.getType()))return;
                        switch (bannerBean.getType()){
                            case "1":
                                if (!Utils.isEmpty(bannerBean.getJumpUrl()))
                                    ARouter.getInstance().build(ARouterPath.ACTIVITY_URL_WebView)
                                            .withString("url", bannerBean.getJumpUrl())
                                            .navigation();
                                break;
                            case "2":
                                if (!Utils.isEmpty(bannerBean.getId()))
                                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                                            .withString("courseId", bannerBean.getId()).navigation();
                                break;
                        }
                    }
                });
            }
        });
    }

    /**
     * 优秀员工
     */
    private void getGoodStaff(){
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.GOOD_STAFF, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<GoodStaffBean> mListGoodStaff =JSONArray.parseArray(response,GoodStaffBean.class);
                    GoodStaffAdapter goodStaffAdapter =new GoodStaffAdapter(getContext(), new OnClickCallBack() {
                        @Override
                        public void onClick(int position) {

                        }
                    });
                    goodStaffAdapter.setmList(mListGoodStaff);
                    viewBinding.goodPersonalRV.setAdapter(goodStaffAdapter);
                }
            }
        });
    }

    /**
     * 企业公告
     */
    private void getCompanyNotice(){
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.COMPANY_NOTICE, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<CompanyNoticeEntity> noticeEntityList = JSONArray.parseArray(response, CompanyNoticeEntity.class);
                    if (noticeEntityList==null || noticeEntityList.size()==0){
                        viewBinding.layoutNoticeCompany.setVisibility(View.GONE);
                        return;
                    }
                    viewBinding.layoutNoticeCompany.setVisibility(View.VISIBLE);
                    List<String> listAudio = new ArrayList<>();
                    if (noticeEntityList.size() != 0) {
                        for (CompanyNoticeEntity entity : noticeEntityList) {
                            listAudio.add(entity.getTitle());
                        }
                        MarqueeViewAdapter adapter = new MarqueeViewAdapter(getContext(), listAudio);
                        adapter.setOnItemClickListener((position, view) -> {
                            ARouter.getInstance().build(ARouterPath.ACTIVITY_NoticeCompanyDetails)
                                    .withString("titleNotice", noticeEntityList.get(position).getTitle())
                                    .withString("id", noticeEntityList.get(position).getId()).navigation();
                        });
                        viewBinding.marqueeView.setAdapter(adapter);
                    }
                }
            }
        });
    }

    /**
     * 课程列表
     * @param _type 1:课程推荐 2：课程上新 3大家都在学 4冠军分享上新
     */
    private void getCourseList(int _type ,int page){
        String url =null;
        Map<String,String> map=new HashMap<>();
        if (_type==1){
            url =InterfaceClass.COURSE_RECOMMEND;
            map.put("limit","4");
        }else if (_type==2) {
            url = InterfaceClass.COURSE_NEW;
            map.put("currPage","1");
            map.put("limit","10");
        }else if (_type==3) {
            url = InterfaceClass.COURSE_STUDY;
            map.put("currPage",""+page);
            if (page==1 && mlistStudy!=null)
                mlistStudy.clear();
        }else if (_type==4) {
            url = InterfaceClass.CHANPIONSHARE_NEW;
        }
        NetWorkUtils.getResultArray(getContext(), url, map, new NetWorkCallback() {

            @Override
            public void onRequestError() {
                super.onRequestError();
                viewBinding.swipeRefreshFirstPager.setRefreshing(false);
                if (_type==1) viewBinding.courseRecommend.setVisibility(View.GONE);
                else if (_type==2) viewBinding.courseNew.setVisibility(View.GONE);
                else if (_type==3) {
                    viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
                    viewBinding.contentLayout.setVisibility(View.GONE);
                }else if (_type==4) viewBinding.championShareRV.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<CourseBean> _list =JSONArray.parseArray(response,CourseBean.class);
                    if (_list==null) _list =new ArrayList<>();
                    if (_type==1){
                        if (_list.size()==0){
                            viewBinding.courseRecommend.setVisibility(View.GONE);
                        }else {
                            viewBinding.courseRecommend.setVisibility(View.VISIBLE);
                            List<CourseBean> final_list = _list;
                            CourseGridAdapter courseGridAdapter =new CourseGridAdapter(getContext(), new OnClickCallBack() {
                                @Override
                                public void onClick(int position) {
                                    CourseBean courseBean =final_list.get(position);
                                    if (courseBean==null) return;
                                    if (courseBean.getType()==1){
                                        ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                                                .withString("courseId", final_list.get(position).getId()).navigation();
                                    }else if (courseBean.getType()==2){
                                        ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                                                .withString("typeView", CourseDetailsActivity.TYPE_VIEW_GUANJUN_SHARE)
                                                .withString("courseId", final_list.get(position).getId())
                                                .navigation();
                                    }
                                }
                            });
                            courseGridAdapter.setmList(_list);
                            viewBinding.courseRecommendRV.setAdapter(courseGridAdapter);
                        }
                    }else  if (_type==2){
                        if (_list.size()==0){
                            viewBinding.courseNew.setVisibility(View.GONE);
                        }else {
                            viewBinding.courseNew.setVisibility(View.VISIBLE);
                            List<CourseBean> final_list1 = _list;
                            CourseHorizontalAdapter courseHorizontalAdapter =new CourseHorizontalAdapter(getContext(), new OnClickCallBack() {
                                @Override
                                public void onClick(int position) {
                                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                                            .withString("courseId", final_list1.get(position).getId()).navigation();
                                }
                            });
                            courseHorizontalAdapter.setmList(_list);
                            viewBinding.courseNewRV.setAdapter(courseHorizontalAdapter);
                        }
                    }else if (_type==3){
                        viewBinding.layoutNoData.noDataLayout.setVisibility(View.GONE);
                        viewBinding.contentLayout.setVisibility(View.VISIBLE);
                        if (_list.size()<15){
                            courseStudyAdapter.setProgressBarVisiable(false);
                        }else {
                            courseStudyAdapter.setProgressBarVisiable(true);
                        }
                        if (page == 1) {
                            mlistStudy = _list;
                        } else {
                            if (_list.size() != 0) {
                                for (CourseBean entity : _list) {
                                    mlistStudy.add(entity);
                                }
                            }else {
                                currentPage--;
                            }
                        }
                        courseStudyAdapter.setmList(mlistStudy);
                        courseStudyAdapter.notifyDataSetChanged();
                    }else if (_type==4){
                        if (_list.size()==0){
                            viewBinding.championShareRV.setVisibility(View.GONE);
                        }else {
                            viewBinding.championShareRV.setVisibility(View.VISIBLE);
                            for (CourseBean bean: _list){
                                bean.setName(bean.getTitle());
                                bean.setLogoUrl(bean.getCoverImgUrl());
                            }
                            List<CourseBean> final_list1 = _list;
                            CourseHorizontalAdapter courseHorizontalAdapter =new CourseHorizontalAdapter(getContext(), new OnClickCallBack() {
                                @Override
                                public void onClick(int position) {
                                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                                            .withString("courseId",final_list1.get(position).getId())
                                            .withString("typeView", CourseDetailsActivity.TYPE_VIEW_GUANJUN_SHARE).navigation();
                                }
                            });
                            courseHorizontalAdapter.setmList(_list);
                            viewBinding.championShareRV.setAdapter(courseHorizontalAdapter);
                        }
                    }
                } else {
                    if (_type==3){
                        viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
                        viewBinding.contentLayout.setVisibility(View.GONE);
                    }else if (_type==4)
                        viewBinding.championShareRV.setVisibility(View.GONE);
                }
                viewBinding.swipeRefreshFirstPager.setRefreshing(false);
            }
        });
    }

    /**
     * 问卷调研消息提醒
     */
    private void getResearchNotice(){
        NetWorkUtils.getResultString(getContext(), InterfaceClass.RESEARCH_NOTICE, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    if (!Utils.isEmpty(response) && mList!=null && mList.size()!=0){
                        for (int i=0;i<mList.size();i++){
                            if (mList.get(i)!=null &&  Utils.getString(mList.get(i).getType()).equals("6")){
                                if (!Utils.isEmpty(response))
                                    newResearchMsg =Integer.parseInt(response);
                                mList.get(i).setNewNoticeCount(newResearchMsg);
                                if (classifyFirstpageAdapter!=null) {
                                    classifyFirstpageAdapter.setmList(mList);
                                    classifyFirstpageAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 热门投票
     */
    private void getHotVote(){
        NetWorkUtils.getRequest(getContext(), InterfaceClass.VOTE_ACTIVE_HOT, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    VoteEntity voteEntity = JSONObject.parseObject(response,VoteEntity.class);
                    if (voteEntity==null) {
                        viewBinding.hotVote.setVisibility(View.GONE);
                        return;
                    }
                    viewBinding.hotVote.setVisibility(View.VISIBLE);
                    GlideLoadUtils.load(getContext(),voteEntity.getImgUrl(),viewBinding.itemVoteSelection.hotVoteIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                    viewBinding.itemVoteSelection.titleVote.setText(Utils.getString(voteEntity.getActiveName()));
                    if (Utils.isEmpty(voteEntity.getVoteUsers()))
                        viewBinding.itemVoteSelection.countVote.setText(String.format(getResources().getString(R.string.place_people),"0"));
                    else
                        viewBinding.itemVoteSelection.countVote.setText(String.format(getResources().getString(R.string.place_people),voteEntity.getVoteUsers()));
                    viewBinding.itemVoteSelection.timeVote.setText(String.format(getResources().getString(R.string.place_start_end_time),
                            Utils.getTimeFormat(voteEntity.getStartDate()),Utils.getTimeFormat(voteEntity.getEndDate())));
                    viewBinding.itemVoteSelection.infoVote.setText(Utils.getString(voteEntity.getIntroduce()));
                    viewBinding.itemVoteSelection.layoutVoteSelection.setOnClickListener(v -> {
                        ARouter.getInstance().build(ARouterPath.VoteDetailsActivity)
                                .withParcelable("voteEntity",voteEntity).navigation();
                    });
                }
            }
        });
    }

    /**
     * 分类icon
     */
    private void getClassifyData(){
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.HOMEPAGER_CLASSIFY_ICONS, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    mList =JSONArray.parseArray(response,ClassifyFirstpageBean.class);
                    if (mList==null) mList =new ArrayList<>();
                    if (mList.size()==0){
                        viewBinding.classifyLayoutMain.setVisibility(View.GONE);
                    }else {
                        viewBinding.classifyLayoutMain.setVisibility(View.VISIBLE);
                        if (mList.size()<=4){
                            GridLayoutManager managerClassify =new GridLayoutManager(getContext(),mList.size());
                            managerClassify.setOrientation(RecyclerView.VERTICAL);
                            viewBinding.classifyFirstpage.setLayoutManager(managerClassify);
                            viewBinding.classifyFirstpage.setNestedScrollingEnabled(false);
                        }else if (mList.size()>4 && mList.size()<=8){
                            GridLayoutManager managerClassify =new GridLayoutManager(getContext(),4);
                            managerClassify.setOrientation(RecyclerView.VERTICAL);
                            viewBinding.classifyFirstpage.setLayoutManager(managerClassify);
                            viewBinding.classifyFirstpage.setNestedScrollingEnabled(false);
                        }else {
                            viewBinding.classifyLayoutMain.setVisibility(View.VISIBLE);
                            GridLayoutManager managerClassify =new GridLayoutManager(getContext(),2);
                            managerClassify.setOrientation(RecyclerView.HORIZONTAL);
                            viewBinding.classifyFirstpage.setLayoutManager(managerClassify);
                            viewBinding.classifyFirstpage.setNestedScrollingEnabled(true);
                        }
                    }
                    for (int i=0;i<mList.size();i++){
                        if (mList.get(i)!=null &&  Utils.getString(mList.get(i).getType()).equals("6")){
                            mList.get(i).setNewNoticeCount(newResearchMsg);
                        }
                    }
                    boolean isShowChampion =false;
                    for (ClassifyFirstpageBean bean : mList){
                        if (bean!=null &&  Utils.getString(bean.getType()).equals("2"))
                            isShowChampion =true;
                    }
                    MyApplication.getInstance().setShowChamPionShare(isShowChampion);
                    boolean isShowReadCollectionShare =false;
                    for (ClassifyFirstpageBean bean : mList){
                        if (bean!=null &&  Utils.getString(bean.getType()).equals("4"))
                            isShowReadCollectionShare =true;
                    }
                    MyApplication.getInstance().setShowReadCollectionShare(isShowReadCollectionShare);
                    boolean isShowResearch =false;
                    for (ClassifyFirstpageBean bean : mList){
                        if (bean!=null &&  Utils.getString(bean.getType()).equals("6"))
                            isShowResearch =true;
                    }
                    MyApplication.getInstance().setShowResearch(isShowResearch);
                    if (classifyFirstpageAdapter!=null){
                        classifyFirstpageAdapter.setmList(mList);
                        classifyFirstpageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void getReadCollectionList() {
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.READCOLLECTION_NEW, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.readCollectionLayout.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<ListPagerDataBean> list =JSONArray.parseArray(response,ListPagerDataBean.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size()==0){
                        viewBinding.readCollectionLayout.setVisibility(View.GONE);
                    }else {
                        viewBinding.readCollectionLayout.setVisibility(View.VISIBLE);
                        List<ListPagerDataBean> final_list1 = list;
                        ViewPagerListManager courseViewPagerListManager = new ViewPagerListManager(getActivity(),
                                viewBinding.readCollectionViewPager, viewBinding.readCollectionIndicator,
                                2, ViewPagerListManager.TYPE_READCOLLECTION_NEW, new OnClickCallBack() {
                            @Override
                            public void onClick(ReadCollectionEntity.Entity entity) {
                                if (entity!=null)
                                    ARouter.getInstance().build(ARouterPath.ReadCollectionDetailsActivity)
                                            .withString("shareId",entity.getId()).navigation();
                            }

                            @Override
                            public void onClickReadCollection(ReadCollectionEntity.Entity entity) {
                                if (entity==null) return;
                                if (FileTypeUtils.isImageFileType(entity.getFileUrl())){
                                    if (!Utils.isEmpty(entity.getFileUrl())){
                                        PictureLookDialog dialog =new PictureLookDialog(getActivity(),entity.getFileUrl());
                                        dialog.setType(PictureLookDialog.TYPE_READCOLLECTION);
                                        dialog.show();
                                    }
                                }else if (FileTypeUtils.isVideoFileType(entity.getFileUrl())){
                                    ARouter.getInstance().build(ARouterPath.VideoFullScreenActivity)
                                            .withString("videoUrl",entity.getFileUrl()).navigation();
                                }else if (Utils.getString(entity.getFileUrl()).toLowerCase().endsWith("pdf")){
                                    ARouter.getInstance().build(ARouterPath.PdfViewerActvity)
                                            .withString("urlPdf",entity.getFileUrl())
                                            .withString("pdfTitle",entity.getTitle()).navigation();
                                }else if (FileTypeUtils.isWordFileType(entity.getFileUrl()) ||
                                        FileTypeUtils.isPPTFileType(entity.getFileUrl())) {
                                    ARouter.getInstance().build(ARouterPath.WordViewerActivity)
                                            .withString("urlPdf",entity.getFileUrl())
                                            .withString("pdfName",entity.getTitle())
                                            .withString("pdfTitle",entity.getTitle()).navigation();
                                }
                            }
                        });
                        courseViewPagerListManager.setmList(list);
                    }
                }
            }
        });
    }
}