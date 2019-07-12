package com.amolina.weather.clima.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import com.amolina.weather.clima.ui.show.ShowAdapter
import com.amolina.weather.clima.ui.show.ShowItemModel

import com.bumptech.glide.Glide


import java.util.ArrayList


/**
 * Created by Amolina on 02/07/19.
 */

object BindingUtils {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, url: String) {
        val context = imageView.context
        Glide.with(context).load(url).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("android:imageUrl")
    fun ImageView.loadImageUrl(url: String?) {
        if (url == null) return

        Glide.with(context)
                .load(url)
                .into(this)
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun addWeatherItems(
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            showItems: ArrayList<ShowItemModel>
    ) {
        val adapter = recyclerView.adapter as ShowAdapter
        adapter.clearItems()
        adapter.addItems(showItems)
    }


}// This class is not publicly instantiable
