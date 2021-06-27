package com.planet.premium.activity.sidesheet;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments=new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new BinanceWalletFragment());
        fragments.add(new BithumbWalletFragment());
        fragments.add(new UpbitWalletFragment());

    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}