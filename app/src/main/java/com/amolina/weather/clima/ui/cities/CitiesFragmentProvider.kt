package com.amolina.weather.clima.ui.cities

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Amolina on 02/07/19.
 */
@Module
abstract class CitiesFragmentProvider {

    @ContributesAndroidInjector(modules = [CitiesFragmentModule::class])
    internal abstract fun provideCitiesFragmentFactory(): CitiesFragment

}
