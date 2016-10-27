package com.pointhub.earnredeemtab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Lenovo1 on 13-09-2016.
 */
public class ViewPageAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        public void addFragments(Fragment fragments , String titles)
        {
            this.fragments.add(fragments);
            this.tabTitles.add(titles);

        }

    public ViewPageAdapter(FragmentManager fm)
    {
        super(fm);
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
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}





