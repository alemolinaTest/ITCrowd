package com.amolina.weather.clima.data.model.api

import com.amolina.weather.clima.data.model.db.*

data class WeatherResponse(

    var coord: CoordResponse?,
    var weather: List<WeatherListResponse>?,
    var base: String,
    var main: MainListResponse?,
    var visibility: Int,
    var wind: WindListResponse?,
    var clouds: CloudListResponse?,
    var dt: Int,
    var sys: SysResponse?,
    var id: Int,
    var name: String,
    var cod: Int
) {
    constructor() : this(null, null, "", null, 0, null, null, 0, null, 0, "", 0)

    //(cod: Int, message: String?, cnt: Int?, cityId: Long?)
    fun toCurrentWeather(): CurrentWeather? {
        return CurrentWeather(
            name = this.name,
            dt = this.dt,
            id = this.id.toLong(),
            base = this.base,
            visibility = this.visibility
        )

    }

    fun toCurrentWeatherList(): List<CurrentWeatherList>? {
        return weather?.flatMap { item ->
            listOf(this.id.let {
                CurrentWeatherList(it.toLong(), this.dt, item.id, item.main, item.description, item.icon)
            })
        }
    }

    fun toMain(): CurrentWeatherMain? {

        return CurrentWeatherMain(
            this.id.toLong(), this.dt, main?.temp, main?.temp_min, main?.temp_max, main?.pressure, main?.humidity
        )
    }


    fun toWeatherSys(): CurrentWeatherSys? {
        return sys?.let { sy ->
            CurrentWeatherSys(
                this.id.toLong(), this.dt, sy.type, sy.id, sy.message, sy.country, sy.sunrise, sy.sunset
            )
        }
    }

}