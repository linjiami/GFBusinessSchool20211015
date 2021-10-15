package com.gfbusinessschool.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.adapter.AreaClassifyAdapter;
import com.gfbusinessschool.bean.StoreAddressEntity;
import com.gfbusinessschool.databinding.FragmentAreaclassifyBinding;
import com.gfbusinessschool.utils.ARouterPath;

import java.util.ArrayList;
import java.util.List;

public class AreaClassifyFragment extends BaseFragment<FragmentAreaclassifyBinding>{
    private List<StoreAddressEntity> mlist=new ArrayList<>();

    public void setMlist(List<StoreAddressEntity> mlist) {
        this.mlist = mlist;
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
    }

    @Override
    protected void initData() {
        if (viewBinding==null) return;
        AreaClassifyAdapter adapter =new AreaClassifyAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position, StoreAddressEntity storeAddressEntity) {
                ARouter.getInstance().build(ARouterPath.StoreListActivity)
                        .withString("cityId",storeAddressEntity.getId())
                        .withString("cityName",storeAddressEntity.getAddressName())
                        .navigation();
            }
        });
        adapter.setList(mlist);
        viewBinding.recyclerView.setAdapter(adapter);
    }
}