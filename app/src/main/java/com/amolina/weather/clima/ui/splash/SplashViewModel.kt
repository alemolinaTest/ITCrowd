package com.amolina.weather.clima.ui.splash

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.data.model.api.ForecastResponse
import com.amolina.weather.clima.data.model.api.WeatherResponse
import com.amolina.weather.clima.data.model.db.City
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.ui.base.BaseViewModel
import com.amolina.weather.clima.ui.location.Location
import com.amolina.weather.clima.ui.location.LocationProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import io.reactivex.observers.DisposableObserver


/**
 * Created by Amolina on 02/07/19.
 */
//all viewmodels has DataManager and SchedulerProvider
class SplashViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    var locationProvider: LocationProvider
) : BaseViewModel<SplashNavigator>(dataManager, schedulerProvider), LifecycleObserver {

    val TAG: String = this::class.java.simpleName
    val showWeather: MutableLiveData<Boolean> = MutableLiveData()
    var gotoNextActivity: Boolean = true


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
       //Log.d(TAG, "onDestroy")
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
       //Log.d(TAG, "onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
       //Log.d(TAG, "onStop")
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

    fun presetSettings(gotoNext: Boolean=true) {
        gotoNextActivity = gotoNext
    }

    private fun isAnyCitySelected() {
        setIsLoading(true)
       //Log.d(TAG, "isAnyCtyelected")
        compositeDisposable.add(
            dataManager
                .allNearestCities
                .map {
                    it.sortedBy(City::distance)
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ cities ->
                    if (cities.isEmpty()) {
                        fetchLocalCities()
                    } else {
                        getNearAndSelectedAllCities()
                    }
                    setIsLoading(false)
                }, { throwable ->
                    throwable.printStackTrace()
                    setIsLoading(false)
                })
        )
    }

    //get cities from json or analyze wich one is the default one
    fun fetchLocalCities() {
        setIsLoading(true)
       //Log.d(TAG, "fetchLocalCities")
        compositeDisposable.add(
            dataManager
                .allCities
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ cities ->
                    if (cities.isEmpty()) {
                        gotoLoadCities()
                    } else {
                        checkNearestCities(cities)
                    }
                    //          setIsLoading(false)
                }, { throwable ->
                    throwable.printStackTrace()
                    setIsLoading(false)
                })
        )
    }

    fun saveCities(cities: List<City>) {
        setIsLoading(true)
       //Log.d(TAG, "saveCities")
        compositeDisposable.add(
            dataManager
                .saveCityList(cities)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    checkNearestCities(cities)
                    setIsLoading(false)

                },
                    { throwable ->
                        setIsLoading(false)
                        throwable.printStackTrace()
                        navigator?.handleError(throwable)
                    })
        )
    }

    private fun checkNearestCities(cities: List<City>) {
        setIsLoading(true)
       //Log.d(TAG, "checkNearestCities")
        compositeDisposable.add(
            Observable.fromIterable(cities)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ city ->
                    checkNearestCity(city)
                    setIsLoading(false)

                },
                    { throwable ->
                        setIsLoading(false)
                        throwable.printStackTrace()
                        navigator?.handleError(throwable)
                    })
        )
    }

    //select the nearest city according to location
    private fun checkNearestCity(city: City) {
        setIsLoading(true)
       //Log.d(TAG, "checkNearestCity")
        compositeDisposable.add(
            isCurrentCityNearToLocation(Observable.just(city))!!
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(nearestCityObserver(city))
        )
    }

    private fun nearestCityObserver(city: City): DisposableObserver<Float> {
        return object : DisposableObserver<Float>() {
            override fun onComplete() {
                setIsLoading(true)

            }

            override fun onError(e: Throwable) {
                setIsLoading(true)
            }

            override fun onNext(distance: Float) {
                if (distance < SplashActivity.MIN_DISTANCE_FOR_POSITION_IN_METERS) {

                    fetchForecast(city)
                    city.setDistance(distance)
                    //city.setSelected()
                    setSelectedCity(city)

                }
            }
        }
    }

    private fun isCurrentCityNearToLocation(city: Observable<City>): Observable<Float>? {
       //Log.d(TAG, "isCurrentCityNearToLocation")
        return Observable.combineLatest(
            locationProvider.getLocation().toObservable(),
            city,
            BiFunction<Location, City, Float> { location, cityLocation ->
                return@BiFunction locationProvider.getDistanceBetween(
                    location,
                    Location(cityLocation.coord.lat, cityLocation.coord.lon)
                )
            })
    }

    private fun setSelectedCity(city: City) {
        setIsLoading(true)
       //Log.d(TAG, "setSelectedCity: " + city.name)
        compositeDisposable.add(
            dataManager
                .updateCity(city)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    setIsLoading(false)
                }, { throwable ->
                    throwable.printStackTrace()
                    setIsLoading(false)
                })
        )
    }

    private fun fetchForecastForCities(cities: List<City>) {
        setIsLoading(true)
       //Log.d(TAG, "checkNearestCities")
        compositeDisposable.add(
            Observable.fromIterable(cities)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ city ->
                    if (city != null) {
                        fetchWeather(city)
                        fetchForecast(city)
                    }
                    setIsLoading(false)

                },
                    { throwable ->
                        setIsLoading(false)
                        throwable.printStackTrace()
                        navigator?.handleError(throwable)
                    })
        )
    }

    private fun fetchWeather(city: City) {
        setIsLoading(true)
       //Log.d(TAG, "fetchForecast" + city.name)
        compositeDisposable.add(
            dataManager
                .getCurrentWeatherApiCall(city.id.toString())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ response ->

                    saveAllWeather(response)
                }, { throwable ->
                    throwable.printStackTrace()
                })
        )
    }

    //select the nearest city according to location
    private fun saveAllWeather(response: WeatherResponse) {
        setIsLoading(true)
       //Log.d(TAG, "checkNearestCity")
        compositeDisposable.add(
            combineWeatherSaving(response)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(saverWeatherObserver(response.id.toInt()))
        )
    }

    private fun saverWeatherObserver(cityId: Int): DisposableObserver<Boolean> {
        return object : DisposableObserver<Boolean>() {
            override fun onComplete() {
                setIsLoading(true)
               //Log.d(TAG, "saverObserver - onComplete")


            }

            override fun onError(e: Throwable) {
                setIsLoading(true)
            }

            override fun onNext(distance: Boolean) {
               //Log.d(TAG, "saverObserver - onNext")

            }
        }
    }

    private fun combineWeatherSaving(weather: WeatherResponse): Observable<Boolean> {

        return Observable.zip(
            weather.toCurrentWeather()?.let {
                dataManager.saveCurrentWeather(it)
            },
            weather.toCurrentWeatherList()?.let {
                dataManager.saveCurrentWeatherList(it)
            },
            weather.toMain()?.let {
                dataManager.saveCurrentWeatherMain(it)
            },
            weather.toWeatherSys()?.let {
                dataManager.saveCurrentWeatherSys(it)
            },
            Function4 { forecastRes: Boolean, forecastListRes: Boolean, weatherListRes: Boolean, mainListRes: Boolean ->
                return@Function4 forecastRes && forecastListRes && weatherListRes && mainListRes
            })
    }

    private fun fetchForecast(city: City) {
        setIsLoading(true)
       //Log.d(TAG, "fetchForecast" + city.name)
        compositeDisposable.add(
            dataManager
                .getForecastApiCall(city.id.toString())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ forecastResponse ->
                    saveAllForecast(forecastResponse)
                }, { throwable ->
                    throwable.printStackTrace()
                })
        )
    }

    //select the nearest city according to location
    private fun saveAllForecast(response: ForecastResponse) {
        setIsLoading(true)
       //Log.d(TAG, "checkNearestCity")
        compositeDisposable.add(
            combineForecastSaving(response)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(saverObserver())
        )
    }

    private fun saverObserver(): DisposableObserver<Boolean> {
        return object : DisposableObserver<Boolean>() {
            override fun onComplete() {
                setIsLoading(true)
               //Log.d(TAG, "saverObserver - onComplete")
                showWeather.value=true
                gotoNextActivity()

            }

            override fun onError(e: Throwable) {
                setIsLoading(true)
            }

            override fun onNext(distance: Boolean) {
               //Log.d(TAG, "saverObserver - onNext")
                //fetchLocalForecastList()
            }
        }
    }

    private fun combineForecastSaving(forecast: ForecastResponse): Observable<Boolean> {

        return Observable.zip(
            forecast.toForecast()?.let {
                dataManager.saveForecast(it)
            },
            forecast.toForecastList()?.let {
                dataManager.saveForecastList(it)
            },
            forecast.toWeatherList()?.let {
                dataManager.saveWeatherList(it)
            },
            forecast.toMainList()?.let {
                dataManager.saveMainList(it)
            },
            Function4 { forecastRes: Boolean, forecastListRes: Boolean, weatherListRes: Boolean, mainListRes: Boolean ->
                return@Function4 forecastRes && forecastListRes && weatherListRes && mainListRes
            })
    }

    fun fetchLocalForecastList() {
        setIsLoading(true)
       //Log.d(TAG, "fetchLocalForecastList")
        compositeDisposable.add(
            dataManager
                .allLocalForecastList
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ forecasts ->
                    if (!forecasts.isNullOrEmpty()) {
                       //Log.d(TAG, "fetchLocalForecastList not empty")

                    }
                    setIsLoading(false)
                }, { throwable ->
                    setIsLoading(false)
                    throwable.printStackTrace()
                })
        )
    }

    private fun resetCitiesState() {
        setIsLoading(true)
       //Log.d(TAG, "resetCitiesState")
        compositeDisposable.add(
            dataManager
                .resetCitiesState()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({}, { throwable ->
                    throwable.printStackTrace()
                })
        )
    }

    private fun resetForecastState() {
        setIsLoading(true)
       //Log.d(TAG, "resetForecastState")
        compositeDisposable.add(
            dataManager
                .resetForecastState()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({}, { throwable ->
                    throwable.printStackTrace()
                })
        )
    }

    fun resetWeatherStateBeforeGet() {
        setIsLoading(true)
       //Log.d(TAG, "resetWeatherState")
        compositeDisposable.add(
            dataManager
                .resetWeatherState()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    isAnyCitySelected()
                }, { throwable ->
                    throwable.printStackTrace()
                })
        )
    }

    fun getNearAndSelectedAllCities() {
        setIsLoading(true)
       //Log.d(TAG, "checkNearestCity")
        compositeDisposable.add(
            combineNearAndSelectedCities()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(getCitiesObserver())
        )
    }


    private fun combineNearAndSelectedCities(): Observable<List<City>> {

        return Observable.zip(
            dataManager.allSelectedCities.take(1),
            dataManager.allNearestCities,
            BiFunction() { selected: List<City>, near: List<City> ->
                return@BiFunction setCitiesListsResponse(
                    selected,
                    near
                )
            })
    }

    private fun getCitiesObserver(): DisposableObserver<List<City>> {
        return object : DisposableObserver<List<City>>() {
            override fun onComplete() {
                setIsLoading(false)
               //Log.d(TAG, "saverObserver - onComplete")

            }

            override fun onError(e: Throwable) {
                setIsLoading(true)
            }

            override fun onNext(cities: List<City>) {
               //Log.d(TAG, "saverObserver - onNext")
                fetchForecastForCities(cities)

            }
        }
    }


    private fun setCitiesListsResponse(
        selected: List<City>, near: List<City>
    ): List<City> {
        val responseList = ArrayList<City>()
        selected.forEach() {
            responseList.add(it)
        }
        near.forEach() {
            responseList.add(it)
        }
        return responseList
    }

    private fun gotoNextActivity() {
       //Log.d(TAG, "gotoNextActivity")
        if (gotoNextActivity) {
            navigator?.openMainActivity()
        }
    }

    private fun gotoLoadCities() {
       //Log.d(TAG, "gotoLoadCities")
        navigator?.loadCities()
    }

}
