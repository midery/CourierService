package com.liarstudio.courierservice.injection.screen.fragment

import android.app.Fragment
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.ui.screen.main.MainFragment
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment
import dagger.Binds
import dagger.Module

@Module
abstract class MainFragmentModule {
    @Binds
    @PerScreen
    abstract fun provideFragment(fragment: MainFragment): Fragment

}