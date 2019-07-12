package com.amolina.weather.clima.ui.cities

import com.amolina.weather.clima.ui.citiesSelection.CitiesSelectFragment
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
