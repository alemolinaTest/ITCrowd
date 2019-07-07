package com.amolina.weather.clima.ui.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.os.LocaleListCompat
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager

import com.amolina.weather.clima.utils.CommonUtils
import com.amolina.weather.clima.utils.NetworkUtils

import dagger.android.AndroidInjection

import android.os.Build.VERSION.SDK_INT


/**
 * Created by Amolina on 02/07/19.
 */
//extend annotation
abstract class BaseExtraActivity<T : ViewDataBinding, V : BaseViewModel<*>, X : BaseViewModel<*>> : AppCompatActivity(), BaseFragment.Callback {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private var mProgressDialog: ProgressDialog? = null

    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null
    private var mExtraViewModel: X? = null


    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val extraViewModel: X

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inject the activity
        performDependencyInjection()
        //databinding from the layout
        performDataBinding()
    }

    fun performDependencyInjection() {
        //inject method for Activities
        AndroidInjection.inject(this)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()

        this.mExtraViewModel = if (mExtraViewModel == null) extraViewModel else mExtraViewModel
        viewDataBinding!!.setVariable(bindingVariable, mExtraViewModel)
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


}

