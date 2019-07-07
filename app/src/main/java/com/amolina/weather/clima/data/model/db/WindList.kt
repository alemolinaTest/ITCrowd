package com.amolina.weather.clima.data.model.db

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.NonNull

@Entity(
    tableName = "wind_list",
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
data class WindList(

    @NonNull
    @ColumnInfo(name = "city_id")
    var city_id: Long,

    @NonNull
    @ColumnInfo(name = "list_dt")
    var list_dt: Int,

    @Expose
    @SerializedName("speed")
    @ColumnInfo(name = "speed")
    var speed: Double?,

    @Expose
    @SerializedName("deg")
    @ColumnInfo(name = "degree")
    var deg: Double?

)
