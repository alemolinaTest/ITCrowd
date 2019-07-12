package com.amolina.weather.clima.ui.showDays

import androidx.lifecycle.MutableLiveData
import androidx.databinding.ObservableArrayList
import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.data.model.api.ForecastListResponse
import com.amolina.weather.clima.data.model.api.ForecastResponse
import com.amolina.weather.clima.data.model.api.MainListResponse
import com.amolina.weather.clima.data.model.api.WeatherListResponse
import com.amolina.weather.clima.data.model.db.*
import com.amolina.weather.clima.data.remote.ApiEndPoint
import com.amolina.weather.clima.rx.SchedulerProvider
import com.amolina.weather.clima.ui.base.BaseViewModel
import com.amolina.weather.clima.ui.show.ShowNavigator
import com.amolina.weather.clima.utils.DATE_FORMAT_DAY_MONTH
import com.amolina.weather.clima.utils.HOUR_FORMAT_WITHOUT_SECONDS
import com.amolina.weather.clima.utils.getDateFromUTCTimestamp
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.observers.DisposableObserver
import java.util.*

/**
 * Created by Amolina on 02/07/19.
 */

class ShowDaysViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<ShowNavigator>(dataManager, schedulerProvider) {

    val TIME_SEL: String = "12:00"
    val TAG: String = this::class.java.simpleName
    val forecastItemViewModels = ObservableArrayList<ShowDaysItemModel>()
    val showRepos: MutableLiveData<List<ShowDaysItemModel>> = MutableLiveData()
    val city: MutableLiveData<String> = MutableLiveData()

    private val forecastResponsesList = ArrayList<ForecastListResponse>()
    private var forecastResponse = ForecastResponse()


    fun fetchCity(cityId: Int) {
        setIsLoading(true)
       //Log.d(TAG, "fetchLocalForecast")
        compositeDisposable.add(
            dataManager.getCityById(id = cityId.toLong())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ response ->
                    if (response != null) {
                       //Log.d(TAG, "setViewModelList to LiveData")
                        city.value = response.name
                    }

                }, { throwable ->
                    setIsLoading(false)
                    throwable.printStackTrace()
                    navigator!!.handleError(throwable)
                })

        )
    }

    fun fetchLocalCityForecast(cityId: Int) {
        setIsLoading(true)
       //Log.d(TAG, "fetchLocalForecast")
        compositeDisposable.add(
            combineForecast(cityId = cityId.toLong())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ response ->
                    if (response != null) {
                       //Log.d(TAG, "setViewModelList to LiveData")
                        forecastResponse = response
                        response.list?.let { fetchForecastListResponse(it) }
                    }

                }, { throwable ->
                    setIsLoading(false)
                    throwable.printStackTrace()
                    navigator!!.handleError(throwable)
                })

        )
    }

    private fun combineForecast(cityId: Long): Observable<ForecastResponse> {

        return Observable.zip(
            dataManager.getCityById(cityId),
            dataManager.getForecastById(cityId.toInt()),
            dataManager.getForecastListById(cityId.toInt()),
            Function3 { city: City, forecast: Forecast, flList: List<ForecastList> ->
                return@Function3 setForecastResponse(
                    forecast,
                    flList,
                    city
                )
            })
    }

    private fun setForecastResponse(
        forecast: Forecast,
        forecastList: List<ForecastList>,
        city: City
    ): ForecastResponse {
        var response = ForecastResponse()
        val responseList = ArrayList<ForecastListResponse>()
        response.cod = forecast.cod
        response.city = city
        forecastList.forEach { forecast ->
            if (getDateFromUTCTimestamp(forecast.dt.toLong(), HOUR_FORMAT_WITHOUT_SECONDS).equals(TIME_SEL)) {
                responseList.add(
                    ForecastListResponse(
                        city_id = forecast.city_id,
                        dt = forecast.dt,
                        dt_txt = forecast.dtTxt,
                        main = null,
                        weather = null,
                        clouds = null,
                        wind = null
                    )
                )
            }
        }
        response.list = responseList

        return response
    }

    private fun fetchForecastListResponse(fResponse: List<ForecastListResponse>) {
        //setIsLoading(true)
       //Log.d(TAG, "fetchLocalForecast")
        compositeDisposable.add(
            Observable.fromIterable(fResponse)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ list ->
                    if (list != null) {
                       //Log.d(TAG, "setViewModelList to LiveData")
                        getForecastList(list)
                    }
                    //   setIsLoading(false)
                }, { throwable ->
                    setIsLoading(false)
                    throwable.printStackTrace()
                    navigator!!.handleError(throwable)
                })

        )
    }

    private fun getForecastList(response: ForecastListResponse) {
        setIsLoading(true)
       //Log.d(TAG, "checkNearestCity")
        compositeDisposable.add(
            combineForecastListsWithOthers(response)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(saverObserver())
        )
    }

    private fun saverObserver(): DisposableObserver<ForecastListResponse> {
        return object : DisposableObserver<ForecastListResponse>() {
            override fun onComplete() {
                setIsLoading(false)
               //Log.d(TAG, "saverObserver - onComplete")
                forecastResponse.list = forecastResponsesList.sortedBy { it.dt }
                showRepos.value = getViewModel(forecastResponse)
            }

            override fun onError(e: Throwable) {
                setIsLoading(true)
            }

            override fun onNext(list: ForecastListResponse) {
                setIsLoading(true)
               //Log.d(TAG, "saverObserver - onNext")
                forecastResponsesList.add(list)
            }
        }
    }

    private fun combineForecastListsWithOthers(forecastList: ForecastListResponse): Observable<ForecastListResponse> {

        val dt = forecastList.dt
        val cityId = forecastList.city_id

        return Observable.zip(
            Observable.just(forecastList),
            dataManager.getWeatherListByDtAndId(cityId.toInt(), dt),
            dataManager.getMainListByDtAndId(cityId.toInt(), dt),
            Function3 { forecastList: ForecastListResponse, wList: List<WeatherList>, mList: List<MainList> ->
                return@Function3 setForecastListsResponse(
                    forecastList,
                    toResponseWeatherList(wList),
                    mList.first().toMainListResponse()
                )
            })

    }

    private fun setForecastListsResponse(
        list: ForecastListResponse,
        wList: List<WeatherListResponse>,
        mList: MainListResponse
    ): ForecastListResponse {
        list.weather = wList
        list.main = mList

        return list
    }

    private fun combineForecastLists(fResponse: ForecastResponse): Observable<ForecastResponse> {
        val dt = fResponse.list?.first()?.dt
        val cityId = fResponse.city?.id

        return Observable.zip(
            Observable.just(fResponse),
            cityId?.let { dt?.let { listDt -> dataManager.getWeatherListByDtAndId(it.toInt(), listDt) } },
            cityId?.let { dt?.let { listDt -> dataManager.getMainListByDtAndId(it.toInt(), listDt) } },
            Function3 { fResponse: ForecastResponse, wList: List<WeatherList>, mList: List<MainList> ->
                return@Function3 setForecastListsResponse(
                    fResponse,
                    toResponseWeatherList(wList),
                    mList.first().toMainListResponse()
                )
            })
    }

    private fun setForecastListsResponse(
        response: ForecastResponse,
        wList: List<WeatherListResponse>,
        mList: MainListResponse
    ): ForecastResponse {
        response.list?.first()?.weather = wList
        response.list?.first()?.main = mList

        return response
    }

    private fun toResponseWeatherList(w: List<WeatherList>): List<WeatherListResponse> {
        return w.flatMap { item -> listOf(item.toWeatherListResponse()) }
    }

    private fun getViewModel(forecast: ForecastResponse): List<ShowDaysItemModel> {
        val forecastList = forecast.list
        val forecastItemList = ArrayList<ShowDaysItemModel>()
        if (forecastList != null) {
            forecastList.indices.let { ind ->
                for (i in ind) {
                    val list = forecastList[i]
                    val iconUrl = list.weather?.get(0)?.icon
                    forecastItemList.add(
                        forecast.city?.name?.let { cityName ->
                            ShowDaysItemModel(
                                ApiEndPoint.ENDPOINT_FORECAST_ICON + iconUrl + ApiEndPoint.ENDPOINT_FORECAST_ICON_TAIL,
                                cityName,
                                list.main?.temp.toString(),
                                forecast.message.toString(),
                                list.main?.temp_max.toString(),
                                getDateFromUTCTimestamp(list.dt.toLong(), DATE_FORMAT_DAY_MONTH)
                            )
                        }!!
                    )
                }
            }
        }
        return forecastItemList
    }

    fun addShowItemsToList(showItems: List<ShowDaysItemModel>) {
        forecastItemViewModels.clear()
        forecastItemViewModels.addAll(showItems)
    }

}
