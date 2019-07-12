package  com.amolina.weather.clima.ui.view

import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

interface FragmentListener {

    fun addFragment(
            fragmentView: Fragment, @IdRes containerId: Int,
            addToStack: Boolean = false, @AnimRes enter: Int = 0, @AnimRes exit: Int = 0
    )

}
