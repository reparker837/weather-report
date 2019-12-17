package com.ait.weatherreportapp.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "city")
data class City(
    @PrimaryKey(autoGenerate = true) var cityId: Long?,
    @ColumnInfo(name = "itemName") var itemName: String,
    @ColumnInfo(name = "done") var done: Boolean
) : Serializable