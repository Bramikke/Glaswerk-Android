package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.bramgoedvriend.glaswerk.data.AppDatabase
import com.bramgoedvriend.glaswerk.data.Lokaal
import com.bramgoedvriend.glaswerk.data.LokaalDao
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomRepository private constructor(private val lokaalDao: LokaalDao): Repository <Lokaal> {
    override val data: LiveData<List<Lokaal>> = lokaalDao.getRooms()

    fun getRoom(roomid: Int): LiveData<Lokaal> = lokaalDao.getRoom(roomid)

    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val rooms = RetrofitClient.instance.getRoomAsync().await()
            lokaalDao.insertAll(*rooms.asDatabaseModel())
        }
    }

    companion object {
        @Volatile private var instance: RoomRepository? = null

        fun getInstance(lokaalDao: LokaalDao) =
            instance ?: synchronized(this) {
                instance ?: RoomRepository(lokaalDao).also { instance = it }
            }
    }
}