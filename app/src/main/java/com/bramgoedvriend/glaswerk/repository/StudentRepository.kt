package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.bramgoedvriend.glaswerk.database.Database
import com.bramgoedvriend.glaswerk.database.asDomainModel
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.domain.StudentItem
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StudentRepository(private val database: Database):Repository<Student> {
    override val data: LiveData<List<Student>> = database.studentDao.getStudents()

    fun studentsByClassByItem(itemid: Int, classid: Int) : LiveData<List<Student>> = database.studentDao.getStudentsByClassByItem(itemid, classid)

    fun studentsByClass(classid: Int): LiveData<List<Student>> = database.studentDao.getStudentsByClass(classid)

    override suspend fun refresh()  {
        withContext(Dispatchers.IO) {
            val students = RetrofitClient.instance.getStudentsAsync().await()
            database.studentDao.insertAll(*students.asDatabaseModel())
            val studentItems = RetrofitClient.instance.getStudentItemAsync().await()
            database.studentDao.dropStudentItems()
            database.studentDao.insertStudentItems(*studentItems.asDatabaseModel())
        }
    }

    suspend fun fullRefresh() {
        withContext(Dispatchers.IO) {
            val students = RetrofitClient.instance.getStudentsAsync().await()
            database.studentDao.dropStudents()
            database.studentDao.insertAll(*students.asDatabaseModel())
        }
    }
}