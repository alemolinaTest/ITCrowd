package com.amolina.weather.clima.data.model.db

import androidx.room.*

/**
 * Created by Amolina on 10/02/2019.
 */
@Entity(
    tableName = "forecast_list",
    primaryKeys = ["dt", "city_id"],
    indices = [(Index(value = ["dt", "city_id"], unique = true))],
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Forecast::class,
            parentColumns = ["city_id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ForecastList(

    @ColumnInfo(name = "dt") var dt: Int,

    @ColumnInfo(name = "city_id")
    var city_id: Long,

    @ColumnInfo(name = "dt_txt") var dtTxt: String = ""

)