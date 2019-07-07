package com.amolina.weather.clima.utils

import android.content.Context
import android.support.design.widget.Snackbar
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager

/**
 * Created by Amolina on 02/07/19.
 */

object ScreenUtils {

    fun getScreenWidth(context: Context): Int {
        val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources
                .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun View.showSnackbar(msgId: Int, length: Int) {
        showSnackbar(context.getString(msgId), length)
    }

    fun View.showSnackbar(msg: String, length: Int) {
        showSnackbar(msg, length, null, {})
    }

    fun View.showSnackbar(
        msgId: Int,
        length: Int,
        actionMessageId: Int,
        action: (View) -> Unit
    ) {
        showSnackbar(context.getString(msgId), length, context.getString(actionMessageId), action)
    }

    fun View.showSnackbar(
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(this, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        }
    }

}// This class is not publicly instantiable
