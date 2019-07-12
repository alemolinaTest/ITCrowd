package com.amolina.weather.clima.ui.show

import androidx.recyclerview.widget.LinearLayoutManager

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Amolina on 02/07/19.
 */
@Module
class ShowFragmentModule {

    @Provides
    @Singleton
    internal fun provideShowAdapter(): ShowAdapter {
        return ShowAdapter()
    }

    @Provides
    internal fun provideLinearLayoutManager(fragment: ShowFragment): androidx.recyclerview.widget.LinearLayoutManager {
        return androidx.recyclerview.widget.LinearLayoutManager(fragment.activity)
    }

}
