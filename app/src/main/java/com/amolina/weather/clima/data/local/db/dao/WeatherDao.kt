package com.amolina.weather.clima.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.amolina.weather.clima.data.model.db.CurrentWeather
import com.amolina.weather.clima.data.model.db.CurrentWeatherList
import com.amolina.weather.clima.data.model.db.CurrentWeatherMain
import com.amolina.weather.clima.data.model.db.CurrentWeatherSys

/**
 * Created by Amolina on 10/02/2019.
 */
//ROOM Data Access Object - database interactions
@Dao
interface WeatherDao {

    @Query("SELECT * FROM current_weather")
    fun loadAllCurrentWeather(): List<CurrentWeather>

    @Query("SELECT * FROM current_weather_list")
    fun loadAllCurrentWeatherList(): List<CurrentWeatherList>

    @Query("SELECT * FROM current_weather_main")
    fun loadAllCurrentWeatherMain(): List<CurrentWeatherMain>

    @Query("SELECT * FROM current_weather_sys")
    fun loadAllCurrentWeatherSys(): List<CurrentWeatherSys>


    @Query("SELECT * FROM current_weather_list")
    fun loadAllWeatherList(): List<CurrentWeatherList>

    @Query("SELECT * FROM current_weather  WHERE city_id = :id")
    fun loadCurrentWeatherByCity(id:Int): CurrentWeather

    @Query("SELECT * FROM current_weather_list  WHERE city_id = :id")
    fun loadCurrentWeatherListByCity(id:Int): List<CurrentWeatherList>

    @Query("SELECT * FROM current_weather_main  WHERE city_id = :id")
    fun loadCurrentWeatherMainByCity(id:Int): CurrentWeatherMain

    @Query("SELECT * FROM current_weather_sys  WHERE city_id = :id")
    fun loadCurrentWeatherSysByCity(id:Int): CurrentWeatherSys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAllWeather(weatherList: List<CurrentWeather>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: CurrentWeather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertWeatherList(weather: List<CurrentWeatherList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMainList(main: CurrentWeatherMain)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSysList(sys: CurrentWeatherSys)

    @Query("DELETE FROM current_weather")
    fun deleteAllWeather()

    @Query("DELETE FROM current_weather_list")
    fun deleteAllWeatherList()

    @Query("DELETE FROM current_weather_main")
    fun deleteAllMainList()

    @Query("DELETE FROM current_weather_sys")
    fun deleteAllSysList()

    @Query("DELETE FROM current_weather WHERE city_id = :cityId")
    fun deleteWeatherCascade(cityId: Int)

}
