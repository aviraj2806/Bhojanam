package com.mp2.bhojanam.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StudentEntity::class],version = 1)
abstract class StudentDatabse: RoomDatabase() {
    abstract fun studentDao(): StudentDao
}