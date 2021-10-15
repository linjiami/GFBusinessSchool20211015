package com.gfbusinessschool.fragment;

import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.activity.CourseCenterActivity;
import com.gfbusinessschool.adapter.AudioAdapter;
import com.gfbusinessschool.bean.AudioEntity;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.RecyclerviewLayoutBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MenegerEvent;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 音频列表
 */
public class AudioFragment extends BaseFragment<RecyclerviewLayoutBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private String classifyId;
    private AudioAdapter audioAdapter;
    private List<AudioEntity> listCourse =new ArrayList<>();
    private int currentPage;

    public AudioFragment() {
    }

    @Override
    protected void initView() {
        super.initView();
        classifyId =getArguments().getString("classifyId","");
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        audioAdapter =new AudioAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                AudioEntity entity =listCourse.get(position);
                if (entity==null)return;
                EventBus.getDefault().post(new MenegerEvent(MenegerEvent.TYPE_AUDIO_CLOSE_FLOATWIN));
                ARouter.getInstance().build(ARouterPath.AudioDetailsActivity)
                        .withString("courseId",entity.getId()).navigation();
            }
        });
        viewBinding.recyclerView.setAdapter(audioAdapter);
        viewBinding.swipeRefresh.setOnRefreshListener(this);

        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( audioAdapter!=null)
                    audioAdapter.setProgressBarVisiable(true);
                currentPage++;
                getAudioData(currentPage);
            }
        });
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefresh.setRefreshing(true);
        currentPage=1;
        getAudioData(currentPage);
    }

    @Override
    protected void initData() {
        super.initData();
        currentPage =1;
        getAudioData(currentPage);
    }

    private void getAudioData(int page) {
        if (Utils.isEmpty(classifyId)) {
            showNullListView(false,viewBinding.swipeRefresh,viewBinding.recyclerView,
                    viewBinding.noDataLayoutRecycler.noDataLayout,viewBinding.noDataLayoutRecycler.tvNodata,
                    viewBinding.noDataLayoutRecycler.iconNodata);
            return;
        }
        if (page==1 && listCourse !=null) {
            listCourse.clear();
        }
        Map<String,String> map =new HashMap<>();
        map.put("classifyId",classifyId);
        map.put("currPage",page+"");
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.AUDIO_LIST, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                showNullListView(true,viewBinding.swipeRefresh,viewBinding.recyclerView,
                        viewBinding.noDataLayoutRecycler.noDataLayout,viewBinding.noDataLayoutRecycler.tvNodata,
                        viewBinding.noDataLayoutRecycler.iconNodata);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefresh.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<AudioEntity> list = JSONArray.parseArray(response, AudioEntity.class);
                    if (list.size() < 15) {
                        audioAdapter.setProgressBarVisiable(false);
                    } else {
                        audioAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            showNullListView(false,viewBinding.swipeRefresh,viewBinding.recyclerView,
                                    viewBinding.noDataLayoutRecycler.noDataLayout,viewBinding.noDataLayoutRecycler.tvNodata,
                                    viewBinding.noDataLayoutRecycler.iconNodata);
                        }else {
                            viewBinding.noDataLayoutRecycler.noDataLayout.setVisibility(View.GONE);
                            viewBinding.recyclerView.setVisibility(View.VISIBLE);
                        }
                        listCourse =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (AudioEntity bean : list){
                            listCourse.add(bean);
                        }
                    }
                    audioAdapter.setmList(listCourse);
                    audioAdapter.notifyDataSetChanged();
                }else {
                    showNullListView(true,viewBinding.swipeRefresh,viewBinding.recyclerView,
                            viewBinding.noDataLayoutRecycler.noDataLayout,viewBinding.noDataLayoutRecycler.tvNodata,
                            viewBinding.noDataLayoutRecycler.iconNodata);
                }
            }
        });
    }
}