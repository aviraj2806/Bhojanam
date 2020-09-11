package com.mp2.bhojanam.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class StudentEntity (
    @PrimaryKey val timestamp: String,
    @ColumnInfo(name = "incharge_mobile")val mobile: String,
    @ColumnInfo(name = "student_name")val name:String,
    @ColumnInfo(name = "student_school") val school:String,
    @ColumnInfo(name = "student_pic") val image:String,
    @ColumnInfo(name = "student_class") val stdClass:String
    )