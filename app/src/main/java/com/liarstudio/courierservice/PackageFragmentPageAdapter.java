package com.liarstudio.courierservice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.ArrayList;

public class PackageFragmentPageAdapter extends FragmentStatePagerAdapter {

    String[] tabs;

    ArrayList<Package> packages;


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
    public PackageFragmentPageAdapter(FragmentManager fm, ArrayList<Package> packages) {
        this(fm);
        this.packages = packages;
        packages.sort ( (p1, p2) -> p1.getDate().compareTo(p2.getDate()));
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

        ArrayList<Package> filtered = (ArrayList<Package>) packages.clone();

        switch (position) {
            case 0:
                filtered.removeIf(s -> !(s.getStatus()==0));
                packageFragment.setPackages(filtered);
                return packageFragment;//PackageFragment.getInstance();
            case 1:
                filtered.removeIf(s -> !(s.getStatus()==1));
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

    //Процедура добавления посылки. Если позиция -1, то добавляем в конец.
    //Если нет - то на свою позицию
    public void add(int position, Package pkg) {
        if (position == -1) {
            for (Package pack : packages)
            {
                position++;
                if (pkg.getDate().compareTo(pack.getDate()) < 0){
                    break;
                }

            }
            if ((position == packages.size()-1) && (pkg.getDate().compareTo(packages.get(position).getDate()) > 0))
                packages.add(pkg);
            else
                packages.add(position, pkg);
            //packages.add(pkg);
        }
        else
            packages.set(position, pkg);
        //packages.sort ( (p1, p2) -> p1.getDate().compareTo(p2.getDate()));
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
}
