package com.gfbusinessschool.activity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ActivityVoteselectionBinding;
import com.gfbusinessschool.fragment.VoteSelectionFragement;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *投票评选
 */
@Route(path = ARouterPath.VoteSelectionActivity)
public class VoteSelectionActivity extends BaseActivity<ActivityVoteselectionBinding> implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList =new ArrayList<>();
    private int lastFragmentIndex=0;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.voteSelection));
        viewBinding.btnStarding.setOnClickListener(this);
        viewBinding.btnNoStarted.setOnClickListener(this);
        viewBinding.btnComplete.setOnClickListener(this);

        fragmentManager =getSupportFragmentManager();
        fragmentList.clear();
        fragmentList.add(new VoteSelectionFragement(VoteSelectionFragement.TYPE_SATRTING));
        fragmentList.add(new VoteSelectionFragement(VoteSelectionFragement.TYPE_NO_SATRT));
        fragmentList.add(new VoteSelectionFragement(VoteSelectionFragement.TYPE_COMPLETE));
        showFragment(0);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStarding:
                if (lastFragmentIndex==0) return;
                showFragment(0);
                break;
            case R.id.btnNoStarted:
                if (lastFragmentIndex==1) return;
                showFragment(1);
                break;
            case R.id.btnComplete:
                if (lastFragmentIndex==2) return;
                showFragment(2);
                break;
        }
    }

    private void showFragment(int postion) {
        setTabState(postion);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentList.get(postion).isAdded()){
            if (postion==lastFragmentIndex) return;
            if (fragmentList.get(lastFragmentIndex).isAdded())
                transaction.show(fragmentList.get(postion)).hide(fragmentList.get(lastFragmentIndex)).commit();
            else
                transaction.show(fragmentList.get(postion)).commit();
        }else {
            if (fragmentList.get(lastFragmentIndex).isAdded())
                transaction.add(R.id.frameLayoutFollow,fragmentList.get(postion)).hide(fragmentList.get(lastFragmentIndex)).commit();
            else
                transaction.add(R.id.frameLayoutFollow,fragmentList.get(postion)).commit();
        }
        lastFragmentIndex =postion;
    }

    private void setTabState(int postion) {
        viewBinding.btnStarding.setTextColor(getApplicationContext().getResources().getColor(R.color.color_14));
        viewBinding.btnNoStarted.setTextColor(getApplicationContext().getResources().getColor(R.color.color_14));
        viewBinding.btnComplete.setTextColor(getApplicationContext().getResources().getColor(R.color.color_14));
        Utils.setBackgroundSolid(viewBinding.btnStarding,getResources().getColor(R.color.white));
        Utils.setBackgroundSolid(viewBinding.btnNoStarted,getResources().getColor(R.color.white));
        Utils.setBackgroundSolid(viewBinding.btnComplete,getResources().getColor(R.color.white));
        if (postion==0){
            Utils.setBackgroundSolid(getApplicationContext(),viewBinding.btnStarding);
            viewBinding.btnStarding.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }else if (postion==1){
            Utils.setBackgroundSolid(getApplicationContext(),viewBinding.btnNoStarted);
            viewBinding.btnNoStarted.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }else {
            Utils.setBackgroundSolid(getApplicationContext(),viewBinding.btnComplete);
            viewBinding.btnComplete.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }
    }
}