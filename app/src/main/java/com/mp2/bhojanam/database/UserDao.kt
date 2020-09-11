package com.mp2.bhojanam.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM user where mobile = :mobile")
    fun getUserByMobile(mobile: String): UserEntity

    @Query("UPDATE user SET user_school = :school where mobile = :mobile")
    fun insertSchool(school:String,mobile: String)

    @Query("SELECT * FROM user where user_type = :type")
    fun getUsersByType(type:String): List<UserEntity>

}