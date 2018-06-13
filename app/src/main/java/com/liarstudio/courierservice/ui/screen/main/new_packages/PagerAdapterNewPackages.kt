package com.liarstudio.courierservice.ui.screen.main.new_packages

import android.app.FragmentManager
import android.support.v13.app.FragmentPagerAdapter
import com.liarstudio.courierservice.ui.screen.main.packages.PackTabType
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment

class PagerAdapterNewPackages(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) =
            PackageListFragment.newInstance(PackTabType.getByPos(0))

    override fun getCount() = 1
}
