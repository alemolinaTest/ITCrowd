package com.amolina.weather.clima.ui.base

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableBoolean

import com.amolina.weather.clima.data.DataManager
import com.amolina.weather.clima.rx.SchedulerProvider

import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Amolina on 02/07/19.
 */

abstract class BaseViewModel<N>(val dataManager: DataManager,
                                val schedulerProvider: SchedulerProvider) : ViewModel() {

    var navigator: N? = null
    val isLoading = ObservableBoolean(false)

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onViewCreated() {

    }

    fun onDestroyView() {

    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
