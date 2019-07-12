package com.amolina.weather.clima.ui.citiesSelection

import androidx.lifecycle.MutableLiveData
import androidx.databinding.ObservableArrayList
import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.data.model.db.City
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.ui.base.BaseViewModel
import com.amolina.weather.clima.ui.cities.CitiesActions
import com.amolina.weather.clima.ui.cities.CitiesItemModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.PublishSubject


/**
 * Created by Amolina on 02/07/19.
 */

open class CitiesSelectionViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
        BaseViewModel<Any>(dataManager, schedulerProvider) {

    val TAG: String = this::class.java.simpleName
    val showCities: MutableLiveData<List<CitiesItemModel>> = MutableLiveData()
    val actionsSubject: PublishSubject<CitiesActions> = PublishSubject.create()
    val citiesItemViewModels = ObservableArrayList<CitiesItemModel>()
    val cityDeleted: MutableLiveData<Boolean> = MutableLiveData()
    val cityAdded: MutableLiveData<Boolean> = MutableLiveData()
    val allCities: MutableLiveData<List<CitiesItemModel>> = MutableLiveData()


    fun getSearchedCities(search: String) {
        setIsLoading(true)
        //Log.d(TAG, "checkNearestCity")
        compositeDisposable.add(
                dataManager.allCities
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe({ cities ->

                            val citiesList = cities.toList().filter { it.name.toUpperCase().contains(search.toUpperCase(), true) }

                            showCities.value = getViewModel(citiesList.sortedBy { it.name })
                           // showCities.value = allCities
                            setIsLoading(false)
                        }, { throwable ->
                            setIsLoading(false)
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

    fun getAllCities() {
        setIsLoading(true)
        //Log.d(TAG, "checkNearestCity")
        compositeDisposable.add(
                dataManager.allCities
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe({ cities ->
                            if (cities != null) {
                                val responseList = ArrayList<City>()
                                cities.forEach() {
                                    responseList.add(it)
                                }

                                showCities.value = getViewModel(responseList.sortedBy { it.name })
                                //allCities.value = showCities
                            }
                            setIsLoading(false)
                        }, { throwable ->
                            setIsLoading(false)
                            throwable.printStackTrace()
                        })

        )
    }

    private fun getCitiesObserver(): DisposableObserver<List<CitiesItemModel>> {
        return object : DisposableObserver<List<CitiesItemModel>>() {
            override fun onComplete() {
                setIsLoading(false)
                //Log.d(TAG, "saverObserver - onComplete")

            }

            override fun onError(e: Throwable) {
                setIsLoading(true)
            }

            override fun onNext(cities: List<CitiesItemModel>) {
                //Log.d(TAG, "saverObserver - onNext")
                showCities.value = cities

            }
        }
    }

    private fun combineNearAndSelectedCities(): Observable<List<CitiesItemModel>> {

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

    private fun combineSearchedCities(search: String): Observable<List<CitiesItemModel>> {

        return Observable.zip(
                dataManager.getCitySelectedBySearch(search.toUpperCase()),
                dataManager.getCityNearestBySearch(search.toUpperCase()),
                BiFunction() { selected: List<City>, near: List<City> ->
                    return@BiFunction setCitiesListsResponse(
                            selected,
                            near
                    )
                })
    }

    private fun setCitiesListsResponse(
            selected: List<City>, near: List<City>
    ): List<CitiesItemModel> {
        val responseList = ArrayList<City>()
        selected.forEach() {
            responseList.add(it)
        }
        near.forEach() {
            responseList.add(it)
        }
        return getViewModel(responseList)
    }

    private fun getViewModel(cities: List<City>): List<CitiesItemModel> {

        val forecastItemList = ArrayList<CitiesItemModel>()

        cities.forEach {
            forecastItemList.add(CitiesItemModel(it.name, it.id.toInt()))
        }

        return forecastItemList
    }

    fun deleteCity(cityId: Int) {
        setIsLoading(true)
        //Log.d(TAG, "fetchLocalWeather")
        compositeDisposable.add(
                combineDelete(cityId)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(deleteObserver(cityId))

        )
    }

    fun addCity(cityId: Int) {
        setIsLoading(true)
        //Log.d(TAG, "addCity")
        compositeDisposable.add(
                dataManager.getCityById(cityId.toLong())
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe({ response ->
                            if (response != null) {
                                response.setSelected()
                                setSelectedCity(response)
                            }
                            setIsLoading(false)
                        }, { throwable ->
                            setIsLoading(false)
                            throwable.printStackTrace()
                        })

        )
    }

    private fun setSelectedCity(city: City) {
        setIsLoading(true)
        //Log.d(TAG, "setSelectedCity: " + city.name)
        compositeDisposable.add(
                dataManager
                        .updateCity(city)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe({ response ->
                            cityAdded.value = response
                            setIsLoading(false)
                        }, { throwable ->
                            throwable.printStackTrace()
                            setIsLoading(false)
                        })
        )
    }

    private fun deleteObserver(cityId: Int): DisposableObserver<Boolean> {
        return object : DisposableObserver<Boolean>() {
            override fun onComplete() {
                setIsLoading(true)
                //Log.d(TAG, "saverObserver - onComplete")
                cityDeleted.value = true

            }

            override fun onError(e: Throwable) {
                setIsLoading(true)
            }

            override fun onNext(distance: Boolean) {
                //Log.d(TAG, "saverObserver - onNext")

            }
        }
    }

    private fun combineDelete(CityId: Int): Observable<Boolean> {
        return Observable.zip(
                dataManager.deleteCity(CityId),
                dataManager.deleteForecastByCity(CityId),
                dataManager.deleteWeatherByCity(CityId),
                Function3 { forecastRes: Boolean, forecastListRes: Boolean, weatherListRes: Boolean ->
                    return@Function3 forecastRes && forecastListRes && weatherListRes
                })
    }

    fun addCitiesItemsToList(showItems: List<CitiesItemModel>) {
        citiesItemViewModels.clear()
        citiesItemViewModels.addAll(showItems)
    }
}
