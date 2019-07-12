package com.amolina.weather.clima.ui.show


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.amolina.weather.clima.BR
import com.amolina.weather.clima.R
import com.amolina.weather.clima.databinding.FragmentShowBinding
import com.amolina.weather.clima.ui.base.BaseFragment
import com.amolina.weather.clima.ui.showDays.ShowDaysFragment
import com.amolina.weather.clima.ui.splash.SplashViewModel
import javax.inject.Inject

/**
 * Created by Amolina on 02/07/19.
 */

class ShowFragment : BaseFragment<FragmentShowBinding, ShowViewModel>(), ShowNavigator,
        ShowAdapter.ShowAdapterListener {

    @Inject
    lateinit var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager

    @Inject
    lateinit var mShowViewModel: ShowViewModel

    @Inject
    lateinit var mSplashViewModel: SplashViewModel

    private var mFragmentShowBinding: FragmentShowBinding? = null

    private var adapter: ShowAdapter? = null

    override fun getViewModel(): ShowViewModel = mShowViewModel

    private var mListener: showDaysListener? = null

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mShowViewModel.navigator = this
        adapter = ShowAdapter()
        adapter!!.setListener(this)

        mSplashViewModel.presetSettings(false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentShowBinding = viewDataBinding
        getViewModel()
        setUp()
        subscribeToShowLiveData()
        refreshForecastList()
    }

    private fun subscribeToShowLiveData() {
        mShowViewModel.let { vm ->
            vm.showRepos.observe(this, Observer { items ->
                if (items != null) {
                    vm.addShowItemsToList(items)
                }
                setAdapterData(vm.weatherItemViewModels)
                mFragmentShowBinding?.swipeContainer?.isRefreshing = false
            })
        }
        mSplashViewModel.showWeather.observe(this, Observer {
            refreshForecastList()
        })
    }

    private fun setUp() {

        setTitle("Cities Weather")

        mFragmentShowBinding?.swipeContainer?.setOnRefreshListener {
            refreshWeatherFromApi()
        }
        mShowViewModel.let { vm ->
            if (adapter != null) {
                setAdapterData(vm.weatherItemViewModels)
            }
        }
        mFragmentShowBinding?.weatherRecyclerView.let { binding ->
            mLayoutManager.orientation = RecyclerView.VERTICAL
            binding?.layoutManager = mLayoutManager
            binding?.itemAnimator = DefaultItemAnimator()
            binding?.adapter = adapter
            binding?.setRecyclerListener(recycleListener)
        }
    }


    /**

     * RecycleListener that completely clears the [com.google.android.gms.maps.GoogleMap]

     * attached to a row in the RecyclerView.

     * Sets the map type to [com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE] and clears

     * the map.

     */

    private val recycleListener = RecyclerView.RecyclerListener { holder ->

        when (holder) {
            is ShowAdapter.ShowViewHolder -> holder.clearView()
        }
    }

    private fun setAdapterData(repoList: List<ShowItemModel>) {
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
        refreshForecastList()
    }

    override fun oShowDaysClick(cityId: Int) {
        gotoDaysFragment(cityId)
    }

    private fun gotoDaysFragment(cityId: Int) {
        baseActivity?.addFragment(ShowDaysFragment.newInstance(cityId), R.id.content, true)
    }

    private fun refreshForecastList() {

        mShowViewModel.fetchLocalWeather()
    }

    private fun refreshWeatherFromApi() {
        mSplashViewModel.presetSettings(false)
        mSplashViewModel.resetWeatherStateBeforeGet()
    }

    interface showDaysListener {
        // TODO: Update argument type and name
        fun onCallDaysFragment(cityId: Int)
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity?.finish()
    }

    companion object {

        fun newInstance(): ShowFragment {
            val fragment = ShowFragment()
            return fragment
        }
    }
}
