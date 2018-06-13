package com.liarstudio.courierservice.injection.screen.fragment

import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.injection.screen.activity.MainActivityModule
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import com.liarstudio.courierservice.ui.screen.main.MainFragment
import com.liarstudio.courierservice.ui.screen.main.packages.PackageListFragment
import com.liarstudio.courierservice.ui.screen.main.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Строитель зависимостей для Fragment
 * Сюда следует заносить модули всех Fragment и привязывать их к классам
 */
@Module
abstract class FragmentBuilder {

    /**
     * Метод, привязывающий [PackageListFragmentModule] к [PackageListFragment]
     */
    @PerScreen
    @ContributesAndroidInjector(modules = [PackageListFragmentModule::class])
    abstract fun packageListFragment(): PackageListFragment

    /**
     * Метод, привязывающий [SettingsFragmentModule] к [SettingsFragment]
     */
    @PerScreen
    @ContributesAndroidInjector(modules = [SettingsFragmentModule::class])
    abstract fun settingsFragment(): SettingsFragment

    /**
     * Метод, привязывающий [MainFragmentModule] к [MainFragment]
     */
    @PerScreen
    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    abstract fun mainFragment(): MainFragment

}