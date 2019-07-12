package com.amolina.weather.clima.data.model.db

import androidx.room.*
import com.amolina.weather.clima.data.model.api.SysResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.NonNull


@Entity(
    tableName = "current_weather_sys",
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
data class CurrentWeatherSys(

    @NonNull
    @ColumnInfo(name = "city_id")
    var city_id: Long = 0,

    @NonNull
    @ColumnInfo(name = "dt")
    var dt: Int = 0,

    @Expose
    @SerializedName("type")
    @ColumnInfo(name = "type")
    var type: Int = 0,

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @SerializedName("message")
    @ColumnInfo(name = "message")
    var message: Double = 0.0,

    @Expose
    @SerializedName("country")
    @ColumnInfo(name = "country")
    var country: String = "",

    @Expose
    @SerializedName("sunrise")
    @ColumnInfo(name = "sunrise")
    var sunrise: Int = 0,

    @Expose
    @SerializedName("sunset")
    @ColumnInfo(name = "sunset")
    var sunset: Int = 0

) {
    constructor() : this(0, 0, 0, 0, 0.0, "", 0,  0)

    fun toWeatherSysResponse(): SysResponse {
        return SysResponse(
            this.type,
            this.id,
            this.message,
            this.country,
            this.sunrise,
            this.sunset
        )
    }
}
