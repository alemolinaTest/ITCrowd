package com.amolina.weather.clima.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.amolina.weather.clima.BR
import com.amolina.weather.clima.R
import com.amolina.weather.clima.databinding.ActivityMainBinding
import com.amolina.weather.clima.ui.base.BaseActivity
import com.amolina.weather.clima.ui.cities.CitiesActivity
import com.amolina.weather.clima.ui.show.ShowFragment
import com.amolina.weather.clima.ui.show.ShowViewModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.computation
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var mMainViewModel: MainViewModel

    @Inject
    lateinit var mShowViewModel: ShowViewModel

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    var mActivityMainBinding: ActivityMainBinding? = null

    private val compositeSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun getViewModel(): MainViewModel = mMainViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = viewDataBinding
        setSupportActionBar(toolbar)

        //addFragment(ShowFragment.newInstance(),R.id.content,true)
        loadFragment(ShowFragment.newInstance())

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        nav_view.setNavigationItemSelectedListener(this)

        bindSubscription()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_cities -> {
                showCitiesActivity()
                //loadFragment(CitiesFragment.newInstance())
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentDispatchingAndroidInjector
    }

    private fun bindSubscription() {
        compositeSubscriptions.addAll(
            mMainViewModel.actionsSubject
                .subscribeOn(computation())
                .subscribe(this::eventListener))
    }

    private fun eventListener(actions: MainActions) {
        when (actions) {
            is MainActions.getLocalForecasts -> refreshForecastList()
        }
    }

    private fun refreshForecastList() {
        mShowViewModel.fetchLocalWeather()
    }

    private fun loadFragment(fragment: Fragment) {
        addFragment(fragment,R.id.content,true)
    }

    private fun showCitiesActivity() {
        val intent = CitiesActivity.getStartIntent(this,true)
        startActivity(intent)
    }

    override fun refreshContent() {
        refreshForecastList()
    }


    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
