package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.bramgoedvriend.glaswerk.data.AppDatabase
import com.bramgoedvriend.glaswerk.data.Student
import com.bramgoedvriend.glaswerk.data.StudentAndStudentItem
import com.bramgoedvriend.glaswerk.data.StudentDao
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StudentRepository private constructor(private val studentDao: StudentDao):Repository<StudentAndStudentItem> {
    override val data: LiveData<List<StudentAndStudentItem>> = studentDao.getStudents()

    fun studentsByClass(classid: Int) : LiveData<List<StudentAndStudentItem>> = studentDao.getStudentsByClass(classid)

    @Transaction
    override suspend fun refresh()  {
        withContext(Dispatchers.IO) {
            val students = RetrofitClient.instance.getStudentsAsync().await()
            studentDao.insertAll(*students.asDatabaseModel())
            val studentItems = RetrofitClient.instance.getStudentItemAsync().await()
            studentDao.dropStudentItems()
            studentDao.insertStudentItems(*studentItems.asDatabaseModel())
        }
    }

    @Transaction
    suspend fun fullRefresh() {
        withContext(Dispatchers.IO) {
            val students = RetrofitClient.instance.getStudentsAsync().await()
            studentDao.dropStudents()
            studentDao.insertAll(*students.asDatabaseModel())
            val studentItems = RetrofitClient.instance.getStudentItemAsync().await()
            studentDao.dropStudentItems()
            studentDao.insertStudentItems(*studentItems.asDatabaseModel())
        }
    }

    companion object {
        @Volatile private var instance: StudentRepository? = null

        fun getInstance(studentDao: StudentDao) =
            instance ?: synchronized(this) {
                instance ?: StudentRepository(studentDao).also { instance = it }
            }
    }
}