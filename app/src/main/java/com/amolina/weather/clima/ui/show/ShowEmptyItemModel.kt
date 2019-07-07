package com.amolina.weather.clima.ui.show

/**
 * Created by Amolina on 02/07/19.
 */

class ShowEmptyItemModel(private val mListener: ShowEmptyItemViewModelListener) {

    fun onRetryClick() {
        mListener.onRetryClick()
    }

    interface ShowEmptyItemViewModelListener {
        fun onRetryClick()
    }
}
