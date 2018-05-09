package com.liarstudio.courierservice.injection.screen

import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.injection.screen.activity.AuthActivityModule
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @PerScreen
    @ContributesAndroidInjector(modules = [AuthActivityModule::class])
    abstract fun provideAuthActivityInjector(): AuthActivity
}
