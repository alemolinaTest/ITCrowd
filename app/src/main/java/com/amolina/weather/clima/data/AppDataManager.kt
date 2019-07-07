package com.amolina.weather.clima.data

import android.content.Context

import com.amolina.weather.clima.data.local.db.DbHelper
import com.amolina.weather.clima.data.model.api.*
import com.amolina.weather.clima.data.model.db.*
import com.amolina.weather.clima.data.remote.ApiHeader
import com.amolina.weather.clima.data.remote.ApiHelper

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable

/**
 * Created by Amolina on 02/07/19.
 */

@Singleton
class AppDataManager @Inject
constructor(
    private val mContext: Context,
    private val mDbHelper: DbHelper,
    private val mApiHelper: ApiHelper
) : DataManager {

    override fun getNearTestCities(search: String): Observable<List<City>> {
        return mDbHelper.getNearTestCities(search)
    }

    override val apiHeader: ApiHeader
        get() = mApiHelper.apiHeader

    override fun getForecastApiCall(cityId: String): Observable<ForecastResponse> {
        return mApiHelper.getForecastApiCall(cityId)
    }

    override fun getCurrentWeatherApiCall(cityId: String): Observable<WeatherResponse> {
        return mApiHelper.getCurrentWeatherApiCall(cityId)
    }

    override fun updateApiHeader(accessToken: String) {
        mApiHelper.apiHeader.protectedApiHeader.accessToken = accessToken
    }

    override fun saveCity(city: City): Observable<Boolean> {
        return mDbHelper.saveCity(city)
    }

    override fun updateCity(city: City): Observable<Boolean> {
        return mDbHelper.updateCity(city)
    }

    override fun deleteCity(cityId: Int): Observable<Boolean> {
        return mDbHelper.deleteCity(cityId)
    }

    override fun deleteForecastByCity(cityId: Int): Observable<Boolean> {
        return mDbHelper.deleteForecastByCity(cityId)
    }

    override fun deleteWeatherByCity(cityId: Int): Observable<Boolean> {
        return mDbHelper.deleteWeatherByCity(cityId)
    }

    override val allCities: Observable<List<City>>
        get() = mDbHelper.allCities

    override val allNearestCities: Observable<List<City>>
        get() = mDbHelper.allNearestCities

    override val allSelectedCities: Observable<List<City>>
        get() = mDbHelper.allSelectedCities

    override val isCityListEmpty: Observable<Boolean>
        get() = mDbHelper.isCityListEmpty

    override val allLocalForecast: Observable<List<Forecast>>
        get() = mDbHelper.allLocalForecast

    override val allLocalForecastList: Observable<List<ForecastList>>
        get() = mDbHelper.allLocalForecastList

    override fun getForecastById(id: Int): Observable<Forecast> {
        return mDbHelper.getForecastById(id)
    }

    override fun getForecastListById(id: Int): Observable<List<ForecastList>> {
        return mDbHelper.getForecastListById(id)
    }

    override fun getMainListById(id: Int): Observable<List<MainList>> {
        return mDbHelper.getMainListById(id)
    }

    override fun getWeatherListById(id: Int): Observable<List<WeatherList>> {
        return mDbHelper.getWeatherListById(id)
    }

    override fun getWindListById(id: Int): Observable<List<WindList>> {
        return mDbHelper.getWindListById(id)
    }

    override fun getCloudListById(id: Int): Observable<List<CloudList>> {
        return mDbHelper.getCloudListById(id)
    }

    override fun saveCityList(list: List<City>): Observable<Boolean> {
        return mDbHelper.saveCityList(list)
    }

    override fun saveMainList(list: List<MainList>): Observable<Boolean> {
        return mDbHelper.saveMainList(list)
    }

    override fun saveForecastResponse(forecast: ForecastResponse): Observable<Boolean> {
        //val forecastSaved= saveForecast(forecast.toForecast())
        return forecast.toForecastList()?.let { saveForecastList(it) }!!

    }

    override fun saveForecast(forecast: Forecast): Observable<Boolean> {
        return mDbHelper.saveForecast(forecast)
    }

    override fun saveForecastList(list: List<ForecastList>): Observable<Boolean> {
        return mDbHelper.saveForecastList(list)
    }

    override fun saveCloudList(cloudList: CloudList): Observable<Boolean> {
        return mDbHelper.saveCloudList(cloudList)
    }

    override fun saveWindList(windList: WindList): Observable<Boolean> {
        return mDbHelper.saveWindList(windList)
    }

    override fun saveWeatherList(list: List<WeatherList>): Observable<Boolean> {
        return mDbHelper.saveWeatherList(list)
    }

    override fun resetCitiesState(): Observable<Boolean> {
        return mDbHelper.resetCitiesState()
    }

    override fun getWeatherListByDtAndId(id: Int, dt: Int): Observable<List<WeatherList>> {
        return mDbHelper.getWeatherListByDtAndId(id = id, dt = dt)
    }

    override fun getMainListByDtAndId(id: Int, dt: Int): Observable<List<MainList>> {
        return mDbHelper.getMainListByDtAndId(dt = dt, id = id)
    }

    override fun resetForecastState(): Observable<Boolean> {
        return mDbHelper.resetForecastState()
    }

    override fun resetWeatherState(): Observable<Boolean> {
        return mDbHelper.resetWeatherState()
    }

    override fun getCityById(id: Long): Observable<City> {
        return mDbHelper.getCityById(id)
    }

    override fun saveCurrentWeather(weather: CurrentWeather): Observable<Boolean> {
        return mDbHelper.saveCurrentWeather(weather)
    }

    override fun saveCurrentWeatherList(weatherList: List<CurrentWeatherList>): Observable<Boolean> {
        return mDbHelper.saveCurrentWeatherList(weatherList)
    }

    override fun saveCurrentWeatherMain(main: CurrentWeatherMain): Observable<Boolean> {
        return mDbHelper.saveCurrentWeatherMain(main)
    }

    override fun saveCurrentWeatherSys(sys: CurrentWeatherSys): Observable<Boolean> {
        return mDbHelper.saveCurrentWeatherSys(sys)
    }

    override fun getCurrentWeatherById(id: Int): Observable<CurrentWeather> {
        return mDbHelper.getCurrentWeatherById(id)
    }

    override fun getCurrentWeatherListById(id: Int): Observable<List<CurrentWeatherList>> {
        return mDbHelper.getCurrentWeatherListById(id)
    }

    override fun getCurrentWeatherMainById(id: Int): Observable<CurrentWeatherMain> {
        return mDbHelper.getCurrentWeatherMainById(id)
    }

    override fun getCurrentWeatherSysById(id: Int): Observable<CurrentWeatherSys> {
        return mDbHelper.getCurrentWeatherSysById(id)
    }

    override fun getCitySelectedBySearch(search: String): Observable<List<City>> {
        return mDbHelper.getCitySelectedBySearch(search)
    }

    override fun getCityNearestBySearch(search: String): Observable<List<City>> {
        return mDbHelper.getCityNearestBySearch(search)
    }

    override val allLocalWeather: Observable<List<CurrentWeather>>
        get() = mDbHelper.allLocalWeather

    override val allLocalWeatherList: Observable<List<CurrentWeatherList>>
        get() = mDbHelper.allLocalWeatherList

    override val allLocalWeatherMain: Observable<List<CurrentWeatherMain>>
        get() = mDbHelper.allLocalWeatherMain

    override val allLocalWeatherSys: Observable<List<CurrentWeatherSys>>
        get() = mDbHelper.allLocalWeatherSys

}
