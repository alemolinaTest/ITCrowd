package com.amolina.weather.clima.ui.show

import androidx.databinding.ObservableField

/**
 * Created by Amolina on 02/07/19.
 */

class ShowItemModel(imageUrl: String, city: String, temp: String, tempMin: String, tempMax: String, time: String,
                    visibility: String, country: String, sunrise: String, sunset: String, cityId: Int,
                    pressure: String, humidity: String) {

    lateinit var mListener: ShowDaysListener

    fun setListener(listener: ShowDaysListener) {
        mListener = listener
    }

    fun onShowDaysClick() {
        mListener.onShowDaysClick()
    }

    interface ShowDaysListener {
        fun onShowDaysClick()
    }

    var cityId = ObservableField<Int>(cityId)
    var imageUrl = ObservableField<String>(imageUrl)
    var city = ObservableField<String>(city)
    var temp = ObservableField<String>(temp)
    var tempMin = ObservableField<String>(tempMin)
    var tempMax = ObservableField<String>(tempMax)
    var time = ObservableField<String>(time)
    var visibility = ObservableField<String>(visibility)
    val country = ObservableField<String>(country)
    val sunrise = ObservableField<String>(sunrise)
    val sunset = ObservableField<String>(sunset)
    val pressure = ObservableField<String>(pressure)
    val humidity = ObservableField<String>(humidity)
}
