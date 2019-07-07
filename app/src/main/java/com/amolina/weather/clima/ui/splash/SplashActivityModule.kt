package com.amolina.weather.clima.ui.splash

import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.ui.location.LocationProvider

import dagger.Module
import dagger.Provides


/**
 * Created by Amolina on 02/07/19.
 */
@Module
class SplashActivityModule {

    @Provides
    internal fun provideSplashViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider,
        locationProvider: LocationProvider
    ): SplashViewModel {
        return SplashViewModel(dataManager, schedulerProvider, locationProvider)
    }

}
