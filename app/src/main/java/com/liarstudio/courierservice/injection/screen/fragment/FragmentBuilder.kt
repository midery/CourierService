package com.liarstudio.courierservice.injection.screen.fragment

import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.ui.screen.main.MainFragment
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {
    @PerScreen
    @ContributesAndroidInjector(modules = [PackageListFragmentModule::class])
    abstract fun packageListFragment(): PackageListFragment

    @PerScreen
    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    abstract fun mainFragment(): MainFragment

}