package com.amolina.weather.clima.ui.base

import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Created by Amolina on 02/07/19.
 */

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(position: Int)

}
