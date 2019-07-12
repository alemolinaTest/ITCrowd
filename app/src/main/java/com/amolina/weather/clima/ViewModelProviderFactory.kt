package com.amolina.weather.clima

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Amolina on 19/02/18.
 *
 *
 * A provider factory that persists ViewModels[ViewModel].
 * Used if the viewmodel has a parameterized constructor.
 */
class ViewModelProviderFactory<V : Any>(private val viewModel: V) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModel.javaClass)) {
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
