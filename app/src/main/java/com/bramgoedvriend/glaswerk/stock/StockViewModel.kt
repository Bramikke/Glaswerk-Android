package com.bramgoedvriend.glaswerk.stock

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.database.getDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.repository.ItemsRepository
import com.bramgoedvriend.glaswerk.repository.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StockViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val itemsRepository = ItemsRepository(database)
    private val roomRepository = RoomRepository(database)

    private val prefs =  application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    init {
        coroutineScope.launch {
            itemsRepository.refresh()
            roomRepository.refresh()
        }
    }

    var items = itemsRepository.itemsByRoom(prefs.getInt("room", 1))
    var lokaal = roomRepository.getRoom(prefs.getInt("room", 1))

    fun updateRoom() {
        try {
            lokaal = roomRepository.getRoom(prefs.getInt("room", 1))
            items = itemsRepository.itemsByRoom(prefs.getInt("room", 1))
        } catch (t: Throwable) {
            lokaal = MutableLiveData(Lokaal(-1, "geen lokaal"))
            items = MutableLiveData(null)
        }
    }
}