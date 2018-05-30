package com.liarstudio.courierservice.ui.screen.main.my_packages


import android.app.FragmentManager
import android.support.v13.app.FragmentPagerAdapter

import com.liarstudio.courierservice.ui.screen.main.packages.PackTabType
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment


class PagerAdapterMyPackages(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val tabs = arrayOf("Активные", "Завершенные", "Все")

    override fun getItem(position: Int) =
        PackageListFragment.newInstance(PackTabType.getByPos(position))

    override fun getCount(): Int {
        return tabs.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabs[position]
    }
}
