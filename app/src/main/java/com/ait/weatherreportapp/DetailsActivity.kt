package com.ait.weatherreportapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.ait.weatherreportapp.Data.Base
import com.ait.weatherreportapp.api.CityAPI
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.city_row.*
import kotlinx.android.synthetic.main.content_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DetailsActivity : AppCompatActivity() {

    val API: String = "07bbb5497652b3fb54c83d9e87da6fe9"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        var cityName = intent.getStringExtra("CITY")

        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var cityAPI = retrofit.create(CityAPI::class.java)
        val call = cityAPI.getCity(cityName!!.toString(),
            "metric",
            API
        )
        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>, response: Response<Base>) {
                var cityDetails = response.body()




                if(cityDetails != null) {
                    tvCity.text = "${cityDetails!!.name}, ${cityDetails!!.sys!!.country}"
                    tvDescription.text = "${cityDetails!!.weather!!.get(0).description}"
                    tvCurrTemp.text = "${cityDetails!!.main!!.temp} \u2103"
                    tvMax.text = "Max Temperature: " + "${cityDetails!!.main!!.temp_max} ℃"
                    tvMin.text = "Min Temperature: " + "${cityDetails!!.main!!.temp_min} ℃"
                    tvHumidity.text = "Humidity: ${cityDetails!!.main!!.humidity}%"

                    val sunrise = "${cityDetails!!.sys!!.sunrise}".toLong()
                    tvSunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))

                    val sunset = "${cityDetails!!.sys!!.sunset}".toLong()
                    tvSunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))

                    Glide.with(this@DetailsActivity)
                        .load(
                            ("https://openweathermap.org/img/w/" +
                                    response.body()?.weather?.get(0)?.icon
                                    + ".png")
                        )
                        .into(ivIcon)
                }

            }
            override fun onFailure(call: Call<Base>, t: Throwable) {
                tvDescription.text = t.message
                tvCity.text = t.message
                tvCurrTemp.text = t.message
                tvMax.text = t.message
                tvMin.text = t.message
                tvHumidity.text = t.message
                tvSunrise.text = t.message
                tvSunset.text = t.message

            }

        })


        fab.setOnClickListener { view ->
            finish()
        }


    }

}
