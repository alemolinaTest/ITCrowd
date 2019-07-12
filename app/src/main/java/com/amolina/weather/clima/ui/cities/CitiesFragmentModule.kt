package com.amolina.weather.clima.ui.cities

import androidx.recyclerview.widget.LinearLayoutManager
import com.amolina.weather.clima.ui.citiesSelection.CitiesSelectFragment

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class CitiesFragmentModule {

    @Provides
    internal fun provideLinearLayoutManager(fragment: CitiesFragment): androidx.recyclerview.widget.LinearLayoutManager {
        return androidx.recyclerview.widget.LinearLayoutManager(fragment.activity)
    }

    @Provides
    @Singleton
    internal fun provideCitiesAdapter(): CitiesAdapter {
        return CitiesAdapter()
    }

}
