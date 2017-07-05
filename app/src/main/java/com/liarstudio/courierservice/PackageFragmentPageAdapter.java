package com.liarstudio.courierservice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.ArrayList;
import java.util.Collections;

public class PackageFragmentPageAdapter extends FragmentPagerAdapter {

    String[] tabs;

    ArrayList<Package> packages;

    public PackageFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "Активные",
                "Завершенные",
                "Все"
        };


    }

    public PackageFragmentPageAdapter(FragmentManager fm, ArrayList<Package> packages) {
        this(fm);
        this.packages = packages;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {

        PackageFragment packageFragment = new PackageFragment();

        ArrayList<Package> filtered = (ArrayList<Package>) packages.clone();

        switch (position) {
            case 0:
                filtered.removeIf(s -> !(s.status==0));
                packageFragment.setPackages(filtered);
                return packageFragment;//PackageFragment.getInstance();
            case 1:
                filtered.removeIf(s -> !(s.status==1));
                packageFragment.setPackages(filtered);
                return packageFragment;
            case 2:
                //filtered.removeIf(s -> !(s.status==2));
                packageFragment.setPackages(packages);
                return packageFragment;
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
