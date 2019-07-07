package  com.amolina.weather.clima.ui.view

import android.support.annotation.AnimRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment

interface FragmentListener {

    fun addFragment(
        fragmentView: Fragment, @IdRes containerId: Int,
        addToStack: Boolean = false, @AnimRes enter: Int = 0, @AnimRes exit: Int = 0
    )

}
