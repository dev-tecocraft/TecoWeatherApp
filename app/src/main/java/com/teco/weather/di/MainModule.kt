package com.teco.weather.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.teco.weather.data.DataStoreRepository
import com.teco.weather.data.SharedPref
import com.teco.weather.rest.AppApiInterface
import com.teco.weather.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStoreRepository(context = context)

    @Provides
    @Singleton
    fun provideSharedPrefRepository(
        @ApplicationContext context: Context
    ) = SharedPref(context = context)

    @Provides
    internal fun provideGson(): Gson =
        GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        }.create()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okClient = OkHttpClient.Builder()
        okClient.connectTimeout(30000, TimeUnit.MILLISECONDS)
        okClient.writeTimeout(30000, TimeUnit.MILLISECONDS)
        okClient.readTimeout(30000, TimeUnit.MILLISECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okClient.interceptors().add(interceptor)

        okClient.interceptors().add(Interceptor { chain ->
            val response = chain.proceed(chain.request())
            response.newBuilder()
                .header("Cache-Control", "only-if-cached")
                .build()
            response
        })
        return okClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.API_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("GEO_CODING")
    fun provideRetrofitSearch(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.GEO_CODING_API_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): AppApiInterface =
        retrofit.create(AppApiInterface::class.java)

    @Provides
    @Singleton
    @Named("GEO_CODING")
    fun provideApiInterfaceSearch(@Named("GEO_CODING") retrofit: Retrofit): AppApiInterface =
        retrofit.create(AppApiInterface::class.java)
}