package com.liarstudio.courierservice.injection.screen

import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.injection.screen.activity.AuthActivityModule
import com.liarstudio.courierservice.injection.screen.activity.MainActivityModule
import com.liarstudio.courierservice.injection.screen.activity.MapActivityModule
import com.liarstudio.courierservice.injection.screen.activity.PackageActivityModule
import com.liarstudio.courierservice.injection.screen.fragment.FragmentBuilder
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import com.liarstudio.courierservice.ui.screen.maps.MapActivity
import com.liarstudio.courierservice.ui.screen.pack.PackageActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Строитель зависимостей для Activity
 * Сюда следует заносить модули всех Activity и привязывать их к классам
 */
@Module
abstract class ActivityBuilder {

    /**
     * Метод, привязывающий [AuthActivityModule] к [AuthActivity]
     */
    @PerActivity
    @ContributesAndroidInjector(modules = [AuthActivityModule::class])
    abstract fun provideAuthActivityInjector(): AuthActivity

    /**
     * Метод, привязывающий [MainActivityModule] и [FragmentBuilder] к [MainActivity].
     * [FragmentBuilder] служит для построения зависимостей фрагментов внутри Activity
     */
    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilder::class])
    abstract fun provideMainActivityInjector(): MainActivity

    /**
     * Метод, привязывающий [MapActivityModule] к [MapActivity]
     */
    @PerActivity
    @ContributesAndroidInjector(modules = [MapActivityModule::class])
    abstract fun provideMapActivityInjector(): MapActivity

    /**
     * Метод, привязывающий [PackageActivityModule] к [PackageActivity]
     */
    @PerActivity
    @ContributesAndroidInjector(modules = [PackageActivityModule::class])
    abstract fun providePackageActivityInjector(): PackageActivity
}
