package com.amolina.weather.clima.ui.cities

import android.support.v7.widget.LinearLayoutManager

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class CitiesFragmentModule {

    @Provides
    internal fun provideLinearLayoutManager(fragment: CitiesFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.activity)
    }

    @Provides
    @Singleton
    internal fun provideCitiesAdapter(): CitiesAdapter {
        return CitiesAdapter()
    }

}
