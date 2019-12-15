package com.bramgoedvriend.glaswerk.database.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Transaction
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.domain.StudentAndStudentItem
import com.bramgoedvriend.glaswerk.domain.StudentItem

@Dao
interface StudentDao {
    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM student")
    fun getStudents(): LiveData<List<StudentAndStudentItem>>

    @Transaction
    @Query("select * from student where klasid = :classid order by voornaam")
    fun getStudentsByClass(classid: Int): LiveData<List<StudentAndStudentItem>>

    @Query("select * from studentitem")
    fun getStudentItem(): LiveData<List<StudentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudentItems(vararg studentItem: StudentItem)

    @Query("DELETE FROM studentitem")
    fun deleteStudentItems()

    @Query("DELETE FROM student")
    fun delete()
}
