package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import com.bramgoedvriend.glaswerk.database.DAOs.KlasDao
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.network.DTO.asDatabaseModel
import com.bramgoedvriend.glaswerk.network.GlaswerkConnectivityManager
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClassRepository private constructor(
    private val klasDao: KlasDao,
    private val manager: GlaswerkConnectivityManager
) : Repository<Klas> {
    override val data: LiveData<List<Klas>> = klasDao.getClasses()

    fun getClass(classid: Int): LiveData<Klas> = klasDao.getClass(classid)

    override suspend fun refresh(): Boolean {
        if (manager.hasInternet()) {
            withContext(Dispatchers.IO) {
                val classes = RetrofitClient.instance.getClassAsync().await()
                klasDao.insertAll(*classes.asDatabaseModel())
            }
            return true
        }
        return false
    }

    companion object {
        @Volatile private var instance: ClassRepository? = null

        fun getInstance(klasDao: KlasDao, manager: GlaswerkConnectivityManager) =
            instance ?: synchronized(this) {
                instance ?: ClassRepository(klasDao, manager).also { instance = it }
            }
    }
}
