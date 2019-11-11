package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.bramgoedvriend.glaswerk.database.Database
import com.bramgoedvriend.glaswerk.database.asDomainModel
import com.bramgoedvriend.glaswerk.database.asDomainModelItemRoom
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemsRepository(private val database: Database):Repository<Item> {

    override val data: LiveData<List<Item>> = Transformations.map(database.itemDao.getItems()) {
        it.asDomainModel()
    }

    fun itemsByRoom(roomid: Int):LiveData<List<Item>> = Transformations.map(database.itemDao.getItemsByRoom(roomid)) {
        it.asDomainModel()
    }

    val itemOrders: LiveData<List<Item>> = Transformations.map(database.itemDao.getItemOrders()) {
        it.asDomainModelItemRoom()
    }

    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val items = RetrofitClient.instance.getItemsAsync().await()
            database.itemDao.insertAll(*items.asDatabaseModel())
        }
    }

    suspend fun fullRefresh() {
        withContext(Dispatchers.IO) {
            val items = RetrofitClient.instance.getItemsAsync().await()
            database.itemDao.dropItems()
            database.itemDao.insertAll(*items.asDatabaseModel())
        }
    }
}