package com.bramgoedvriend.glaswerk.viewmodels.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.database.AppDatabase
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.network.ApiStatus
import com.bramgoedvriend.glaswerk.network.GlaswerkConnectivityManager
import com.bramgoedvriend.glaswerk.network.OrderItem
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.repository.ItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val manager = GlaswerkConnectivityManager.getInstance(application)
    private val database = AppDatabase.getInstance(application)
    private val itemsRepository = ItemsRepository.getInstance(database.itemDao, manager)

    init {
        coroutineScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                if (!itemsRepository.refresh()) {
                    _status.value = ApiStatus.OFFLINE
                }
                _status.value = ApiStatus.DONE
            } catch (t: Throwable) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    val items = itemsRepository.itemOrders

    fun order(item: Item, amount: String) {
        coroutineScope.launch {
            RetrofitClient.instance.postOrderItemAsync(
                OrderItem(
                    item.id,
                    item.aantal + amount.toInt()
                )
            ).await()
            itemsRepository.refresh()
        }
    }
}
