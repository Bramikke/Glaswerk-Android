package com.bramgoedvriend.glaswerk.stock

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.data.AppDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.data.Item
import com.bramgoedvriend.glaswerk.data.Lokaal
import com.bramgoedvriend.glaswerk.repository.ItemsRepository
import com.bramgoedvriend.glaswerk.repository.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StockViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _navigateToDetail = MutableLiveData<Item>()
    val navigateToDetail: LiveData<Item>
        get() = _navigateToDetail

    private val database = AppDatabase.getInstance(application)
    private val itemsRepository = ItemsRepository.getInstance(database.itemDao)
    private val roomRepository = RoomRepository.getInstance(database.lokaalDao)

    private val prefs =  application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    init {
        coroutineScope.launch {
            try {
                itemsRepository.fullRefresh()
                roomRepository.refresh()
            } catch (t:Throwable) {
                _status.value = ApiStatus.OFFLINE
            }
        }
    }

    var lokaal = roomRepository.getRoom(prefs.getInt("room", 1))
    var items = itemsRepository.itemsByRoom(prefs.getInt("room", 1))

    fun updateRoom() {
        try {
            lokaal = roomRepository.getRoom(prefs.getInt("room", 1))
            items = itemsRepository.itemsByRoom(prefs.getInt("room", 1))
        } catch (t: Throwable) {
            lokaal = MutableLiveData(Lokaal(-1, "geen lokaal"))
            items = MutableLiveData(null)
        }
    }

    fun onItemClicked(item: Item) {
        _navigateToDetail.value = item
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}