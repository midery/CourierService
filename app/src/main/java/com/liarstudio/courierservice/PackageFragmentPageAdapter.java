package com.liarstudio.courierservice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.Database.PackageList;
import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


import java.util.Collections;
import java.util.Comparator;

public class PackageFragmentPageAdapter extends FragmentStatePagerAdapter {

    String[] tabs;



    //Инициализируем табы
    public PackageFragmentPageAdapter(FragmentManager fm) {
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
        packageFragment.setAdapter(this);

        //Фильтруем посылки по заданному критерию и возвращаем фрагмент с отфильтрованным списком

        PackageList packages;

        switch (position) {
            case 0:
                packages = filterPackages(0);
                break;
            case 1:
                packages = filterPackages(1);
                break;
            case 2:
                packages = loadPackages();
                break;
            default:
                return null;
        }

        Collections.sort(packages, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));

        packageFragment.setPackages(packages);
        return packageFragment;

    }

    //Процедура добавления посылки. Если позиция -1, то добавляем в конец.
    //Если нет - то на свою позицию
    public void add(Package pkg) {
        pkg.getSender().save();
        pkg.getRecipient().save();
        pkg.save();
        notifyDataSetChanged();

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

    private PackageList filterPackages(int status) {
        return new PackageList(
                Select.from(Package.class).where(Condition.prop("status").eq(status)).list());
    }
    private PackageList loadPackages() {
        return new PackageList(Package.listAll(Package.class));
    }




}
