package com.amolina.weather.clima.data.model.db

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Amolina on 10/02/2019.
 */

@Entity(
    tableName = "forecast",
    primaryKeys = ["city_id"],
    indices = [(Index(value = ["city_id"], unique = true))],
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Forecast(

    @SerializedName("city_id")
    @ColumnInfo(name = "city_id")
    var city_id: Long,

    @Expose
    @SerializedName("cod")
    @ColumnInfo(name = "cod")
    val cod: Int,

    @SerializedName("message")
    @ColumnInfo(name = "message")
    var message: String?,

    @Expose
    @SerializedName("cnt")
    @ColumnInfo(name = "cnt")
    var cnt: Int?

)

