package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import com.bramgoedvriend.glaswerk.data.AppDatabase
import com.bramgoedvriend.glaswerk.data.Klas
import com.bramgoedvriend.glaswerk.data.KlasDao
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClassRepository private constructor(private val klasDao: KlasDao): Repository<Klas> {
    override val data: LiveData<List<Klas>> = klasDao.getClasses()

    fun getClass(classid: Int): LiveData<Klas> = klasDao.getClass(classid)

    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val classes = RetrofitClient.instance.getClassAsync().await()
            klasDao.insertAll(*classes.asDatabaseModel())
        }
    }

    companion object {
        @Volatile private var instance: ClassRepository? = null

        fun getInstance(klasDao: KlasDao) =
            instance ?: synchronized(this) {
                instance ?: ClassRepository(klasDao).also { instance = it }
            }
    }
}