package com.amolina.weather.clima.ui.cities

import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.rx.SchedulerProvider
import dagger.Module
import dagger.Provides

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class CitiesModule {

    @Provides
    internal fun provideMainViewModel(dataManager: DataManager,
                                      schedulerProvider: SchedulerProvider): CitiesViewModel {
        return CitiesViewModel(dataManager, schedulerProvider)
    }

}
