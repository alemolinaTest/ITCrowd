package com.amolina.weather.clima.data.model.db

import android.arch.persistence.room.*

/**
 * Created by Amolina on 10/02/2019.
 */
@Entity(
    tableName = "current_weather",
    primaryKeys = ["dt", "city_id"],
    indices = [(Index(value = ["dt", "city_id"], unique = true))],
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CurrentWeather(

    @ColumnInfo(name = "name") var name: String,

    @ColumnInfo(name = "dt") var dt: Int,

    @ColumnInfo(name = "city_id") var id: Long,

    @ColumnInfo(name = "base") var base: String,

    @ColumnInfo(name = "visibility") var visibility: Int

)