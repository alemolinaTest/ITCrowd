package com.amolina.weather.clima.ui.base

import androidx.recyclerview.widget.RecyclerView
import android.view.View


/**
 * Created by Amolina on 02/07/19.
 */

abstract class BaseViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(position: Int)

}
