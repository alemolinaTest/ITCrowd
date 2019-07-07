package com.amolina.weather.clima.ui.location

import android.Manifest
import android.content.Context
import android.support.annotation.RequiresPermission
import com.amolina.weather.clima.utils.CommonUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject


typealias AndroidLocation = android.location.Location

class AndroidLocationProvider @Inject constructor(private val context: Context) : LocationProvider {

    private val fusedLocationClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun getLocation(): Single<Location> {
        return Single.create { emitter ->
            fusedLocationClient.lastLocation
                    .addOnSuccessListener {
                        if (it != null) {
                            emitter.onSuccess(Location(it.latitude, it.longitude))
                        } else {
                            emitter.onError(Throwable("No Location available"))
                        }
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }

        }
    }

    override fun getDistanceBetween(source: Location, destination: Location): Float {
        val result = FloatArray(3)
        AndroidLocation.distanceBetween(
                source.latitude,
                source.longitude,
                destination.latitude,
                destination.longitude,
                result
        )
        return result.first()
    }

    override fun isLocationEnableByUser(): Observable<Boolean> {
        return Observable.just(CommonUtils.isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION))
    }
}