package com.mp2.bhojanam.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class MealEntity (
    @PrimaryKey val timestamp: String,
    @ColumnInfo (name = "meal_date") val date:String,
    @ColumnInfo(name = "meal_served_to") val name:String,
    @ColumnInfo(name = "meal_served_in") val school:String,
    @ColumnInfo(name = "meal_served_by") val mobile:String
)