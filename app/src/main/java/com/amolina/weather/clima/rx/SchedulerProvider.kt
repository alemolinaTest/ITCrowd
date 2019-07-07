package com.amolina.weather.clima.rx

import io.reactivex.Scheduler

/**
 * Created by Amolina on 02/07/19.
 */

interface SchedulerProvider {

    fun ui(): Scheduler //observeOn

    fun computation(): Scheduler

    fun io(): Scheduler //subscribeOn

}
