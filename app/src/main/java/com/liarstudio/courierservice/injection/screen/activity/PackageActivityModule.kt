package com.liarstudio.courierservice.injection.screen.activity

import android.support.v7.app.AppCompatActivity
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.ui.screen.pack.PackageActivity
import dagger.Binds
import dagger.Module

@Module
abstract class PackageActivityModule {

    @Binds
    @PerActivity
    abstract fun provideAppCompatActivity(activity: PackageActivity): AppCompatActivity

}