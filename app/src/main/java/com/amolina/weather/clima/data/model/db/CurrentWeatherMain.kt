package com.amolina.weather.clima.data.model.db

import android.arch.persistence.room.*
import com.amolina.weather.clima.data.model.api.MainListResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.NonNull


@Entity(
    tableName = "current_weather_main",
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
data class CurrentWeatherMain(

    @NonNull
    @ColumnInfo(name = "city_id")
    var city_id: Long = 0,

    @NonNull
    @ColumnInfo(name = "dt")
    var dt: Int = 0,

    @Expose
    @SerializedName("temp")
    @ColumnInfo(name = "temp")
    var temp: Double? = 0.0,

    @Expose
    @SerializedName("temp_min")
    @ColumnInfo(name = "temp_min")
    var tempMin: Double? = 0.0,

    @SerializedName("temp_max")
    @ColumnInfo(name = "temp_max")
    var tempMax: Double? = 0.0,

    @SerializedName("pressure")
    @ColumnInfo(name = "pressure")
    var pressure: Double? = 0.0,

    @SerializedName("humidity")
    @ColumnInfo(name = "humidity")
    var humidity: Double? = 0.0

) {
    constructor() : this(0, 0, 0.0, 0.0, 0.0, 0.0, 0.0)

    fun toMainListResponse(): MainListResponse {
        return MainListResponse(
            this.temp,
            this.tempMin,
            this.tempMax,
            this.pressure,
            this.humidity
        )
    }
}
