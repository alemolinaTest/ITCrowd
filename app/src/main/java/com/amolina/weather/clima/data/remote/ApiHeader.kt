package com.amolina.weather.clima.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.amolina.weather.clima.di.ApiInfo

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Amolina on 02/07/19.
 */

@Singleton
class ApiHeader @Inject
constructor(val publicApiHeader: PublicApiHeader, val protectedApiHeader: ProtectedApiHeader) {

    class PublicApiHeader @Inject
    constructor(
        @param:ApiInfo @field:Expose
        @field:SerializedName("APPID")
        var apiKey: String?
    )

    class ProtectedApiHeader(
        @field:Expose
        @field:SerializedName("APPID")
        var apiKey: String?, @field:Expose
        @field:SerializedName("access_token")
        var accessToken: String?
    )
}
