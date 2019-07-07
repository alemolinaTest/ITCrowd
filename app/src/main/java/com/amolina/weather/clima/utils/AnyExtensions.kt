package com.amolina.weather.clima.utils

import android.os.Handler
import android.os.Looper
import java.io.File

fun Any.className(): String = this::class.java.name
fun Any.simpleClassName(): String = this::class.java.simpleName

inline fun <T> tryOrDefault(f: () -> T, defaultValue: T): T {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
        defaultValue
    }
}

inline fun <T> tryOrDefaultWithOnError(f: () -> T, onError: () -> Unit, defaultValue: T): T {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
        onError()
        defaultValue
    }
}

inline fun tryWithOnError(f: () -> Unit, onError: () -> Unit) {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
        onError()
    }
}

inline fun tryOrPrintException(f: () -> Unit) {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

inline fun runWithDelay(crossinline f: () -> Unit, delayInMillis: Int) {
    Handler().postDelayed({ f() }, delayInMillis.toLong())
}

inline fun runWithDelayOnMainLooper(crossinline f: () -> Unit, delayInMillis: Int) {
    Handler(Looper.getMainLooper()).postDelayed({ f() }, delayInMillis.toLong())
}

fun File.deleteWithoutFear() {
    if (exists()) {
        delete()
    }
}

fun CharSequence?.isNotNullOrBlank(): Boolean {
    return this.isNullOrBlank().not()
}