package com.amolina.weather.clima.data.model.api

import com.amolina.weather.clima.data.model.db.*

/**
 * Created by Amolina on 10/02/2019.
 */

data class ForecastResponse(

    var cod: Int,
    var message: Double,
    var cnt: Int,
    var list: List<ForecastListResponse>?,
    var city: City?
) {
    constructor() : this(0, 0.0, 0, null, null)

    //(cod: Int, message: String?, cnt: Int?, cityId: Long?)
    fun toForecast(): Forecast? {
        return this.city?.id?.let {
            Forecast(
                cod = this.cod,
                message = this.message.toString(),
                cnt = this.cnt,
                city_id = it
            )
        }
    }

    fun toForecastList(): List<ForecastList>? {
        return list?.flatMap { item -> listOf(this.city?.id?.let { ForecastList(item.dt, it, item.dt_txt) }!!) }
    }

    fun toMainList(): List<MainList>? {

        return (list?.flatMap { item ->
            listOf(this.city?.id?.let {
                MainList(
                    it, item.dt, item.main?.temp, item.main?.temp_min,
                    item.main?.temp_max, item.main?.pressure, item.main?.sea_level,
                    item.main?.grnd_level, item.main?.humidity, item.main?.temp_kf
                )
            })
        } as List<MainList>?)

    }

    fun toWeatherList(): List<WeatherList>? {
        return (list?.flatMap { item ->
            listOf(
                this.city?.id?.let {
                    WeatherList(
                        0,
                        it, item.dt, item.weather!![0].main, item.weather!![0].description, item.weather!![0].icon
                    )
                })
        } as List<WeatherList>?)
    }

}
