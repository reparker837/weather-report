package com.ait.weatherreportapp

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.ait.weatherreportapp.Adapter.CityAdapter
import com.ait.weatherreportapp.Data.AppDatabase
import com.ait.weatherreportapp.Data.City
import com.ait.weatherreportapp.Touch.CityReyclerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.city_row.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class ScrollingActivity : AppCompatActivity(), CityDialog.CityHandler {

    lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        initRecyclerView()


        fab.setOnClickListener {
            showAddCityDialog()
        }



        if (!wasStartedBefore()) {
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setPrimaryText(getString(R.string.newItem))
                .setSecondaryText("Click here to add new cities")
                .show()
            saveWasStarted()
        }
    }

    fun saveWasStarted() {
        var sharedPref =
            PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPref.edit()
        editor.putBoolean("KEY_STARTED", true)
        editor.apply()
    }

    fun wasStartedBefore(): Boolean {
        var sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        return sharedPref.getBoolean("KEY_STARTED", false)
    }

    private fun initRecyclerView() {
        Thread {
            var cityList =
                AppDatabase.getInstance(this@ScrollingActivity).cityDao().getAllCity()

            runOnUiThread {
                cityAdapter = CityAdapter(this, cityList)
                recyclerCity.adapter = cityAdapter

                var itemDecoration = DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
                recyclerCity.addItemDecoration(itemDecoration)


                val callback = CityReyclerTouchCallback(cityAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerCity)
            }
        }.start()
    }

    fun showAddCityDialog() {
        CityDialog().show(supportFragmentManager, "TAG_CITY_DIALOG")
    }

    var editIndex: Int = -1

    fun saveCity(city: City) {
        Thread {
            var newId: Long =
                AppDatabase.getInstance(this).cityDao().insertCity(
                    city
                )

            city.cityId = newId

            runOnUiThread {
                cityAdapter.addCity(city)
            }
        }.start()
    }

    override fun cityCreated(item: City) {
        saveCity(item)
    }

    override fun cityUpdated(item: City) {
        Thread {
            AppDatabase.getInstance(
                this@ScrollingActivity
            ).cityDao().updateCity(item)

            runOnUiThread {
                cityAdapter.updateCityOnPosition(item, editIndex)
            }
        }.start()
    }
}