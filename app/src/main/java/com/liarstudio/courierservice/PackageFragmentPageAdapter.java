package com.liarstudio.courierservice;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.Database.PackageDB;
import com.liarstudio.courierservice.Database.PackageList;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PackageFragmentPageAdapter extends FragmentStatePagerAdapter {

    String[] tabs;

    PackageList packages;


    //Инициализируем табы
    public PackageFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "Активные",
                "Завершенные",
                "Все"
        };


    }

    //Передаем список посылок и сортируем его по дате
    public PackageFragmentPageAdapter(FragmentManager fm, PackageList packages) {
        this(fm);
        this.packages = packages;


        //Из-за того, что packages.sort требует API24, используем старую конструкцию с Comparator.compare
        //packages.sort ( (p1, p2) -> p1.getDate().compareTo(p2.getDate()));
        Collections.sort(packages, new Comparator<PackageDB>() {
            @Override
            public int compare(PackageDB o1, PackageDB o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {

        PackageFragment packageFragment = new PackageFragment();
        packageFragment.setAdapter(this);

        //Фильтруем посылки по заданному критерию и возвращаем фрагмент с отфильтрованным списком

        //PackageList filtered = (PackageList) packages.clone();

        switch (position) {
            case 0:

                /*filtered.removeIf(s -> !(s.getStatus()==0));
                packageFragment.setPackages(filtered);*/
                packageFragment.setPackages(filterPackages(0));
                return packageFragment;//PackageFragment.getInstance();
            case 1:
                /*
                filtered.removeIf(s -> !(s.getStatus()==1));
                packageFragment.setPackages(filtered);*/
                packageFragment.setPackages(filterPackages(1));
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

    //Процедура добавления посылки. Если позиция -1, то добавляем в конец.
    //Если нет - то на свою позицию
    public void add(int position, PackageDB pkg) {
        if (position == -1)
            packages.addDB(pkg);
        else
            packages.updateDB(position, pkg);

        notifyDataSetChanged();

    }

    //Для корректного отображения при обновлении
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //Возвращает индекс заданной посылки из списка всех посылок
    public int getAbsolutePosition(Package pkg) {
        return packages.indexOf(pkg);
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    public PackageList filterPackages(int status) {
        PackageList filtered = new PackageList(packages.getDbHelper());

        for(PackageDB packageDB : packages)
        {
            if(packageDB.getStatus() == status)
                filtered.add(packageDB);
        }
        return filtered;

    }
}
