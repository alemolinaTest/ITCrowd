package com.amolina.weather.clima.di

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Qualifier

/**
 * Created by Amolina on 07/12/17.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
annotation class DatabaseInfo
