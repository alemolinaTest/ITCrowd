package com.amolina.weather.clima.data.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.amolina.weather.clima.data.local.db.dao.CityDao
import com.amolina.weather.clima.data.local.db.dao.ForecastDao
import com.amolina.weather.clima.data.local.db.dao.WeatherDao

import com.amolina.weather.clima.data.model.db.*

/**
 * Created by Amolina on 02/07/19.
 */
//the classes to be tables
@Database(
    entities = [City::class,
        CloudList::class,
        Forecast::class,
        ForecastList::class,
        Location::class,
        MainList::class,
        WeatherList::class,
        WindList::class,
        CurrentWeather::class,
        CurrentWeatherList::class,
        CurrentWeatherMain::class,
        CurrentWeatherSys::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    abstract fun forecastDao(): ForecastDao

    abstract fun weatherDao(): WeatherDao
}
