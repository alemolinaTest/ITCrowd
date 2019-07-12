package com.amolina.weather.clima.ui.showDays

import androidx.recyclerview.widget.LinearLayoutManager

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class ShowFragmentDaysModule {

    @Provides
    @Singleton
    internal fun provideShowDaysAdapter(): ShowDaysAdapter {
        return ShowDaysAdapter()
    }

    @Provides
    internal fun provideLinearLayoutManager(fragment: ShowDaysFragment): androidx.recyclerview.widget.LinearLayoutManager {
        return androidx.recyclerview.widget.LinearLayoutManager(fragment.activity)
    }

}
