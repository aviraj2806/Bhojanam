package com.mp2.bhojanam.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MealEntity::class],version = 2)
abstract class MealDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao
}