package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import com.bramgoedvriend.glaswerk.database.DAOs.LokaalDao
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.DTO.asDatabaseModel
import com.bramgoedvriend.glaswerk.network.GlaswerkConnectivityManager
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomRepository private constructor(
    private val lokaalDao: LokaalDao,
    private val manager: GlaswerkConnectivityManager
) : Repository <Lokaal> {
    override val data: LiveData<List<Lokaal>> = lokaalDao.getRooms()

    fun getRoom(roomid: Int): LiveData<Lokaal> = lokaalDao.getRoom(roomid)

    override suspend fun refresh(): Boolean {
        if (manager.hasInternet()) {
            withContext(Dispatchers.IO) {
                val rooms = RetrofitClient.instance.getRoomAsync().await()
                lokaalDao.delete()
                lokaalDao.insertAll(*rooms.asDatabaseModel())
            }
            return true
        }
        return false
    }

    companion object {
        @Volatile private var instance: RoomRepository? = null
        fun getInstance(lokaalDao: LokaalDao, manager: GlaswerkConnectivityManager) =
            instance ?: synchronized(this) {
                instance ?: RoomRepository(lokaalDao, manager).also { instance = it }
            }
    }
}
