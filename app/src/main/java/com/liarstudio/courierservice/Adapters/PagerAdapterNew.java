package com.liarstudio.courierservice.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.Database.PackageList;
import com.liarstudio.courierservice.Fragments.PackageFragment;
import com.orm.query.Condition;
import com.orm.query.Select;


import java.util.Collections;

public class PagerAdapterNew extends FragmentStatePagerAdapter {


    //Инициализируем табы
    public PagerAdapterNew(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        PackageFragment packageFragment = new PackageFragment();
        PackageList packages;

        switch (position) {
            case 0:
                packages = new PackageList(
                        Select.from(Package.class).where(Condition.prop("status").eq(0)).list());
                break;
            default:
                return null;
        }

        Collections.sort(packages, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));

        packageFragment.setPackages(packages);
        return packageFragment;

    }

    @Override
    public int getCount() {
        return 1;
    }

    //Для корректного отображения при обновлении
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
