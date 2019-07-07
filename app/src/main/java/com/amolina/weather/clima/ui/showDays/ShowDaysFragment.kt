package com.amolina.weather.clima.ui.showDays


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.amolina.weather.clima.BR
import com.amolina.weather.clima.R
import com.amolina.weather.clima.databinding.FragmentShowDaysBinding
import com.amolina.weather.clima.ui.base.BaseFragment
import com.amolina.weather.clima.ui.show.ShowNavigator

import javax.inject.Inject

/**
 * Created by Amolina on 02/07/19.
 */

class ShowDaysFragment : BaseFragment<FragmentShowDaysBinding, ShowDaysViewModel>(), ShowNavigator,
    ShowDaysAdapter.ShowAdapterListener {

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mShowDaysViewModel: ShowDaysViewModel

    private var mFragmentShowBinding: FragmentShowDaysBinding? = null

    private var adapter: ShowDaysAdapter? = null

    var cityId: Int? = 0

    override fun getViewModel(): ShowDaysViewModel = mShowDaysViewModel

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_show_days

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mShowDaysViewModel.navigator = this
        adapter = ShowDaysAdapter()
        adapter!!.setListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentShowBinding = viewDataBinding
        getViewModel()
        setUp()
        subscribeToShowLiveData()
        cityId = arguments?.getInt(CITY_ID)
        if (cityId != null) {
            refreshForecastList(cityId)
        }

    }

    private fun subscribeToShowLiveData() {
        mShowDaysViewModel.let { vm ->
            vm.showRepos.observe(this, Observer { items ->
                if (items != null) {
                    vm.addShowItemsToList(items)
                }
                setAdapterData(vm.forecastItemViewModels)
            })
            vm.city.observe(this, Observer {
                if (it != null) {
                    setTitle(it)
                }
            })
        }
    }

    private fun setUp() {

        mShowDaysViewModel.let { vm ->
            if (adapter != null) {
                setAdapterData(vm.forecastItemViewModels)
            }
        }
        mFragmentShowBinding?.weatherDaysRecyclerView.let { binding ->
            mLayoutManager?.orientation = LinearLayoutManager.VERTICAL
            binding?.layoutManager = mLayoutManager
            binding?.itemAnimator = DefaultItemAnimator()
            binding?.adapter = adapter
        }
    }

    private fun setAdapterData(repoList: List<ShowDaysItemModel>) {
        adapter?.clearItems()
        adapter?.addItems(repoList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun handleError(throwable: Throwable) {
        // handle error
    }

    override fun onRetryClick() {
        //refreshForecastList()
    }

    private fun refreshForecastList(cityId: Int?) {
        cityId?.let {
            with(mShowDaysViewModel) {
                fetchLocalCityForecast(it)
                fetchCity(it)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity?.refreshContent()
    }

    companion object {

        const val CITY_ID: String = ""

        fun newInstance(cityId: Int): ShowDaysFragment {
            return ShowDaysFragment().apply {
                arguments = Bundle().apply {
                    putInt(CITY_ID, cityId)
                }
            }
        }
    }
}
