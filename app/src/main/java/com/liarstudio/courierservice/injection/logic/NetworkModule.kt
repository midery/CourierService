package com.liarstudio.courierservice.injection.logic

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.liarstudio.courierservice.injection.scope.PerApplication
import com.liarstudio.courierservice.logic.UrlServer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Модуль для работы с сетью
 */
@Module
class NetworkModule {
    private val NETWORK_TIMEOUT = 30 //таймаут 30 секунд

    /**
     * Метод, предоставляющий экземпляр класса Retrofit в любую точку приложения
     * Предназначен для выполнения REST-запросов
     */
    @Provides
    @PerApplication
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(UrlServer.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    /**
     * Метод, предоставляющий OkHttpClient в любую точку приложения
     * Предназначен для упрощения работы с сетью
     */
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

    /**
     * Метод, предоставляющий экземпляр класса Gson по всему приложению
     * Предназначен для сериализации
     */
    @Provides
    @PerApplication
    internal fun provideGson(): Gson {
        return GsonBuilder()
                .create()
    }

    /**
     * Метод, предоставляющий экземпляр класса HttpLoggingInterceptor всему приложению
     * Предназначен для логгирования
     */
    @Provides
    @PerApplication
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor { message -> println("OkHttp $message") }
                    .setLevel(HttpLoggingInterceptor.Level.BODY)

}