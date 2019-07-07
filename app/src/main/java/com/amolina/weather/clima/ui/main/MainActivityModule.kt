package com.amolina.weather.clima.ui.main

import android.arch.lifecycle.ViewModelProvider

import com.amolina.weather.clima.ViewModelProviderFactory
import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.ui.cities.CitiesViewModel
import com.amolina.weather.clima.ui.location.LocationProvider
import com.amolina.weather.clima.ui.show.ShowViewModel
import com.amolina.weather.clima.ui.showDays.ShowDaysViewModel
import com.amolina.weather.clima.ui.splash.SplashViewModel

import dagger.Module
import dagger.Provides

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class MainActivityModule {

    @Provides
    internal fun provideMainViewModel(dataManager: DataManager,
                                      schedulerProvider: SchedulerProvider): MainViewModel {
        return MainViewModel(dataManager, schedulerProvider)
    }

    @Provides
    internal fun provideShowViewModelProviderFactory(showViewModel: ShowViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(showViewModel)
    }

    @Provides
    internal fun provideShowDaysViewModelProviderFactory(showDaysViewModel: ShowDaysViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(showDaysViewModel)
    }

    @Provides
    internal fun provideShowViewModel(dataManager: DataManager,
                                  schedulerProvider: SchedulerProvider): ShowViewModel {
        return ShowViewModel(dataManager, schedulerProvider)
    }

    @Provides
    internal fun provideShowDaysViewModel(dataManager: DataManager,
                                      schedulerProvider: SchedulerProvider): ShowDaysViewModel {
        return ShowDaysViewModel(dataManager, schedulerProvider)
    }

    @Provides
    internal fun provideCitiesViewModel(dataManager: DataManager,
                                      schedulerProvider: SchedulerProvider): CitiesViewModel {
        return CitiesViewModel(dataManager, schedulerProvider)
    }

    @Provides
    internal fun provideSplashViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider,  locationProvider: LocationProvider): SplashViewModel {
        return SplashViewModel(dataManager, schedulerProvider,locationProvider)
    }

}
