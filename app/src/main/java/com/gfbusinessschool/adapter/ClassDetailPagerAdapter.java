package com.gfbusinessschool.adapter;

import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassDetailPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments = new ArrayList<>();
    String[] titles;

    public ClassDetailPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titleList) {
        super(fm);
        this.fragments = fragmentList;
        this.titles = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}