package com.amolina.weather.clima.data.local.db

import androidx.room.TypeConverter
import com.amolina.weather.clima.data.model.db.City

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.ArrayList

object DataConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<Long>? {
        val listType = object : TypeToken<ArrayList<Long>>() {

        }.type
        return Gson().fromJson<ArrayList<Long>>(value, listType)
    }

    @TypeConverter
    fun fromArrayLisr(list: ArrayList<Long>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun cityFromString(value: String): List<City>? {
        val listType = object : TypeToken<List<City>>() {

        }.type
        return Gson().fromJson<List<City>>(value, listType)
    }
}