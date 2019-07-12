package com.amolina.weather.clima.ui.citiesSelection

import com.amolina.weather.clima.ui.citiesSelection.CitiesSelectFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Amolina on 02/07/19.
 */
@Module
abstract class CitiesSelectionFragmentProvider {

    @ContributesAndroidInjector(modules = [CitiesSelectionFragmentModule::class])
    internal abstract fun provideCitiesSelectFragmentFactory(): CitiesSelectFragment

}
