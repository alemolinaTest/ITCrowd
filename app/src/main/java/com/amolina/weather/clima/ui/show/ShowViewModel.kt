package com.amolina.weather.clima.ui.show

import androidx.lifecycle.MutableLiveData
import androidx.databinding.ObservableArrayList
import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.data.model.api.CoordResponse
import com.amolina.weather.clima.data.model.api.WeatherListResponse
import com.amolina.weather.clima.data.model.api.WeatherResponse
import com.amolina.weather.clima.data.model.db.*
import com.amolina.weather.clima.data.remote.ApiEndPoint
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.ui.base.BaseViewModel
import com.amolina.weather.clima.utils.*
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Observable.zip
import io.reactivex.functions.Function5
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
                            //Log.d(TAG, "setViewModelList to LiveData")
                            weatherResponses.add(weatherResponse)
                            showRepos.value = getViewModelListResponse(weatherResponses)

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
        return zip(
                dataManager.getCurrentWeatherById(cityId.toInt()),
                dataManager.getCurrentWeatherListById(cityId.toInt()),
                dataManager.getCurrentWeatherMainById(cityId.toInt()),
                dataManager.getCurrentWeatherSysById(cityId.toInt()),
                dataManager.getCityById(cityId),
                Function5 { weather: CurrentWeather, weatherList: List<CurrentWeatherList>, mainList: CurrentWeatherMain, sysList: CurrentWeatherSys, city: City ->
                    return@Function5 setWeatherListsResponse(
                            weather,
                            weatherList,
                            mainList,
                            sysList,
                            city
                    )
                })
    }


    private fun setWeatherListsResponse(
            weather: CurrentWeather, weatherList: List<CurrentWeatherList>,
            mainList: CurrentWeatherMain, sysList: CurrentWeatherSys, city: City
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
        response.coord = CoordResponse(lat = city.coord.lat, lon = city.coord.lon)

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
                val coord = response[i].coord!!

                if (sys != null) {
                    if (main != null) {
                        weatherItemList.add(
                                ShowItemModel(
                                        imageUrl = ApiEndPoint.ENDPOINT_FORECAST_ICON + weather?.icon + ApiEndPoint.ENDPOINT_FORECAST_ICON_TAIL,
                                        city = response[i].name,
                                        temp = main.temp.toString(),
                                        tempMin = sys.message.toString(),
                                        tempMax = main.temp_max.toString(),
                                        time = getDateFromUTCTimestamp(response[i].dt.toLong(), DATE_FORMAT_FACEBOOK),
                                        visibility = response[i].visibility.toString(),
                                        country = sys.country,
                                        sunrise = getDateFromUTCTimestamp(sys.sunrise.toLong(), HOUR_FORMAT),
                                        sunset = getDateFromUTCTimestamp(sys.sunset.toLong(), HOUR_FORMAT),
                                        cityId = response[i].id,
                                        pressure = main.pressure.toString(),
                                        humidity = main.humidity.toString(),
                                        coord = LatLng(coord.lat, coord.lon)
                                )
                        )
                    }
                }
            }
        }
        return weatherItemList
    }

    fun clearWeatherResponses() {
        weatherResponses.clear()
    }


    fun addShowItemsToList(showItems: List<ShowItemModel>) {
        weatherItemViewModels.clear()
        weatherItemViewModels.addAll(showItems)
    }
}
