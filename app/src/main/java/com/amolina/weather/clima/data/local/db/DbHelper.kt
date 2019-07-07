package com.amolina.weather.clima.data.local.db

import com.amolina.weather.clima.data.model.db.*
import io.reactivex.Observable

/**
 * Created by Amolina on 02/07/19.
 */

interface DbHelper {

    val allCities: Observable<List<City>>

    val allSelectedCities: Observable<List<City>>

    val allNearestCities: Observable<List<City>>

    val isCityListEmpty: Observable<Boolean>

    val allLocalForecast: Observable<List<Forecast>>

    val allLocalForecastList: Observable<List<ForecastList>>

    fun getNearTestCities(search: String): Observable<List<City>>

    fun saveCity(city: City): Observable<Boolean>

    fun saveCityList(list: List<City>): Observable<Boolean>

    fun saveForecast(forecast: Forecast): Observable<Boolean>

    fun saveForecastList(forecastList: List<ForecastList>): Observable<Boolean>

    fun saveMainList(mainList: List<MainList>): Observable<Boolean>

    fun saveCloudList(cloudList: CloudList): Observable<Boolean>

    fun saveWindList(windList: WindList): Observable<Boolean>

    fun saveWeatherList(list: List<WeatherList>): Observable<Boolean>

    fun getForecastListById(id: Int): Observable<List<ForecastList>>

    fun getMainListById(id: Int): Observable<List<MainList>>

    fun getWeatherListById(id: Int): Observable<List<WeatherList>>

    fun getWindListById(id: Int): Observable<List<WindList>>

    fun getCloudListById(id: Int): Observable<List<CloudList>>

    fun updateCity(city: City): Observable<Boolean>

    fun deleteCity(cityId: Int): Observable<Boolean>

    fun resetCitiesState(): Observable<Boolean>

    fun getWeatherListByDtAndId(id: Int, dt: Int): Observable<List<WeatherList>>

    fun getMainListByDtAndId(id: Int, dt: Int): Observable<List<MainList>>

    fun resetForecastState(): Observable<Boolean>

    fun resetWeatherState(): Observable<Boolean>

    fun getCityById(id: Long): Observable<City>

    fun getForecastById(id: Int): Observable<Forecast>

    fun getCurrentWeatherById(id: Int): Observable<CurrentWeather>

    fun getCurrentWeatherListById(id: Int): Observable<List<CurrentWeatherList>>

    fun getCurrentWeatherMainById(id: Int): Observable<CurrentWeatherMain>

    fun getCurrentWeatherSysById(id: Int): Observable<CurrentWeatherSys>

    fun saveCurrentWeather(weather: CurrentWeather): Observable<Boolean>

    fun saveCurrentWeatherList(weatherList: List<CurrentWeatherList>): Observable<Boolean>

    fun saveCurrentWeatherMain(weather: CurrentWeatherMain): Observable<Boolean>

    fun saveCurrentWeatherSys(weather: CurrentWeatherSys): Observable<Boolean>

    val allLocalWeather: Observable<List<CurrentWeather>>

    val allLocalWeatherList: Observable<List<CurrentWeatherList>>

    val allLocalWeatherMain: Observable<List<CurrentWeatherMain>>

    val allLocalWeatherSys: Observable<List<CurrentWeatherSys>>

    fun getCitySelectedBySearch(search: String): Observable<List<City>>

    fun getCityNearestBySearch(search: String): Observable<List<City>>

    fun deleteForecastByCity(cityId: Int): Observable<Boolean>

    fun deleteWeatherByCity(cityId: Int): Observable<Boolean>

}
