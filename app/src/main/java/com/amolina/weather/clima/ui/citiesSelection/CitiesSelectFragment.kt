package com.amolina.weather.clima.ui.citiesSelection

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.amolina.weather.clima.BR
import com.amolina.weather.clima.R
import com.amolina.weather.clima.databinding.FragmentCitiesBinding
import com.amolina.weather.clima.ui.base.BaseFragment
import androidx.lifecycle.Observer
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import com.amolina.weather.clima.databinding.FragmentSelectCitiesBinding
import com.amolina.weather.clima.ui.cities.CitiesAdapter
import com.amolina.weather.clima.ui.cities.CitiesItemModel
import com.amolina.weather.clima.ui.cities.CitiesNavigator
import com.amolina.weather.clima.ui.cities.CitiesViewModel
import io.reactivex.disposables.CompositeDisposable
import org.herventure.android.commons.CustomDialog
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class CitiesSelectFragment : BaseFragment<FragmentSelectCitiesBinding, CitiesSelectionViewModel>(), CitiesNavigator,
        CitiesAdapter.CitiesAdapterListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    @Inject
    lateinit var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager

    @Inject
    lateinit var mCitiesViewModel: CitiesSelectionViewModel

    private lateinit var deleteConfirmDialog: CustomDialog

    private lateinit var addConfirmDialog: CustomDialog

    private var mFragmentSelectCitiesBinding: FragmentSelectCitiesBinding? = null

    private var adapter: CitiesAdapter? = null

    override fun getViewModel(): CitiesSelectionViewModel = mCitiesViewModel

    private var mListener: CitiesDaysListener? = null

    private val compositeSubscriptions: CompositeDisposable = CompositeDisposable()

    private lateinit var searchView: SearchView

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_select_cities

    var showAll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCitiesViewModel.navigator = this
        adapter = CitiesAdapter()
        adapter!!.setListener(this)
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getBoolean(ALL)?.let {
            showAll = it
        }

        mFragmentSelectCitiesBinding = viewDataBinding
        getViewModel()
        setUp()
        subscribeToCitiesLiveData()

        refreshCitiesList(showAll)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.menu_main, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        val item = menu.findItem(R.id.action_search)
        if (item != null) {
            searchView = item.actionView as SearchView

            setSearchViewListener()
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun setSearchViewListener() {

        if (::searchView.isInitialized) {

            searchView.setOnQueryTextListener(this)
            searchView.setOnCloseListener(this)

        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        mCitiesViewModel.getSearchedCities(query.toUpperCase())
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onClose(): Boolean {
        refreshCitiesList(true)
        return false
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
        mFragmentSelectCitiesBinding?.weatherRecyclerView.let { binding ->
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

    private fun refreshCitiesList(all: Boolean) {
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
    }


    interface CitiesDaysListener {
        // TODO: Update argument type and name
        fun onCallDaysFragment(cityId: Int)
    }

    companion object {
        private val ALL = "all"
        fun newInstance(all: Boolean): CitiesSelectFragment {
            val fragment = CitiesSelectFragment()
            val args = Bundle()
            args.putBoolean(ALL, all)
            fragment.arguments = args
            return fragment
        }
    }
}
