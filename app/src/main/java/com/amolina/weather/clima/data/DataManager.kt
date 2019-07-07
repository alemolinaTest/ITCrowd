package com.amolina.weather.clima.data


import com.amolina.weather.clima.data.local.db.DbHelper
import com.amolina.weather.clima.data.model.api.ForecastResponse
import com.amolina.weather.clima.data.remote.ApiHelper
import io.reactivex.Observable

/**
 * Created by Amolina on 02/07/19.
 */

interface DataManager : DbHelper, ApiHelper {

    fun updateApiHeader(accessToken: String)
    fun saveForecastResponse(forecast: ForecastResponse): Observable<Boolean>

}
