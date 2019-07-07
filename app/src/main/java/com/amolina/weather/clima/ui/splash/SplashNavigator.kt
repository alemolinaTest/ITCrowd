package com.amolina.weather.clima.ui.splash


/**
 * Created by Amolina on 02/07/19.
 */

interface SplashNavigator {

    fun handleError(throwable: Throwable)

    fun openMainActivity()

    fun loadCities()

}
