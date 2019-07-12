package com.amolina.weather.clima.data.model.db

import androidx.room.*
import com.amolina.weather.clima.data.model.api.MainListResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.NonNull


@Entity(
        tableName = "main_list",
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
            ),
            ForeignKey(
                    entity = ForecastList::class,
                    parentColumns = ["dt", "city_id"],
                    childColumns = ["list_dt", "city_id"],
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class MainList(

        @NonNull
        @ColumnInfo(name = "city_id")
        var city_id: Long = 0,

        @NonNull
        @ColumnInfo(name = "list_dt")
        var list_dt: Int = 0,

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

        @SerializedName("sea_level")
        @ColumnInfo(name = "sea_level")
        var seaLevel: Double? = 0.0,

        @SerializedName("grnd_level")
        @ColumnInfo(name = "grnd_level")
        var grndLevel: Double? = 0.0,

        @SerializedName("humidity")
        @ColumnInfo(name = "humidity")
        var humidity: Double? = 0.0,

        @Expose
        @SerializedName("temp_kf")
        @ColumnInfo(name = "temp_kf")
        var tempKf: Double? = 0.0

) {
    constructor() : this(0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    fun toMainListResponse(): MainListResponse {
        return MainListResponse(
                temp = this.temp,
                temp_min = this.tempMin,
                temp_max = this.tempMax,
                pressure = this.pressure,
                sea_level = this.seaLevel,
                grnd_level = this.grndLevel,
                humidity = this.humidity,
                temp_kf = this.tempKf
        )
    }
}
