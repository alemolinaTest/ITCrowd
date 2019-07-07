package com.amolina.weather.clima.ui.main

import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.data.model.api.ForecastResponse
import com.amolina.weather.clima.data.model.db.City
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.functions.Function4
import io.reactivex.subjects.PublishSubject


/**
 * Created by Amolina on 02/07/19.
 */

class MainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<Any>(dataManager, schedulerProvider){

    val TAG: String = this::class.java.simpleName

    val actionsSubject: PublishSubject<MainActions> = PublishSubject.create()

    init {
   //     fetchSelectedSelectedCities()
    }
    private fun fetchSelectedSelectedCities() {
        setIsLoading(true)
       //Log.d(TAG, "fetchSelectedSelectedCities")
        compositeDisposable.add(
            dataManager
                .allSelectedCities
                .take(5)
                .map {
                    it.sortedBy(City::distance)
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ cities ->
                    if (!cities.isEmpty()) {
                        checkForecastForCities(cities)
                    }
                    setIsLoading(false)
                }, { throwable ->
                    throwable.printStackTrace()
                    setIsLoading(false)
                })
        )
    }

    private fun checkForecastForCities(cities: List<City>) {
        setIsLoading(true)
       //Log.d(TAG, "checkForecastForCities")
        compositeDisposable.add(
            Observable.fromIterable(cities)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ city ->
                    fetchForecast(city)
                    setIsLoading(false)
                }, { throwable ->
                    throwable.printStackTrace()
                    setIsLoading(false)
                })
        )
    }

    private fun fetchForecast(city: City) {
        setIsLoading(true)
       //Log.d(TAG, "fetchForecast" + city.name)
        compositeDisposable.add(
            dataManager
                .getForecastApiCall(city.id.toString())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ genreResponse ->
                    saveAllForecast(genreResponse)
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
                setIsLoading(false)
               //Log.d(TAG, "saverObserver - onComplete")
                actionsSubject.onNext(MainActions.getLocalForecasts())
            }

            override fun onError(e: Throwable) {
                setIsLoading(true)
            }

            override fun onNext(distance: Boolean) {
               //Log.d(TAG, "saverObserver - onNext")
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
}
