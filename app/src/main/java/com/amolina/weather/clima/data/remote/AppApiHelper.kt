package com.amolina.weather.clima.data.remote

import com.amolina.weather.clima.data.model.api.*
import com.rx2androidnetworking.Rx2AndroidNetworking

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable

/**
 * Created by Amolina on 02/07/19.
 */
//make calls using androidnetworking library, getting Observable<Response>
//with retrofit is like
// @POST("api/user/auth/google_signin")
//    fun googleSignIn(@Body registerTokenDTO: RegisterTokenDTO): Single<AuthToken>
@Singleton
class AppApiHelper @Inject
constructor(override val apiHeader: ApiHeader) : ApiHelper {

    override fun getForecastApiCall(cityId: String): Observable<ForecastResponse> {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FORECAST)
            .addQueryParameter("id", cityId)
            .addQueryParameter(apiHeader.publicApiHeader)
            .build()
            .getObjectObservable(ForecastResponse::class.java)
    }

    override fun getCurrentWeatherApiCall(cityId: String): Observable<WeatherResponse> {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CURRENT_WEATHER)
            .addQueryParameter("id", cityId)
            .addQueryParameter(apiHeader.publicApiHeader)
            .build()
            .getObjectObservable(WeatherResponse::class.java)
    }

}
