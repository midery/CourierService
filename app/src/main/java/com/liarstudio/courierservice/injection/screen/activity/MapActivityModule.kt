package com.liarstudio.courierservice.injection.screen.activity

import android.support.v7.app.AppCompatActivity
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.ui.screen.maps.MapActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MapActivityModule {

    @Binds
    @PerActivity
    abstract fun provideAppCompatActivity(activity: MapActivity): AppCompatActivity
}