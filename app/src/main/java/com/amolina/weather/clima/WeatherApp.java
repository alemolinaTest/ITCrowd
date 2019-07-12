package com.amolina.weather.clima;

import android.app.Activity;
import android.app.Application;
import androidx.fragment.app.Fragment;
import com.amolina.weather.clima.di.component.DaggerAppComponent;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.facebook.stetho.Stetho;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import javax.inject.Inject;

/**
 * Created by Amolina on 02/07/19.
 */

public class WeatherApp extends Application implements HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        //Dagger 2 creates the DaggerComponent classes during compilation
        //see AppComponent
        Stetho.initializeWithDefaults(this);

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);

        //set style to text views like style="@style/TextStyle.Heading"
        //using fontPath with custom fonts (ttf)
        CalligraphyConfig.initDefault(mCalligraphyConfig);


        //instead of retrofit, implement http2, reduce latency, 50% faster
        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

}
