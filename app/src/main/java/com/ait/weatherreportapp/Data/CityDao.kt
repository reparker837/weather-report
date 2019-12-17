package com.ait.weatherreportapp.Data

import androidx.room.*

@Dao
interface CityDao {
    @Query("SELECT * FROM city")
    fun getAllCity() : List<City>

    @Insert
    fun insertCity(city: City) : Long

    @Delete
    fun deleteCity(city: City)

    @Update
    fun updateCity(city: City)

    @Query("DELETE FROM city")
    fun deleteAllCity()
}