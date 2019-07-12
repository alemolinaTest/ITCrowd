package com.amolina.weather.clima.data.model.db

import androidx.room.*
import com.amolina.weather.clima.data.model.api.WeatherListResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.NonNull


@Entity(
    tableName = "weather_list",
    primaryKeys = ["city_id", "list_dt"],
    indices = [(Index(value = ["city_id", "list_dt"], unique = true))],
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ForecastList::class,
            parentColumns = ["dt", "city_id"],
            childColumns = ["list_dt", "city_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WeatherList(

    @Expose
    @ColumnInfo(name = "id")
    var id: Int,

    @NonNull
    @ColumnInfo(name = "city_id")
    var city_id: Long,

    @NonNull
    @ColumnInfo(name = "list_dt")
    var list_dt: Int,

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

