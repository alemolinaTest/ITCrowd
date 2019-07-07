package com.amolina.weather.clima.ui.location

import io.reactivex.Observable
import io.reactivex.Single


/**
 * This controller provides the current location and methods related to it.
 */
interface LocationProvider {


    /**
     * Returns a single with the current location.
     * @return single with current location.
     */
    fun getLocation(): Single<Location>

    /**
     * Calculates the distance between two locations in meters
     * @return the distance in meters
     */
    fun getDistanceBetween(source: Location, destination: Location): Float

    /**
     * Indicates if the user have granted permission to the current location
     */
    fun isLocationEnableByUser(): Observable<Boolean>

}