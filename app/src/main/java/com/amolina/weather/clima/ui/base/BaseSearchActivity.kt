package com.amolina.weather.clima.ui.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import androidx.core.os.LocaleListCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager

import com.amolina.weather.clima.utils.CommonUtils
import com.amolina.weather.clima.utils.NetworkUtils

import dagger.android.AndroidInjection

import android.os.Build.VERSION.SDK_INT
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.amolina.weather.clima.R
import com.amolina.weather.clima.ui.view.FragmentListener


/**
 * Created by Amolina on 02/07/19.
 */
//extend annotation
abstract class BaseSearchActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(), BaseFragment.Callback,
    FragmentListener {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private var mProgressDialog: ProgressDialog? = null

    var viewDataBinding: T? = null

    private var mViewModel: V? = null


    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */

    abstract fun getLayoutId(): Int

    abstract fun doSearch(searchText:String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inject the activity
        performDependencyInjection()
        //databinding from the layout
        performDataBinding()
    }

    private lateinit var searchText: String

    private lateinit var searchView: SearchView

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView?.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView?.setMaxWidth(Integer.MAX_VALUE)

        searchView?.setOnCloseListener(SearchView.OnCloseListener {
            searchText = ""

            false
        })


        // listening to search query text change
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                searchText = query
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)

    }


    fun performDependencyInjection() {
        //inject method for Activities
        AndroidInjection.inject(this)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        viewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        viewDataBinding!!.executePendingBindings()
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtils.showLoadingDialog(this)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    companion object {


        fun getLocales(configuration: Configuration): LocaleListCompat {
            return if (SDK_INT >= 24) {
                LocaleListCompat.wrap(configuration.locales)
            } else {
                LocaleListCompat.create(configuration.locale)
            }
        }
    }

    fun addFragment(
            fragmentView: Fragment,
            addToStack: Boolean,
            container:Int,
            @AnimRes enter: Int = 0,
            @AnimRes exit: Int = 0
    ) {
        addFragment(fragmentView,container, addToStack, enter, exit)
    }

    override fun addFragment(
            fragmentView: Fragment,
            @IdRes containerId: Int,
            addToStack: Boolean,
            @AnimRes enter: Int,
            @AnimRes exit: Int
    ) {
        tryOrPrintException {
                supportFragmentManager.beginTransaction().apply {
                    setCustomAnimations(enter, exit, enter, exit)
                    add(containerId, fragmentView, fragmentView.className())
                    if (addToStack) {
                        addToBackStack(fragmentView.className())
                    }
                }.commit()
        }
    }

    fun removeFragment(fragmentView: Fragment, @AnimRes animExit: Int = 0, allowStateLoss: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(0, animExit)
        ft.remove(fragmentView)
        if (allowStateLoss) {
            ft.commitAllowingStateLoss()
        } else {
            ft.commit()
        }
        supportFragmentManager.popBackStack(fragmentView.className(), POP_BACK_STACK_INCLUSIVE)
    }

}

