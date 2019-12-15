package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import com.bramgoedvriend.glaswerk.database.DAOs.StudentDao
import com.bramgoedvriend.glaswerk.domain.StudentAndStudentItem
import com.bramgoedvriend.glaswerk.network.DTO.asDatabaseModel
import com.bramgoedvriend.glaswerk.network.GlaswerkConnectivityManager
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StudentRepository private constructor(
    private val studentDao: StudentDao,
    private val manager: GlaswerkConnectivityManager
) : Repository<StudentAndStudentItem> {
    override val data: LiveData<List<StudentAndStudentItem>> = studentDao.getStudents()

    fun studentsByClass(classid: Int): LiveData<List<StudentAndStudentItem>> = studentDao.getStudentsByClass(classid)

    override suspend fun refresh(): Boolean {
        if (manager.hasInternet()) {
            withContext(Dispatchers.IO) {
                val students = RetrofitClient.instance.getStudentsAsync().await()
                val studentItems = RetrofitClient.instance.getStudentItemAsync().await()
                studentDao.delete()
                studentDao.insertAll(*students.asDatabaseModel())
                studentDao.deleteStudentItems()
                studentDao.insertStudentItems(*studentItems.asDatabaseModel())
            }
            return true
        }
        return false
    }

    companion object {
        @Volatile private var instance: StudentRepository? = null
        fun getInstance(studentDao: StudentDao, manager: GlaswerkConnectivityManager) =
            instance ?: synchronized(this) {
                instance ?: StudentRepository(studentDao, manager).also { instance = it }
            }
    }
}
