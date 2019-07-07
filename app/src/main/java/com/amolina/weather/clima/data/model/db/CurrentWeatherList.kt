package com.amolina.weather.clima.data.model.db

import android.arch.persistence.room.*
import com.amolina.weather.clima.data.model.api.WeatherListResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.NonNull


@Entity(
    tableName = "current_weather_list",
    primaryKeys = ["city_id", "dt"],
    indices = [(Index(value = ["city_id", "dt"], unique = true))],
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CurrentWeather::class,
            parentColumns = ["dt", "city_id"],
            childColumns = ["dt", "city_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CurrentWeatherList(

    @NonNull
    @ColumnInfo(name = "city_id")
    var city_id: Long,

    @NonNull
    @ColumnInfo(name = "dt")
    var list_dt: Int,

    @Expose
    @ColumnInfo(name = "id")
    var id: Int,

    @Expose
    @SerializedName("main")
    @ColumnInfo(name = "main")
    var main: String,

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String,

    @Expose
    @SerializedName("icon")
    @ColumnInfo(name = "icon")
    var icon: String

) {
    constructor() : this(0, 0, 0, "", "", "")

    fun toWeatherListResponse(): WeatherListResponse {
        return WeatherListResponse(this.id, this.main, this.description, this.icon)
    }
}

