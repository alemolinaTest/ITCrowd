package com.amolina.weather.clima.data.remote

import com.amolina.weather.clima.data.model.api.*

import io.reactivex.Observable

/**
 * Created by Amolina on 02/07/19.
 */

interface ApiHelper {

    val apiHeader: ApiHeader

    fun getForecastApiCall(cityId: String): Observable<ForecastResponse>

    fun getCurrentWeatherApiCall(cityId: String): Observable<WeatherResponse>
}
