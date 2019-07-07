package com.amolina.weather.clima.data.model.api

/**
 * Created by Amolina on 10/02/2019.
 */
data class ForecastListResponse(

    var dt: Int,
    var main: MainListResponse?,
    var weather: List<WeatherListResponse>?,
    var clouds: CloudListResponse?,
    var wind: WindListResponse?,
    var dt_txt: String,
    var city_id: Long
) {
    constructor() : this(0, null, null, null, null, "", 0)

    fun setCityId(cityId: Long) {
        this.city_id = cityId
    }

}