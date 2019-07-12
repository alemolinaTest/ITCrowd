package com.amolina.weather.clima.ui.citiesSelection

import androidx.recyclerview.widget.LinearLayoutManager
import com.amolina.weather.clima.ui.citiesSelection.CitiesSelectFragment

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class CitiesSelectionFragmentModule {

    @Provides
    internal fun providesFragmentLinearLayoutManager(fragment: CitiesSelectFragment): androidx.recyclerview.widget.LinearLayoutManager {
        return androidx.recyclerview.widget.LinearLayoutManager(fragment.activity)
    }

}
