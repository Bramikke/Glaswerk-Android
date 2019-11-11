package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.bramgoedvriend.glaswerk.database.Database
import com.bramgoedvriend.glaswerk.database.asDomainModel
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClassRepository(private val database: Database) : Repository<Klas> {
    override val data: LiveData<List<Klas>> = Transformations.map(database.classDao.getClasses()) {
        it.asDomainModel()
    }

    fun getClass(classid: Int): LiveData<Klas> = database.classDao.getClass(classid)

    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val classes = RetrofitClient.instance.getClassAsync().await()
            database.classDao.insertAll(*classes.asDatabaseModel())
        }
    }
}