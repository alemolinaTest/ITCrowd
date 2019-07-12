package com.amolina.weather.clima.ui.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import com.amolina.weather.clima.databinding.ItemShowEmptyViewBinding
import com.amolina.weather.clima.databinding.ItemShowViewBinding
import com.amolina.weather.clima.ui.base.BaseViewHolder
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import java.util.*


/**
 * Created by Amolina on 02/07/19.
 */

class ShowAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder>() {

    private val mShowResponseList: MutableList<ShowItemModel>?
    private var mListener: ShowAdapterListener? = null

    init {
        this.mShowResponseList = ArrayList()
    }

    fun setListener(listener: ShowAdapterListener) {
        this.mListener = listener
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val weatherViewBinding = ItemShowViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShowViewHolder(weatherViewBinding, parent)
            }
            VIEW_TYPE_EMPTY -> {
                val emptyViewBinding =
                        ItemShowEmptyViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyViewHolder(emptyViewBinding)
            }
            else -> {
                val emptyViewBinding =
                        ItemShowEmptyViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyViewHolder(emptyViewBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mShowResponseList != null && mShowResponseList.size > 0) {
            VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_EMPTY
        }
    }

    override fun getItemCount(): Int {
        return if (mShowResponseList != null && mShowResponseList.size > 0) {
            mShowResponseList.size
        } else {
            1
        }
    }

    fun addItems(repoList: List<ShowItemModel>) {
        mShowResponseList!!.addAll(repoList)
        notifyDataSetChanged()
    }

    fun clearItems() {
        mShowResponseList!!.clear()
    }

    inner class ShowViewHolder(private val mBinding: ItemShowViewBinding, private val parent: ViewGroup) :
            BaseViewHolder(mBinding.getRoot()), ShowItemModel.ShowDaysListener, OnMapReadyCallback {

        private var coord: LatLng? = null
        lateinit var mapCurrent: GoogleMap
        private val mapView: MapView by lazy { mBinding.mapView }
        private val city: String by lazy { mBinding.cityNameTextView.text.toString() }

        /** Initialises the MapView by calling its lifecycle methods */

        init {
            with(mapView) {
                // Initialise the MapView
                onCreate(null)
                // Set the map ready callback to receive the GoogleMap object
                getMapAsync(this@ShowViewHolder)
            }
        }

        var pos = 0

        override fun onBind(position: Int) {
            val mShowItemViewModel = mShowResponseList?.get(position)
            mShowItemViewModel?.setListener(this)
            mBinding.viewModel = mShowItemViewModel
            pos = position

            coord = mShowItemViewModel?.coord

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings()
        }

        override fun onMapReady(googleMap: GoogleMap?) {
            MapsInitializer.initialize(parent.context)
            mapCurrent = googleMap!!

            setMapLocation()

        }

        private fun setMapLocation() {

            if (!::mapCurrent.isInitialized) return

            with(mapCurrent) {

                moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 13f))

                addMarker(coord?.let { com.google.android.gms.maps.model.MarkerOptions().position(it) })

                mapType = GoogleMap.MAP_TYPE_NORMAL

                setOnMapClickListener {

                    android.widget.Toast.makeText(parent.context, "Clicked on ${city}",

                            android.widget.Toast.LENGTH_SHORT).show()

                }

            }

        }

        override fun onShowDaysClick() {
            mShowResponseList?.get(pos)?.cityId?.get()?.let {
                mListener!!.oShowDaysClick(it)
            }
        }

        fun clearView() {
            if (!::mapCurrent.isInitialized) return

            with(mapCurrent) {

                // Clear the map and free up resources by changing the map type to none
                clear()
                mapType = GoogleMap.MAP_TYPE_NONE
            }
        }

    }


    inner class EmptyViewHolder(private val mBinding: ItemShowEmptyViewBinding) : BaseViewHolder(mBinding.root),
            ShowEmptyItemModel.ShowEmptyItemViewModelListener {

        override fun onBind(position: Int) {
            val emptyItemViewModel = ShowEmptyItemModel(this)
            mBinding.viewModel = emptyItemViewModel
        }

        override fun onRetryClick() {
            mListener!!.onRetryClick()
        }
    }

    interface ShowAdapterListener {
        fun onRetryClick()

        fun oShowDaysClick(cityId: Int)
    }


    companion object {

        val VIEW_TYPE_EMPTY = 0
        val VIEW_TYPE_NORMAL = 1
    }
}