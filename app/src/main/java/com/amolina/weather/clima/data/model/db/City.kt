package com.amolina.weather.clima.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Amolina on 10/02/2019.
 */
@Entity(tableName = "city")
data class City(

    @Expose
    @PrimaryKey
    var id: Long,

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String,

    @Expose
    @SerializedName("country")
    @ColumnInfo(name = "country")
    var country: String,

    @Expose
    @SerializedName("selected")
    @ColumnInfo(name = "selected")
    var selected: Int? = 0,

    @Expose
    @SerializedName("distance")
    @ColumnInfo(name = "distance")
    var distance: Float?,

    @Embedded
    @SerializedName("coord")
    var coord: Location

) {
    fun setSelected() {
        this.selected = 1
    }

    fun setDistance(distance: Float) {
        this.distance = distance
    }
}
