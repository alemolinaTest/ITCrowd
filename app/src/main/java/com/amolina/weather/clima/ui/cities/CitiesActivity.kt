package com.amolina.weather.clima.ui.cities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.amolina.weather.clima.BR
import com.amolina.weather.clima.R
import com.amolina.weather.clima.databinding.ActivityCitiesBinding
import com.amolina.weather.clima.ui.base.BaseActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cities.*
import javax.inject.Inject

class CitiesActivity : BaseActivity<ActivityCitiesBinding, CitiesViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var mCitiesViewModel: CitiesViewModel

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    var mActivityCitiesBinding: ActivityCitiesBinding? = null

    private val compositeSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun getViewModel(): CitiesViewModel = mCitiesViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_cities

    private var isMain: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isMain = intent.getBooleanExtra(MAIN,true)

        mActivityCitiesBinding = viewDataBinding
        setSupportActionBar(city_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpFragmentAndFloatButton()

        bindSubscription()
        //todo revisar disposables
    }

    private fun setUpFragmentAndFloatButton(){
        return when {
            isMain -> {
                loadCitiesFragment(CitiesFragment.newInstance(all=false))

                title = "Cities"

                val fab: FloatingActionButton? = mActivityCitiesBinding?.fab
                if (fab == null) {
                } else {
                    fab.setOnClickListener {
                        loadCitiesFragment(CitiesFragment.newInstance(all=true))
                    }
                }
            }
            else -> {
                loadCitiesFragment(CitiesFragment.newInstance(all=false))

                title = "Select a City"}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Respond to the action bar's Up/Home button
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
            R.id.action_search -> true
        }
        return super.onOptionsItemSelected(item)
    }

    private lateinit var searchView: SearchView

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return when {
            isMain -> setNoSearchOptionMenu(menu)
            else -> setSearchOptionMenu(menu)
        }
    }

    private fun setSearchOptionMenu(menu: Menu): Boolean {
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
            mCitiesViewModel.getAllCities()

            false
        })


        // listening to search query text change
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mCitiesViewModel.getSearchedCities(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                return false
            }
        })

        return true
    }

    private fun setNoSearchOptionMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cities, menu)

        return true
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    private fun bindSubscription() {
        compositeSubscriptions.addAll(
            mCitiesViewModel.actionsSubject
                .subscribeOn(Schedulers.computation())
                .subscribe(this::eventListener)
        )
    }

    private fun eventListener(actions: CitiesActions) {
        when (actions) {
            is CitiesActions.getCities -> refreshCitiesList()
        }
    }

    private fun refreshCitiesList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadCitiesFragment(fragment: Fragment) {
        addFragment(fragment, R.id.content, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToHome()
    }

    private fun goToHome() {
        finish()
    }

    companion object {
        private val MAIN = "main"
        fun getStartIntent(context: Context, main: Boolean): Intent {
            val intent = Intent(context, CitiesActivity::class.java)
            intent.putExtra(MAIN, main)
            return intent
        }
    }

    override fun refreshContent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
