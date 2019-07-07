package com.amolina.weather.clima.data.local.db

import com.amolina.weather.clima.data.model.db.*
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Amolina on 02/07/19.
 */

@Singleton
class AppDbHelper @Inject
constructor(private val mAppDatabase: AppDatabase) : DbHelper {

    override fun getNearTestCities(search: String): Observable<List<City>> {
        return Observable.fromCallable { mAppDatabase.cityDao().loadAllNearTest(search) }
    }


    /*Observable.fromCallable: Returns an Observable that, when an observer subscribes to it,
        invokes a function you specify and then emits the value returned from that function.*///Callable: A task that returns a result and may throw an exception.
    // Implementors define a single method with no arguments called call.
    //getting collection of questions


    override val isCityListEmpty: Observable<Boolean>
        get() = Observable.fromCallable { mAppDatabase.cityDao().loadAll().isEmpty() }

    override val allCities: Observable<List<City>>
        get() = Observable.fromCallable { mAppDatabase.cityDao().loadAll() }

    override val allSelectedCities: Observable<List<City>>
        get() = Observable.fromCallable { mAppDatabase.cityDao().loadAllSelected() }

    override val allNearestCities: Observable<List<City>>
        get() = Observable.fromCallable { mAppDatabase.cityDao().loadAllNearest() }

    override fun saveCity(city: City): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.cityDao().insert(city)
            true
        }
    }

    override fun updateCity(city: City): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.cityDao().updateCity(city)
            true
        }
    }

    override fun deleteCity(cityId: Int): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.cityDao().deleteCity(cityId)
            true
        }
    }

    override fun deleteForecastByCity(cityId: Int): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.forecastDao().deleteForecastCascade(cityId)
            true
        }
    }

    override fun deleteWeatherByCity(cityId: Int): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.weatherDao().deleteWeatherCascade(cityId)
            true
        }
    }


    override fun saveCityList(list: List<City>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.cityDao().insertAll(list)
            true
        }

    }

    override fun getCityById(id: Long): Observable<City> {
        return Observable.fromCallable { mAppDatabase.cityDao().loadById(id) }
    }

    override fun saveForecast(forecast: Forecast): Observable<Boolean> {
        return Observable.fromCallable {

            mAppDatabase.forecastDao().insertForecast(forecast)
            true
        }
    }

    override val allLocalForecast: Observable<List<Forecast>>
        get() = Observable.fromCallable { mAppDatabase.forecastDao().loadAllForecast() }

    override val allLocalForecastList: Observable<List<ForecastList>>
        get() = Observable.fromCallable { mAppDatabase.forecastDao().loadAllForecastList() }

    override val allLocalWeather: Observable<List<CurrentWeather>>
        get() = Observable.fromCallable { mAppDatabase.weatherDao().loadAllCurrentWeather() }

    override val allLocalWeatherList: Observable<List<CurrentWeatherList>>
        get() = Observable.fromCallable { mAppDatabase.weatherDao().loadAllCurrentWeatherList() }

    override val allLocalWeatherMain: Observable<List<CurrentWeatherMain>>
        get() = Observable.fromCallable { mAppDatabase.weatherDao().loadAllCurrentWeatherMain() }

    override val allLocalWeatherSys: Observable<List<CurrentWeatherSys>>
        get() = Observable.fromCallable { mAppDatabase.weatherDao().loadAllCurrentWeatherSys() }

    override fun getForecastById(id: Int): Observable<Forecast> {
        return Observable.fromCallable { mAppDatabase.forecastDao().loadForecastByCity(id) }
    }

    override fun getForecastListById(id: Int): Observable<List<ForecastList>> {
        return Observable.fromCallable { mAppDatabase.forecastDao().loadForecastListByCity(id).sortedBy { it.dt }.map { it} }
    }

    override fun getMainListById(id: Int): Observable<List<MainList>> {
        return Observable.fromCallable { mAppDatabase.forecastDao().loadMainListByCity(id).sortedBy { it.list_dt }.map { it} }
    }

    override fun getWeatherListById(id: Int): Observable<List<WeatherList>> {
        return Observable.fromCallable { mAppDatabase.forecastDao().loadWeatherListByCity(id).sortedBy { it.list_dt }.map { it} }
    }

    override fun getWindListById(id: Int): Observable<List<WindList>> {
        return Observable.fromCallable { mAppDatabase.forecastDao().loadWindListByCity(id) }
    }

    override fun getCloudListById(id: Int): Observable<List<CloudList>> {
        return Observable.fromCallable { mAppDatabase.forecastDao().loadCloudListByCity(id) }
    }

    override fun getMainListByDtAndId(id: Int, dt: Int): Observable<List<MainList>> {
        return Observable.fromCallable { mAppDatabase.forecastDao().loadMainListByDtAndCity(id = id, dt = dt) }
    }

    override fun getWeatherListByDtAndId(id: Int, dt: Int): Observable<List<WeatherList>> {
        return Observable.fromCallable { mAppDatabase.forecastDao().loadWeatherListByDtAndCity(dt = dt, id = id) }
    }

    override fun saveForecastList(forecast: List<ForecastList>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.forecastDao().insertForecastList(forecast)
            true
        }
    }

    override fun saveMainList(main: List<MainList>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.forecastDao().insertMainList(main)
            true
        }
    }

    override fun saveCloudList(cloud: CloudList): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.forecastDao().insertCloudList(cloud)
            true
        }
    }

    override fun saveWindList(wind: WindList): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.forecastDao().insertWindList(wind)
            true
        }
    }

    override fun saveWeatherList(list: List<WeatherList>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.forecastDao().insertWeatherList(list)
            true
        }
    }

    override fun saveCurrentWeather(weather: CurrentWeather): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.weatherDao().insertWeather(weather)
            true
        }
    }

    override fun saveCurrentWeatherList(weatherList: List<CurrentWeatherList>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.weatherDao().insertWeatherList(weatherList)
            true
        }
    }

    override fun saveCurrentWeatherMain(weather: CurrentWeatherMain): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.weatherDao().insertMainList(weather)
            true
        }
    }

    override fun saveCurrentWeatherSys(weather: CurrentWeatherSys): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.weatherDao().insertSysList(weather)
            true
        }
    }

    override fun getCurrentWeatherById(id: Int): Observable<CurrentWeather> {
        return Observable.fromCallable { mAppDatabase.weatherDao().loadCurrentWeatherByCity( id = id) }
    }

    override fun getCurrentWeatherListById(id: Int): Observable<List<CurrentWeatherList>> {
        return Observable.fromCallable { mAppDatabase.weatherDao().loadCurrentWeatherListByCity( id = id) }
    }

    override fun getCurrentWeatherMainById(id: Int): Observable<CurrentWeatherMain> {
        return Observable.fromCallable { mAppDatabase.weatherDao().loadCurrentWeatherMainByCity( id = id) }
    }

    override fun getCurrentWeatherSysById(id: Int): Observable<CurrentWeatherSys> {
        return Observable.fromCallable { mAppDatabase.weatherDao().loadCurrentWeatherSysByCity( id = id) }
    }

    override fun getCitySelectedBySearch(search: String): Observable<List<City>> {
        return Observable.fromCallable { mAppDatabase.cityDao().loadSelectedSearch( search = search) }
    }

    override fun getCityNearestBySearch(search: String): Observable<List<City>> {
        return Observable.fromCallable { mAppDatabase.cityDao().loadNearestSearch( search = search) }
    }


    override fun resetCitiesState(): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.cityDao().resetDistance()
            true
        }
    }

    override fun resetForecastState(): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.forecastDao().deleteAllForecast()
            mAppDatabase.forecastDao().deleteAllForecastList()
            mAppDatabase.forecastDao().deleteAllMainList()
            mAppDatabase.forecastDao().deleteAllWeatherList()
            mAppDatabase.forecastDao().deleteAllWindList()
            mAppDatabase.forecastDao().deleteAllCloudList()
            true
        }
    }

    override fun resetWeatherState(): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.weatherDao().deleteAllWeather()
            mAppDatabase.weatherDao().deleteAllMainList()
            mAppDatabase.weatherDao().deleteAllWeatherList()
            mAppDatabase.weatherDao().deleteAllSysList()
            true
        }
    }
}
