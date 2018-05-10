package com.liarstudio.courierservice.injection.screen.fragment

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment
import dagger.Binds
import dagger.Module

@Module
abstract class PackageListFragmentModule {

    @Binds
    @PerScreen
    abstract fun provideFragment(fragment: PackageListFragment): Fragment
}