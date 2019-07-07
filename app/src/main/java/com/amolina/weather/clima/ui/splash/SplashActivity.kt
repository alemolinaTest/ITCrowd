package com.amolina.weather.clima.ui.splash

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import com.amolina.weather.clima.BR
import com.amolina.weather.clima.R
import com.amolina.weather.clima.data.model.db.City
import com.amolina.weather.clima.databinding.ActivitySplashBinding
import com.amolina.weather.clima.ui.base.BaseActivity
import com.amolina.weather.clima.ui.base.tryOrPrintException
import com.amolina.weather.clima.ui.main.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import javax.inject.Inject

/**
 * Created by Amolina on 02/07/19.
 */

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {

    @Inject
    lateinit var mSplashViewModel: SplashViewModel

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.activity_splash

    override fun getViewModel() = mSplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        lifecycle.addObserver(mSplashViewModel)

        mSplashViewModel.navigator = this
        mSplashViewModel.presetSettings(true)
        requestLocationPermission()

    }

    override fun onDestroy() {
        mSplashViewModel.compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun openMainActivity() {
        val intent = MainActivity.getStartIntent(this@SplashActivity)
        startActivity(intent)
        this.finishAffinity()
    }

    override fun loadCities() {
        loadJsonCities(applicationContext, "cities.json")
    }

    override fun handleError(throwable: Throwable) {
        // handle error
    }

    private fun loadJsonCities(context: Context, fileName: String) {
        tryOrPrintException {
            val inputStream = context.assets.open(fileName)
            var jsonReader = JsonReader(inputStream.reader())
            val cityType = object : TypeToken<List<City>>() {}.type
            //Parse JSON to City objects
            val cityList: List<City> = Gson().fromJson(jsonReader, cityType)
            val argCities = cityList.filter { it.country.equals("AR") }
            mSplashViewModel.saveCities(argCities.toList())
        }

    }

    private fun requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, resources.getString(R.string.location_permission_denied), Toast.LENGTH_LONG).show()

            } else {
                // Request the permission. The result will be received in onRequestPermissionResult().
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
            }
        } else {
            startData()
        }
    }

    private fun startData() {
        mSplashViewModel.resetWeatherStateBeforeGet()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                startData()
            } else {
                // Permission request was denied.
                Toast.makeText(this, resources.getString(R.string.location_permission_denied), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun refreshContent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        const val MIN_DISTANCE_FOR_POSITION_IN_METERS = 10000f
        const val PERMISSION_REQUEST_LOCATION = 0
    }
}
