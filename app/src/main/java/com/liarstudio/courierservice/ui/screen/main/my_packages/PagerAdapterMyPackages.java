package com.liarstudio.courierservice.ui.screen.main.my_packages;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.liarstudio.courierservice.entitiy.pack.Package;
import com.liarstudio.courierservice.logic.pack.PackageRepository;
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment;
import com.orm.query.Condition;
import com.orm.query.Select;


import java.util.Collections;


public class PagerAdapterMyPackages extends FragmentStatePagerAdapter {

    private String[] tabs;

    //Инициализируем табы
    public PagerAdapterMyPackages(FragmentManager fm) {
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

        PackageListFragment packageFragment = new PackageListFragment();

        //Фильтруем посылки по заданному критерию и возвращаем фрагмент с отфильтрованным списком

        PackageRepository packages;

        switch (position) {
            case 0:
                packages = new PackageRepository(Select.from(Package.class)
                                .where(Condition.prop("status").notEq(4)).list());
                break;
            case 1:
                packages = new PackageRepository(
                        Select.from(Package.class)
                                .where(Condition.prop("status").eq(4))
                                .list());
                break;
            case 2:
                packages = new PackageRepository(Package.listAll(Package.class));
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
