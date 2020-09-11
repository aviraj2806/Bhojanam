package com.mp2.bhojanam.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MealDao {

    @Insert
    fun insertMeal(mealEntity: MealEntity)

    @Query("SELECT * FROM meal where meal_served_by = :mobile")
    fun getMealsByIncharge(mobile:String): List<MealEntity>

    @Query("SELECT * FROM meal")
    fun getAllMeals(): List<MealEntity>

    @Query("SELECT * FROM meal where meal_served_by = :mobile AND meal_date = :date")
    fun getMealsByInchargeAndDate(mobile: String,date:String): List<MealEntity>

    @Query("SELECT * FROM meal where meal_date = :date")
    fun getMealsByDate(date: String) :List<MealEntity>
}