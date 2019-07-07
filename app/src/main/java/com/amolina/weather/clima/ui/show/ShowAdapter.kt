package com.amolina.weather.clima.ui.show

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.amolina.weather.clima.databinding.ItemShowEmptyViewBinding
import com.amolina.weather.clima.databinding.ItemShowViewBinding
import com.amolina.weather.clima.ui.base.BaseViewHolder

import java.util.ArrayList

/**
 * Created by Amolina on 02/07/19.
 */

class ShowAdapter : RecyclerView.Adapter<BaseViewHolder>() {

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
                ShowViewHolder(weatherViewBinding)
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

    inner class ShowViewHolder(private val mBinding: ItemShowViewBinding) : BaseViewHolder(mBinding.getRoot()),
       ShowItemModel.ShowDaysListener {
        var pos = 0

        override fun onBind(position: Int) {
            val mShowItemViewModel = mShowResponseList?.get(position)
            mShowItemViewModel?.setListener(this)
            mBinding.viewModel = mShowItemViewModel
            pos = position


            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings()
        }

        override fun onShowDaysClick() {
            mShowResponseList?.get(pos)?.cityId?.get()?.let {
                mListener!!.oShowDaysClick(it)
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

        fun oShowDaysClick(cityId:Int)
    }



    companion object {

        val VIEW_TYPE_EMPTY = 0
        val VIEW_TYPE_NORMAL = 1
    }
}