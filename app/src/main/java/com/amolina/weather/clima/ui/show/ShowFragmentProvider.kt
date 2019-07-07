package com.amolina.weather.clima.ui.show

import com.amolina.weather.clima.ui.cities.CitiesFragment
import com.amolina.weather.clima.ui.cities.CitiesFragmentModule
import com.amolina.weather.clima.ui.showDays.ShowDaysFragment
import com.amolina.weather.clima.ui.showDays.ShowFragmentDaysModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Amolina on 02/07/19.
 */
@Module
abstract class ShowFragmentProvider {

    @ContributesAndroidInjector(modules = [ShowFragmentModule::class])
    internal abstract fun provideShowFragmentFactory(): ShowFragment

    @ContributesAndroidInjector(modules = [ShowFragmentDaysModule::class])
    internal abstract fun provideShowDaysFragmentFactory(): ShowDaysFragment

    @ContributesAndroidInjector(modules = [CitiesFragmentModule::class])
    internal abstract fun provideCitiesFragmentFactory(): CitiesFragment

}
