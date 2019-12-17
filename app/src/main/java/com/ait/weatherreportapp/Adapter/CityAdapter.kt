package com.ait.weatherreportapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ait.weatherreportapp.Data.AppDatabase
import com.ait.weatherreportapp.Data.City
import com.ait.weatherreportapp.DetailsActivity
import com.ait.weatherreportapp.R
import com.ait.weatherreportapp.ScrollingActivity
import com.ait.weatherreportapp.Touch.CityTouchHelperCallback
import kotlinx.android.synthetic.main.city_row.*
import kotlinx.android.synthetic.main.city_row.view.*
import kotlinx.android.synthetic.main.city_row.view.btnCity
import java.util.*

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>, CityTouchHelperCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cityRow = LayoutInflater.from(context).inflate(
            R.layout.city_row, parent, false
        )
        return ViewHolder(cityRow)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var city = cityList.get(holder.adapterPosition)

        holder.tvName.text = city.itemName

        holder.btnDelete.setOnClickListener{
            deleteCity(holder.adapterPosition)
        }

        holder.tvName.setOnClickListener {
            // open Details Activity
            var intentSummary = Intent()
            intentSummary.setClass(context as ScrollingActivity,
                DetailsActivity::class.java)

            intentSummary.putExtra("CITY", holder.tvName.text.toString())

            context.startActivity(intentSummary)
        }
    }

    var cityList = mutableListOf<City>()

    val context: Context
    constructor(context: Context, listCities: List<City>){
        this.context = context

        cityList.addAll(listCities)

        //for(i: Int in 0..20){
        //    cityList.add(City(2019,"$ $i", "City $i", false))
        //}
    }

    fun updateCity(city: City){
        Thread{
            AppDatabase.getInstance(context).cityDao().updateCity(city)
        }.start()
    }

    fun updateCityOnPosition(city: City, index: Int){
        cityList.set(index, city)
        notifyItemChanged(index)
    }

    fun deleteCity(index: Int){
        Thread{
            AppDatabase.getInstance(context).cityDao().deleteCity(cityList[index])
            (context as ScrollingActivity).runOnUiThread {
                cityList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun addCity(city: City){
        cityList.add(city)
        notifyItemInserted(cityList.lastIndex)
    }

    override fun onDismissed(position: Int) {
        deleteCity(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(cityList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName = itemView.btnCity
        val btnDelete = itemView.btnDelete
    }

}