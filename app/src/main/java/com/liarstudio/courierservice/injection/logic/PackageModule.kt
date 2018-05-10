package com.liarstudio.courierservice.injection.logic

import com.liarstudio.courierservice.logic.pack.PackageApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class PackageModule {
    @Provides
    fun providePackageApi(retrofit: Retrofit) = retrofit.create(PackageApi::class.java)
}