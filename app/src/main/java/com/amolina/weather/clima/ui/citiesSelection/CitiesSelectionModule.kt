package com.amolina.weather.clima.ui.citiesSelection

import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.rx.SchedulerProvider
import dagger.Module
import dagger.Provides

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class CitiesSelectionModule {

    @Provides
    internal fun provideMainViewModel(dataManager: DataManager,
                                      schedulerProvider: SchedulerProvider): CitiesSelectionViewModel {
        return CitiesSelectionViewModel(dataManager, schedulerProvider)
    }

}
