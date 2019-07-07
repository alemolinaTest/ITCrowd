package com.amolina.weather.clima.data.model.db


import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Amolina on 10/02/2019.
 */

@Entity(
    tableName = "location"
)
data class Location(

    @PrimaryKey(autoGenerate = true)
    var location_id: Int,

    @Expose
    @SerializedName("lat")
    @ColumnInfo(name = "latitude")
    var lat: Double,

    @Expose
    @SerializedName("lon")
    @ColumnInfo(name = "longitude")
    var lon: Double
)
