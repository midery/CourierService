package com.liarstudio.courierservice.injection.logic

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.liarstudio.courierservice.logic.ServerUtils
import com.liarstudio.courierservice.injection.scope.PerApplication
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    private val NETWORK_TIMEOUT = 30 //sec

    @Provides
    @PerApplication
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ServerUtils.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @PerApplication
    internal fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.connectTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)

        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        return okHttpClientBuilder.build()
    }

    @Provides
    @PerApplication
    internal fun provideGson(): Gson {
        return GsonBuilder()
                .create()
    }

    @Provides
    @PerApplication
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor { message -> print("OkHttp $message") }
                    .setLevel(HttpLoggingInterceptor.Level.BODY)

}