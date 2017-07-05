package com.liarstudio.courierservice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

public class ShipmentFragmentPagerAdapter extends FragmentPagerAdapter {

    String[] tabs;

    public ShipmentFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "Активные",
                "Завершенные",
                "Все"
        };


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ShipmentFragment.getInstance();
            case 1:

                return ShipmentFragment.getInstance();
            case 2:
                return ShipmentFragment.getInstance();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
