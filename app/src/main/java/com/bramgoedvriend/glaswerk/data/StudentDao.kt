package com.bramgoedvriend.glaswerk.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM student, studentitem")
    fun getStudents(): LiveData<List<StudentAndStudentItem>>

    @Query("select * from student where klasid = :classid order by voornaam")
    fun getStudentsByClass(classid: Int): LiveData<List<StudentAndStudentItem>>

    @Query("select * from studentitem")
    fun getStudentItem(): LiveData<List<StudentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudentItems(vararg studentItem: StudentItem)

    @Query("DELETE FROM studentitem")
    fun dropStudentItems()

    @Query("DELETE FROM student")
    fun dropStudents()
}