package com.ait.weatherreportapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.ait.weatherreportapp.Data.Base

// host: https://api.openweathermap.org
// path: /data/2.5/weather
// query params: ? q=Budapest,hu&units=metric&appid=f3d694bc3e1d44c1ed5a97bd1120e8fe

interface CityAPI {
    @GET("/data/2.5/weather")
    fun getCity(@Query("q") city: String,
                @Query("units") units: String,
                @Query("appid") appid: String): Call<Base>
}