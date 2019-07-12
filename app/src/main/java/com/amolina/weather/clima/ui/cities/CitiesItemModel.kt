package com.amolina.weather.clima.ui.cities

import androidx.databinding.ObservableField

/**
 * Created by Amolina on 02/07/19.
 */

class CitiesItemModel(name: String, id: Int) {

    lateinit var mListener: ShowDaysListener

    fun setListener(listener: ShowDaysListener) {
        mListener = listener
    }

    fun onShowDaysClick() {
        mListener.onShowCitiesClick()
    }

    interface ShowDaysListener {
        fun onShowCitiesClick()
    }

    var cityName = ObservableField<String>(name)
    var cityId = ObservableField<Int>(id)

}
