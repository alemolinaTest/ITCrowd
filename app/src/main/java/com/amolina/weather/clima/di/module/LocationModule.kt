package com.amolina.weather.clima.di.module

import com.amolina.weather.clima.ui.location.AndroidLocationProvider
import com.amolina.weather.clima.ui.location.LocationProvider
import dagger.Binds
import dagger.Module

@Module
abstract class LocationModule {
    @Binds
    abstract fun locationProvider(androidLocationProvider: AndroidLocationProvider): LocationProvider
}