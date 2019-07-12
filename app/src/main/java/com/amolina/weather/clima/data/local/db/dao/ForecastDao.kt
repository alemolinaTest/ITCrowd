package com.amolina.weather.clima.data.local.db.dao

import androidx.room.*
import com.amolina.weather.clima.data.model.db.*


/**
 * Created by Amolina on 10/02/2019.
 */
//ROOM Data Access Object - database interactions
@Dao
interface ForecastDao {

    @Query("SELECT * FROM forecast")
    fun loadAllForecast(): List<Forecast>

    @Query("SELECT * FROM forecast_list")
    fun loadAllForecastList(): List<ForecastList>

    @Query("SELECT * FROM forecast  WHERE city_id = :id")
    fun loadForecastByCity(id:Int): Forecast

    @Query("SELECT * FROM forecast_list  WHERE city_id = :id")
    fun loadForecastListByCity(id:Int): List<ForecastList>

    @Query("SELECT * FROM main_list  WHERE city_id = :id")
    fun loadMainListByCity(id:Int): List<MainList>

    @Query("SELECT * FROM weather_list  WHERE city_id = :id")
    fun loadWeatherListByCity(id:Int): List<WeatherList>

    @Query("SELECT * FROM wind_list  WHERE city_id = :id")
    fun loadWindListByCity(id:Int): List<WindList>

    @Query("SELECT * FROM cloud_list  WHERE city_id = :id")
    fun loadCloudListByCity(id:Int): List<CloudList>


    @Query("SELECT * FROM main_list  WHERE city_id = :id and list_dt= :dt")
    fun loadMainListByDtAndCity(id:Int, dt:Int): List<MainList>

    @Query("SELECT * FROM weather_list  WHERE city_id = :id and list_dt= :dt")
    fun loadWeatherListByDtAndCity(id:Int, dt:Int): List<WeatherList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAllForecast(forecastList: List<Forecast>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecast: Forecast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertForecastList(forecast: List<ForecastList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMainList(main: List<MainList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCloudList(cloud: CloudList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWindList(wind: WindList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertWeatherList(weatherList: List<WeatherList>)

    @Query("DELETE FROM forecast")
    fun deleteAllForecast()

    @Query("DELETE FROM forecast_list")
    fun deleteAllForecastList()

    @Query("DELETE FROM main_list")
    fun deleteAllMainList()

    @Query("DELETE FROM weather_list")
    fun deleteAllWeatherList()

    @Query("DELETE FROM wind_list")
    fun deleteAllWindList()

    @Query("DELETE FROM cloud_list")
    fun deleteAllCloudList()

    @Query("DELETE FROM forecast WHERE city_id = :cityId")
    fun deleteForecastCascade(cityId: Int)

}
