package com.liarstudio.courierservice.injection.screen

import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.injection.screen.activity.AuthActivityModule
import com.liarstudio.courierservice.injection.screen.fragment.PackageListFragmentModule
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import com.liarstudio.courierservice.ui.screen.main.MainFragment
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import android.app.Activity
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.injection.screen.activity.MainActivityModule
import com.liarstudio.courierservice.injection.screen.fragment.FragmentBuilder
import dagger.android.AndroidInjector
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import dagger.android.ActivityKey
import dagger.multibindings.IntoMap
import dagger.Binds



@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [AuthActivityModule::class])
    abstract fun provideAuthActivityInjector(): AuthActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilder::class])
    abstract fun provideMainActivityInjector(): MainActivity
}
