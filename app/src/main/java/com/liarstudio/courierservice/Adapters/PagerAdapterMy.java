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


public class PagerAdapterMy extends FragmentStatePagerAdapter {

    private String[] tabs;

    //Инициализируем табы
    public PagerAdapterMy(FragmentManager fm) {
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

        PackageFragment packageFragment = new PackageFragment();

        //Фильтруем посылки по заданному критерию и возвращаем фрагмент с отфильтрованным списком

        PackageList packages;

        switch (position) {
            case 0:
                packages = new PackageList(Select.from(Package.class)
                                .where(Condition.prop("status").notEq(4)).list());
                break;
            case 1:
                packages = new PackageList(
                        Select.from(Package.class)
                                .where(Condition.prop("status").eq(4))
                                .list());
                break;
            case 2:
                packages = new PackageList(Package.listAll(Package.class));
                break;
            default:
                return null;
        }


        //Сортируем посылки по дате
        Collections.sort(packages, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));

        packageFragment.setPackages(packages);
        return packageFragment;

    }

    //Для корректного отображения при обновлении
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

}
