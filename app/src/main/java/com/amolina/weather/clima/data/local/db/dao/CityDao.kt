package com.amolina.weather.clima.data.local.db.dao

import androidx.room.*
import com.amolina.weather.clima.data.model.db.City


/**
 * Created by Amolina on 10/02/2019.
 */
//ROOM Data Access Object - database interactions
@Dao
interface CityDao {

    @Query("SELECT * FROM city")
    fun loadAll(): List<City>

    @Query("SELECT * FROM city WHERE selected = 1")
    fun loadAllSelected(): List<City>

    @Query("SELECT * FROM city WHERE distance > 0")
    fun loadAllNearest(): List<City>

    @Query("SELECT * FROM city WHERE selected = 1 and name GLOB '*' || :search|| '*'")
    fun loadSelectedSearch(search:String): List<City>

    @Query("SELECT * FROM city WHERE distance > 0 and  UPPER(name) GLOB '*' || :search|| '*'")
    fun loadNearestSearch(search:String): List<City>

    @Query("SELECT * FROM city WHERE id = :cityId")
    fun loadById(cityId: Long?): City

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: City)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(city: List<City>)

    @Update
    fun updateCity(vararg city: City)

    @Query("SELECT * FROM city WHERE name like :search ")
    fun loadAllNearTest(search:String): List<City>

    @Query("UPDATE city SET distance=0 , selected=0")
    fun resetDistance()

    @Query("DELETE FROM city WHERE id = :cityId")
    fun deleteCity(cityId: Int)
}
