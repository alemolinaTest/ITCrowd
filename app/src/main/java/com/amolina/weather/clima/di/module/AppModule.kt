package com.amolina.weather.clima.di.module

import android.app.Application
import androidx.room.Room
import android.content.Context

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.amolina.weather.clima.BuildConfig
import com.amolina.weather.clima.R
import com.amolina.weather.clima.data.AppDataManager
import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.data.local.db.AppDatabase
import com.amolina.weather.clima.data.local.db.AppDbHelper
import com.amolina.weather.clima.data.local.db.DbHelper
import com.amolina.weather.clima.data.remote.ApiHeader
import com.amolina.weather.clima.data.remote.ApiHelper
import com.amolina.weather.clima.data.remote.ApiInterceptor
import com.amolina.weather.clima.data.remote.AppApiHelper
import com.amolina.weather.clima.di.ApiInfo
import com.amolina.weather.clima.di.DatabaseInfo
import com.amolina.weather.clima.rx.AppSchedulerProvider
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.utils.AppConstants

import java.util.concurrent.TimeUnit

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    internal fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    @DatabaseInfo
    internal fun provideDatabaseName(): String {
        return AppConstants.DB_NAME
    }

    @Provides
    @ApiInfo
    internal fun provideApiKey(): String {
        return BuildConfig.API_KEY
    }

    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    @Singleton
    internal fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName).fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @Singleton
    internal fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper {
        return appDbHelper
    }

    @Provides
    @Singleton
    internal fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper {
        return appApiHelper
    }

    @Provides
    @Singleton
    internal fun provideProtectedApiHeader(@ApiInfo apiKey: String): ApiHeader.ProtectedApiHeader {
        return ApiHeader.ProtectedApiHeader(
                apiKey, "")
    }

    @Provides
    @Singleton
    internal fun provideApiHeaders(): ApiInterceptor {
        return ApiInterceptor()
    }


    @Provides
    @Singleton
    internal fun provideOkHttpClient(apiHeaders: ApiInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(apiHeaders)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(StethoInterceptor()).build()
    }

    @Provides
    @Singleton
    internal fun provideCalligraphyDefaultConfig(): CalligraphyConfig {
        return CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
    }

}

