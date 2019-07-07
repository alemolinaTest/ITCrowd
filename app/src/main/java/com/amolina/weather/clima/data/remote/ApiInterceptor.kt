package com.amolina.weather.clima.data.remote


import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * [Interceptor] implementation to add authorization header to request.
 * Created by mpierucci on 6/3/17.
 */

class ApiInterceptor : Interceptor {
    private var authValue: String? = null

    fun clearAuthValue() {
        authValue = null
    }

    fun setAuthValue(authValue: String?) {
        this.authValue = authValue
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        if (authValue != null) {

            builder.addHeader("X_AUTH_TOKEN", authValue)

        }
        builder.addHeader("Content-Language", Locale.getDefault().getLanguage())
        builder.addHeader("X_COURSE_ID", "GLOBAL")
        return chain.proceed(builder.build())
    }
}
