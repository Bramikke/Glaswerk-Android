package com.bramgoedvriend.glaswerk.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Transaction
import com.bramgoedvriend.glaswerk.data.AppDatabase
import com.bramgoedvriend.glaswerk.data.Item
import com.bramgoedvriend.glaswerk.data.ItemAndLokaal
import com.bramgoedvriend.glaswerk.data.ItemDao
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemsRepository private constructor(private val itemDao: ItemDao):Repository<Item> {

    override val data: LiveData<List<Item>> = itemDao.getItems()

    val itemOrders: LiveData<List<ItemAndLokaal>> = itemDao.getItemOrders()

    fun itemsByRoom(roomid: Int):LiveData<List<Item>> = itemDao.getItemsByRoom(roomid)


    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val items = RetrofitClient.instance.getItemsAsync().await()
            itemDao.insertAll(*items.asDatabaseModel())
        }
    }

    @Transaction //TODO add more transactions
    suspend fun fullRefresh() {
        withContext(Dispatchers.IO) {
            val items = RetrofitClient.instance.getItemsAsync().await()
            itemDao.dropItems()
            itemDao.insertAll(*items.asDatabaseModel())
        }
    }

    companion object {
        @Volatile private var instance: ItemsRepository? = null

        fun getInstance(itemDao: ItemDao) =
            instance ?: synchronized(this) {
                instance ?: ItemsRepository(itemDao).also { instance = it }
            }
    }
}