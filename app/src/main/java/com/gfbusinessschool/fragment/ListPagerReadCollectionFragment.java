package com.gfbusinessschool.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.adapter.ReadCollectionAdapter;
import com.gfbusinessschool.bean.ListPagerDataBean;
import com.gfbusinessschool.bean.ReadCollectionEntity;
import com.gfbusinessschool.databinding.RecyclerviewOnlyBinding;

import java.util.ArrayList;
import java.util.List;

public class ListPagerReadCollectionFragment extends BaseFragment<RecyclerviewOnlyBinding> {
    private List<ListPagerDataBean> mList =new ArrayList<>();
    private OnClickCallBack onClickCallBack;

    public ListPagerReadCollectionFragment(OnClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }

    public void setmList(List<ListPagerDataBean> mList) {
        this.mList = mList;
    }

    @Override
    protected void initView() {
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
    }

    @Override
    protected void initData() {
        if (viewBinding!=null && mList!=null && mList.size()>0){
            ReadCollectionAdapter courseAdapter =new ReadCollectionAdapter(getContext(),ReadCollectionAdapter.TYPE_READCOLLECTION_NEW,onClickCallBack);
            List<ReadCollectionEntity.Entity> _list =new ArrayList<>();
            for (ListPagerDataBean bean : mList){
                ReadCollectionEntity.Entity courseSingleBean =new ReadCollectionEntity.Entity();
                courseSingleBean.setId(bean.getId());
                courseSingleBean.setHeadImgUrl(bean.getHeadImgUrl());
                courseSingleBean.setUserName(bean.getUserName());
                courseSingleBean.setTitle(bean.getTitle());
                courseSingleBean.setCreateDate(bean.getCreateDate());
                courseSingleBean.setContent(bean.getContent());
                courseSingleBean.setFileUrl(bean.getFileUrl());
                courseSingleBean.setStatus(bean.getStatus());
                courseSingleBean.setStudyAmount(bean.getStudyAmount());
                courseSingleBean.setPassRate(bean.getPassRate());
                _list.add(courseSingleBean);
            }
            courseAdapter.setMlist(_list);
            viewBinding.recyclerView.setAdapter(courseAdapter);
        }
    }
}
