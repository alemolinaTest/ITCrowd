package com.amolina.weather.clima.ui.cities

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.amolina.weather.clima.databinding.ItemCityBinding

import com.amolina.weather.clima.databinding.ItemShowEmptyViewBinding
import com.amolina.weather.clima.ui.base.BaseViewHolder
import com.amolina.weather.clima.ui.show.ShowEmptyItemModel

import java.util.ArrayList

/**
 * Created by Amolina on 02/07/19.
 */

class CitiesAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder>() {

    private val mShowResponseList: MutableList<CitiesItemModel>?
    private var mListener: CitiesAdapterListener? = null

    init {
        this.mShowResponseList = ArrayList()
    }

    fun setListener(listener: CitiesAdapterListener) {
        this.mListener = listener
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val weatherViewBinding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CityViewHolder(weatherViewBinding)
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

    fun addItems(repoList: List<CitiesItemModel>) {
        mShowResponseList!!.addAll(repoList)
        notifyDataSetChanged()
    }

    fun clearItems() {
        mShowResponseList!!.clear()
    }

    inner class CityViewHolder(private val mBinding: ItemCityBinding) : BaseViewHolder(mBinding.getRoot()),
       CitiesItemModel.ShowDaysListener {
        var pos = 0

        override fun onBind(position: Int) {
            val mCitiesItemViewModel = mShowResponseList?.get(position)
            mCitiesItemViewModel?.setListener(this)
            mBinding.viewModel = mCitiesItemViewModel
            pos = position


            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings()
        }

        override fun onShowCitiesClick() {
            mShowResponseList?.get(pos)?.cityId?.get()?.let {
                mListener!!.oShowCitiesClick(it)
            }
        }
    }

    inner class EmptyViewHolder(private val mBinding: ItemShowEmptyViewBinding) : BaseViewHolder(mBinding.root),
        ShowEmptyItemModel.ShowEmptyItemViewModelListener {
        override fun onRetryClick() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onBind(position: Int) {
            val emptyItemViewModel = ShowEmptyItemModel(this)
            mBinding.viewModel = emptyItemViewModel
        }

    }

    interface CitiesAdapterListener {

        fun oShowCitiesClick(it: Int)
    }



    companion object {

        val VIEW_TYPE_EMPTY = 0
        val VIEW_TYPE_NORMAL = 1
    }
}