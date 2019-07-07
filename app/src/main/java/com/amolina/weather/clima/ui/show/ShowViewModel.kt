package com.amolina.weather.clima.ui.show

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.data.model.api.WeatherListResponse
import com.amolina.weather.clima.data.model.api.WeatherResponse
import com.amolina.weather.clima.data.model.db.*
import com.amolina.weather.clima.data.remote.ApiEndPoint
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.ui.base.BaseViewModel
import com.amolina.weather.clima.utils.*
import io.reactivex.Observable
import io.reactivex.functions.Function4
import java.util.*

/**
 * Created by Amolina on 02/07/19.
 */

class ShowViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<ShowNavigator>(dataManager, schedulerProvider) {

    val TAG: String = this::class.java.simpleName
    val weatherItemViewModels = ObservableArrayList<ShowItemModel>()
    val showRepos: MutableLiveData<List<ShowItemModel>> = MutableLiveData()

    private val weatherResponses = ArrayList<WeatherResponse>()

    fun fetchLocalWeather() {
        clearWeatherResponses()
        setIsLoading(true)
       //Log.d(TAG, "fetchLocalWeather")
        compositeDisposable.add(
            dataManager
                .allLocalWeather
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ weathers ->
                    if (!weathers.isNullOrEmpty()) {
                       //Log.d(TAG, "setViewModelList to LiveData")
                        weathers.forEach { weather ->
                            fetchWeatherListResponse(weather)
                        }
                        weatherResponses.clear()
                    }
                    setIsLoading(false)
                }, { throwable ->
                    setIsLoading(false)
                    throwable.printStackTrace()
                    navigator!!.handleError(throwable)
                })
        )
    }

    private fun fetchWeatherListResponse(wResponse: CurrentWeather) {
        setIsLoading(true)
       //Log.d(TAG, "fetchLocalWeather")
        compositeDisposable.add(
            combineWeather(wResponse)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ weatherResponse ->
                    if (weatherResponse != null) {
                       //Log.d(TAG, "setViewModelList to LiveData")
                        weatherResponses.add(weatherResponse)
                        showRepos.value = getViewModelListResponse(weatherResponses)

                    }
                    setIsLoading(false)
                }, { throwable ->
                    setIsLoading(false)
                    throwable.printStackTrace()
                    navigator!!.handleError(throwable)
                })

        )
    }

    private fun combineWeather(weather: CurrentWeather): Observable<WeatherResponse> {
        val cityId = weather.id
        return Observable.zip(
            dataManager.getCurrentWeatherById(cityId.toInt()),
            dataManager.getCurrentWeatherListById(cityId.toInt()),
            dataManager.getCurrentWeatherMainById(cityId.toInt()),
            dataManager.getCurrentWeatherSysById(cityId.toInt()),
            Function4 { weather: CurrentWeather, weatherList: List<CurrentWeatherList>, mainList: CurrentWeatherMain, sysList: CurrentWeatherSys ->
                return@Function4 setWeatherListsResponse(
                    weather,
                    weatherList,
                    mainList,
                    sysList
                )
            })
    }


    private fun setWeatherListsResponse(
        weather: CurrentWeather, weatherList: List<CurrentWeatherList>,
        mainList: CurrentWeatherMain, sysList: CurrentWeatherSys
    ): WeatherResponse {
        var response = WeatherResponse()
        var weatherListResponse = listOf(weatherList.first().toWeatherListResponse())

        response.id = weather.id.toInt()
        response.dt = weather.dt
        response.base = weather.base
        response.visibility = weather.visibility
        response.name = weather.name

        if (!weatherListResponse.isEmpty()) {
            response.weather = weatherListResponse
        }

        response.main = mainList.toMainListResponse()
        response.sys = sysList.toWeatherSysResponse()

        return response
    }

    private fun toResponseWeatherList(w: List<WeatherList>): List<WeatherListResponse> {
        return w.flatMap { item -> listOf(item.toWeatherListResponse()) }
    }

    private fun getViewModelListResponse(response: List<WeatherResponse>): List<ShowItemModel> {
        val weatherItemList = ArrayList<ShowItemModel>()
        response.indices.let { ind ->
            for (i in ind) {
                val weather = response[i].weather?.get(0)
                val main = response[i].main
                val sys = response[i].sys

                if (sys != null) {
                    if (main != null) {
                        weatherItemList.add(
                            ShowItemModel(
                                ApiEndPoint.ENDPOINT_FORECAST_ICON + weather?.icon + ApiEndPoint.ENDPOINT_FORECAST_ICON_TAIL,
                                response[i].name,
                                main.temp.toString(),
                                sys.message.toString(),
                                main.temp_max.toString(),
                                getDateFromUTCTimestamp(response[i].dt.toLong(), DATE_FORMAT_FACEBOOK),
                                response[i].visibility.toString(),
                                sys.country,
                                getDateFromUTCTimestamp(sys.sunrise.toLong(), HOUR_FORMAT),
                                getDateFromUTCTimestamp(sys.sunset.toLong(), HOUR_FORMAT),
                                response[i].id
                            )
                        )
                    }
                }
            }
        }
        return weatherItemList
    }

    fun clearWeatherResponses(){
        weatherResponses.clear()
    }


    fun addShowItemsToList(showItems: List<ShowItemModel>) {
        weatherItemViewModels.clear()
        weatherItemViewModels.addAll(showItems)
    }
}
