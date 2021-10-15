package com.gfbusinessschool.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CertificateAdapter;
import com.gfbusinessschool.bean.CertificateEntity;
import com.gfbusinessschool.bean.CertificateListEntity;
import com.gfbusinessschool.databinding.LayoutListBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的证书列表
 */
@Route(path = ARouterPath.MyCertificateActivity)
public class MyCertificateActivity extends BaseActivity<LayoutListBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private CertificateAdapter certificateAdapter;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.my_certificate));
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        certificateAdapter =new CertificateAdapter(getApplicationContext());
        viewBinding.recyclerView.setAdapter(certificateAdapter);
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshLayout.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.MY_CERTIFICATION, new HashMap<>(), new NetWorkCallback() {

            @Override
            public void onRequestError() {
                showNullListView(true);
            }

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    CertificateListEntity certificateListEntity = JSONObject.parseObject(response,CertificateListEntity.class);
                    if (certificateListEntity==null) {
                        showNullListView(true);
                        return;
                    }
                    boolean isHaveCertificate =false;//默认没有证书
                    if (certificateListEntity.getCourseCert()!=null) {
                        String passRate = certificateListEntity.getCourseCert().getPassRate();
                        if (!Utils.isEmpty(passRate) && Float.parseFloat(passRate)==1)
                            isHaveCertificate = true;
                    }
                    if (certificateListEntity.getRyList()!=null && certificateListEntity.getRyList().size()>0)
                        isHaveCertificate =true;
                    if (certificateListEntity.getTsList()!=null && certificateListEntity.getTsList().size()>0)
                        isHaveCertificate =true;
                    if (isHaveCertificate){
                        viewBinding.swipeRefreshLayout.setRefreshing(false);
                        viewBinding.recyclerView.setVisibility(View.VISIBLE);
                        viewBinding.noDataLayout.noDataLayout.setVisibility(View.GONE);
                        List<CertificateEntity> mlist =new ArrayList<>();
                        if (certificateListEntity.getCourseCert()!=null){//毕业证书
                            String passRate = certificateListEntity.getCourseCert().getPassRate();
                            if (!Utils.isEmpty(passRate) && Float.parseFloat(passRate)==1){
                                certificateListEntity.getCourseCert().setType("1");
                                mlist.add(certificateListEntity.getCourseCert());
                            }
                        }
                        if (certificateListEntity.getRyList()!=null && certificateListEntity.getRyList().size()>0){//荣誉证书
                            for (CertificateEntity entity : certificateListEntity.getRyList()){
                                entity.setType("2");
                                mlist.add(entity);
                            }
                        }
                        if (certificateListEntity.getTsList()!=null && certificateListEntity.getTsList().size()>0){
                            for (String str : certificateListEntity.getTsList()){
                                CertificateEntity entity =new CertificateEntity();
                                entity.setType("3");
                                entity.setImgUrl(str);
                                mlist.add(entity);
                            }
                        }
                        certificateAdapter.setMlist(mlist);
                        certificateAdapter.notifyDataSetChanged();
                    }else {
                        showNullListView(false);
                    }
                }
            }
        });
    }

    private void showNullListView(boolean isError){
        viewBinding.swipeRefreshLayout.setRefreshing(false);
        viewBinding.recyclerView.setVisibility(View.GONE);
        viewBinding.noDataLayout.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.noDataLayout.tvNodata.setText(getString(R.string.network_error));
            viewBinding.noDataLayout.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.noDataLayout.tvNodata.setText(getString(R.string.place_certification));
            viewBinding.noDataLayout.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}