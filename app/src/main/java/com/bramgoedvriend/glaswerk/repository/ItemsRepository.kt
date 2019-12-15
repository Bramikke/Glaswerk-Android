package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData
import com.bramgoedvriend.glaswerk.database.DAOs.ItemDao
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.ItemAndLokaal
import com.bramgoedvriend.glaswerk.network.DTO.asDatabaseModel
import com.bramgoedvriend.glaswerk.network.GlaswerkConnectivityManager
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemsRepository private constructor(
    private val itemDao: ItemDao,
    private val manager: GlaswerkConnectivityManager
) : Repository<Item> {

    override val data: LiveData<List<Item>> = itemDao.getItems()

    val itemOrders: LiveData<List<ItemAndLokaal>> = itemDao.getItemOrders()

    fun itemsByRoom(roomid: Int): LiveData<List<Item>> = itemDao.getItemsByRoom(roomid)

    override suspend fun refresh(): Boolean {
        if (manager.hasInternet()) {
            withContext(Dispatchers.IO) {
                val items = RetrofitClient.instance.getItemsAsync().await()
                itemDao.delete()
                itemDao.insertAll(*items.asDatabaseModel())
            }
            return true
        }
        return false
    }

    companion object {
        @Volatile private var instance: ItemsRepository? = null

        fun getInstance(itemDao: ItemDao, manager: GlaswerkConnectivityManager) =
            instance ?: synchronized(this) {
                instance ?: ItemsRepository(itemDao, manager).also { instance = it }
            }
    }
}
