package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.response.HomeResponse;
import com.alhasawi.acekuwait.ui.main.view.home.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeTabsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    List<String> tabs = new ArrayList<>();
    HashMap<String, HomeResponse.Data> homeDataHashmap;


    public HomeTabsPagerAdapter(Context context, FragmentManager fm, int NumOfTabs, List<String> tabsList, HashMap<String, HomeResponse.Data> homeDataHashmap) {
        super(fm);
        this.context = context;
        this.mNumOfTabs = NumOfTabs;
        this.tabs = tabsList;
        this.homeDataHashmap = homeDataHashmap;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getResources().getString(R.string.ace);
        } else if (position == 1) {
            return context.getResources().getString(R.string.beyond);
        }
        return tabs.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return HomeFragment.newInstance(position, tabs.get(position), homeDataHashmap.get(tabs.get(position)));

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}