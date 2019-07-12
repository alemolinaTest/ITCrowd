package com.amolina.weather.clima.ui.cities

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.amolina.weather.clima.BR
import com.amolina.weather.clima.R
import com.amolina.weather.clima.databinding.FragmentCitiesBinding
import com.amolina.weather.clima.ui.base.BaseFragment
import androidx.lifecycle.Observer
import io.reactivex.disposables.CompositeDisposable
import org.herventure.android.commons.CustomDialog
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class CitiesFragment : BaseFragment<FragmentCitiesBinding, CitiesViewModel>(), CitiesNavigator,
    CitiesAdapter.CitiesAdapterListener {

    @Inject
    lateinit var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager

    @Inject
    lateinit var mCitiesViewModel: CitiesViewModel

    private lateinit var deleteConfirmDialog: CustomDialog

    private lateinit var addConfirmDialog: CustomDialog

    private var mFragmentCitiesBinding: FragmentCitiesBinding? = null

    private var adapter: CitiesAdapter? = null

    override fun getViewModel(): CitiesViewModel = mCitiesViewModel

    private var mListener: CitiesDaysListener? = null

    private val compositeSubscriptions: CompositeDisposable = CompositeDisposable()

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_cities

    var showAll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCitiesViewModel.navigator = this
        adapter = CitiesAdapter()
        adapter!!.setListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getBoolean(ALL)?.let {
            showAll = it
        }

        mFragmentCitiesBinding = viewDataBinding
        getViewModel()
        setUp()
        subscribeToCitiesLiveData()

        refreshCitiesList(showAll)

    }

    private fun subscribeToCitiesLiveData() {
        mCitiesViewModel.let { vm ->
            vm.showCities.observe(this, Observer { items ->
                if (items != null) {
                    vm.addCitiesItemsToList(items)
                }
                setAdapterData(vm.citiesItemViewModels)
            })

            vm.cityDeleted.observe(this, Observer { refreshCitiesList(false) })

            vm.cityAdded.observe(this, Observer { refreshCitiesList(false) })
        }
    }

    private fun setUp() {

        mCitiesViewModel.let { vm ->
            if (adapter != null) {
                setAdapterData(vm.citiesItemViewModels)
            }
        }
        mFragmentCitiesBinding?.weatherRecyclerView.let { binding ->
            mLayoutManager?.orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
            binding?.layoutManager = mLayoutManager
            binding?.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            binding?.adapter = adapter
        }

        if (showAll) {
            setAddDialog()
        } else {
            setDeleteDialog()
        }
    }

    private fun setAdapterData(repoList: List<CitiesItemModel>) {
        adapter?.clearItems()
        adapter?.addItems(repoList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun handleError(throwable: Throwable) {
        // handle error
    }

    override fun oShowCitiesClick(cityId: Int) {
        if (!showAll) {
            deleteConfirmDialog.targetActionId = cityId
            deleteConfirmDialog.show(fragmentManager, "delete_city")
        } else {
            addConfirmDialog.targetActionId = cityId
            addConfirmDialog.show(fragmentManager, "add_city")
        }
    }

    private fun deleteCity(cityId: Int) {
        mCitiesViewModel.deleteCity(cityId)
    }

    private fun addCity(cityId: Int) {
        mCitiesViewModel.addCity(cityId)
    }

    private fun refreshCitiesList(all:Boolean) {
        if (all) {
            mCitiesViewModel.getAllCities()
        } else {
            mCitiesViewModel.getNearAndSelectedAllCities()
        }
    }

    private fun setDeleteDialog() {
        deleteConfirmDialog = CustomDialog.newInstance(
            this.getString(R.string.delete_title),
            this.getString(R.string.delete_description),
            this.getString(R.string.cancel_button),
            this.getString(R.string.ok_button)
        )
        deleteConfirmDialog.confirmListener = {
            deleteCity(deleteConfirmDialog.targetActionId)
        }
    }

    private fun setAddDialog() {
        addConfirmDialog = CustomDialog.newInstance(
            this.getString(R.string.add_title),
            this.getString(R.string.add_description),
            this.getString(R.string.cancel_button),
            this.getString(R.string.ok_button)
        )
        addConfirmDialog.confirmListener = {
            addCity(addConfirmDialog.targetActionId)
        }

    }

    override fun onDetach() {
        super.onDetach()
        baseActivity?.finish()
    }

    interface CitiesDaysListener {
        // TODO: Update argument type and name
        fun onCallDaysFragment(cityId: Int)
    }

    companion object {
        private val ALL = "all"
        fun newInstance(all: Boolean): CitiesFragment {
            val fragment = CitiesFragment()
            val args = Bundle()
            args.putBoolean(ALL, all)
            fragment.arguments = args
            return fragment
        }
    }
}
