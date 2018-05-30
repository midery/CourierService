package com.liarstudio.courierservice.injection.screen.fragment

import android.app.Fragment
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.ui.screen.main.MainFragment
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment
import com.liarstudio.courierservice.ui.screen.main.settings.SettingsFragment
import dagger.Binds
import dagger.Module

@Module
abstract class SettingsFragmentModule {
    @Binds
    @PerScreen
    abstract fun provideFragment(fragment: SettingsFragment): Fragment

}