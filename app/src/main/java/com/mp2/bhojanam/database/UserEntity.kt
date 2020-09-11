package com.mp2.bhojanam.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey val mobile: String,
    @ColumnInfo(name = "user_time") val timestamp:String,
    @ColumnInfo(name = "user_type") val type: String,
    @ColumnInfo(name = "user_name") val name: String,
    @ColumnInfo(name = "user_pass") val pass: String,
    @ColumnInfo(name = "user_school") val school:String
)