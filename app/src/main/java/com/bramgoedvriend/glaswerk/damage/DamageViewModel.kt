package com.bramgoedvriend.glaswerk.damage

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.database.getDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.DamageItemNavigate
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.repository.ClassRepository
import com.bramgoedvriend.glaswerk.repository.ItemsRepository
import com.bramgoedvriend.glaswerk.repository.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DamageViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _navigateToLeerlingen = MutableLiveData<DamageItemNavigate>()
    val navigateToLeerlingen: LiveData<DamageItemNavigate>
        get() = _navigateToLeerlingen

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val itemsRepository = ItemsRepository(database)
    private val roomRepository = RoomRepository(database)
    private val classRepository = ClassRepository(database)

    private val prefs =  application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    init {
        coroutineScope.launch {
            itemsRepository.refresh()
            roomRepository.refresh()
            classRepository.refresh()
        }
    }

    var lokaal = roomRepository.getRoom(prefs.getInt("room", 1))
    var items = itemsRepository.itemsByRoom(prefs.getInt("room", 1))
    var klas = classRepository.getClass(prefs.getInt("class", 1))

    fun updateRoom() {
            try {
                lokaal = roomRepository.getRoom(prefs.getInt("room", 1))
                items = itemsRepository.itemsByRoom(prefs.getInt("room", 1))
            } catch (t: Throwable) {
                lokaal = MutableLiveData(Lokaal(-1, "geen lokaal"))
                items = MutableLiveData(null)
            }
    }

    fun updateClass() {
            try {
                klas = classRepository.getClass(prefs.getInt("class", 1))
            } catch (t: Throwable) {
                klas = MutableLiveData(Klas(-1, "geen klas"))
            }
    }

    fun onItemClicked(item: Item) {
        _navigateToLeerlingen.value = DamageItemNavigate(
            item.id,
            item.name,
            item.amount
        )
    }

    fun onLeerlingenNavigated() {
        _navigateToLeerlingen.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}