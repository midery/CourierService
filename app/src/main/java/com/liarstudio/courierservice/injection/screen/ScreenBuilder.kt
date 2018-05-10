package com.liarstudio.courierservice.injection.screen

import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.injection.screen.activity.AuthActivityModule
import com.liarstudio.courierservice.injection.screen.activity.MainActivityModule
import com.liarstudio.courierservice.injection.screen.fragment.PackageListFragmentModule
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import com.liarstudio.courierservice.ui.screen.main.MainFragment
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBuilder {

    @PerScreen
    @ContributesAndroidInjector(modules = [AuthActivityModule::class])
    abstract fun provideAuthActivityInjector(): AuthActivity

    @PerScreen
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun provideMainActivityInjector(): MainActivity

    @PerScreen
    @ContributesAndroidInjector(modules = [PackageListFragmentModule::class])
    abstract fun providePackageListFragmentInjector(): PackageListFragment
}
