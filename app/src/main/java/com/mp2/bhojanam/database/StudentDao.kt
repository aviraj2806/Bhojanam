package com.mp2.bhojanam.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {

    @Insert
    fun registerStudent(studentEntity: StudentEntity)

    @Query("SELECT * FROM student where incharge_mobile = :mobile")
    fun getStudentByIncharge(mobile:String) : List<StudentEntity>

    @Query("SELECT * FROM student")
    fun getAllStudents() : List<StudentEntity>

}