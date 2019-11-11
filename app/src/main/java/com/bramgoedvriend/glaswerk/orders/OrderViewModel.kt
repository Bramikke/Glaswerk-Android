package com.bramgoedvriend.glaswerk.orders

import android.app.Application
import android.text.Editable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.database.getDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.network.GlaswerkAPIService
import com.bramgoedvriend.glaswerk.network.OrderItem
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.repository.ItemsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    fun order(item: Item, amount: String) {
        coroutineScope.launch {
            RetrofitClient.instance.postOrderItemAsync(OrderItem(
                item.id,
                item.amount + amount.toInt()
            )).await()
            itemsRepository.refresh()
        }
    }

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val itemsRepository = ItemsRepository(database)

    init {
        coroutineScope.launch {
            itemsRepository.refresh()
        }
    }

    val items = itemsRepository.itemOrders
}