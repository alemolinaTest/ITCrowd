package com.amolina.weather.clima.di.builder


import com.amolina.weather.clima.ui.cities.CitiesActivity
import com.amolina.weather.clima.ui.cities.CitiesFragmentProvider
import com.amolina.weather.clima.ui.cities.CitiesModule
import com.amolina.weather.clima.ui.main.MainActivity
import com.amolina.weather.clima.ui.main.MainActivityModule
import com.amolina.weather.clima.ui.show.ShowFragmentProvider
import com.amolina.weather.clima.ui.splash.SplashActivity
import com.amolina.weather.clima.ui.splash.SplashActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Amolina on 02/07/19.
 */

// We map all our activities here, with their modules, that provides the correspondent ViewModel
@Module
abstract class ActivityBuilder {
    /* UI subcomponents(SplashActivityComponent , LoginActivityComponent, MainActivityComponent)
    are just like bridges to get the modules in the graph, those modules provide the ViewModel class
    With this annotation, we can easily attach activities/fragments to dagger graph
    without using components for every activity*/

    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    internal abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class, ShowFragmentProvider::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [CitiesModule::class, CitiesFragmentProvider::class])
    internal abstract fun bindCitiesActivity(): CitiesActivity

}
