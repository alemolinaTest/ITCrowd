package com.amolina.weather.clima.data.model.api

data class MainListResponse(

    var temp: Double? = 0.0,
    var temp_min: Double? = 0.0,
    var temp_max: Double? = 0.0,
    var pressure: Double? = 0.0,
    var sea_level: Double? = 0.0,
    var grnd_level: Double? = 0.0,
    var humidity: Double? = 0.0,
    var temp_kf: Double? = 0.0
) {
    constructor() : this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

}