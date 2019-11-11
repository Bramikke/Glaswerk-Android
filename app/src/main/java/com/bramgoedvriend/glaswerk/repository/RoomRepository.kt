package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.bramgoedvriend.glaswerk.database.Database
import com.bramgoedvriend.glaswerk.database.asDomainModel
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomRepository(private val database: Database): Repository <Lokaal> {
    override val data: LiveData<List<Lokaal>> = Transformations.map(database.roomDao.getRooms()) {
        it.asDomainModel()
    }

    fun getRoom(roomid: Int): LiveData<Lokaal> = database.roomDao.getRoom(roomid)

    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val rooms = RetrofitClient.instance.getRoomAsync().await()
            database.roomDao.insertAll(*rooms.asDatabaseModel())
        }
    }
}