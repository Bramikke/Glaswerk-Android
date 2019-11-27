package com.bramgoedvriend.glaswerk.bottomDialog

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.data.AppDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.data.Klas
import com.bramgoedvriend.glaswerk.data.Lokaal
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.RoomClassName
import com.bramgoedvriend.glaswerk.repository.ClassRepository
import com.bramgoedvriend.glaswerk.repository.Repository
import com.bramgoedvriend.glaswerk.repository.RoomRepository
import kotlinx.coroutines.*

@Suppress("UNCHECKED_CAST")
class DialogViewModel<T> (
    application: Application,
    private val type: Class<T>) : AndroidViewModel(application) {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = AppDatabase.getInstance(application)
    private val repository: Repository <T>
    private val sharedPreferences =  application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    val items : LiveData<List<T>>

    init {
        if (type == Lokaal::class.java) {
            repository = RoomRepository.getInstance(database.lokaalDao) as Repository<T>
        } else {
            repository = ClassRepository.getInstance(database.klasDao) as Repository<T>
        }
        coroutineScope.launch {
            try {
                repository.refresh()
            } catch (t:Throwable) {}
        }
        items = repository.data
    }

    fun onItemClicked(item: Lokaal) {
        sharedPreferences.edit().putInt("room", item.lokaalId).apply()
    }

    fun onItemClicked(item: Klas) {
        sharedPreferences.edit().putInt("class", item.klasId).apply()
    }

    fun add(name: String) {
        coroutineScope.launch {
            if(type == Lokaal::class.java) {
                RetrofitClient.instance.postAddRoom(RoomClassName(name)).await()
            } else {
                RetrofitClient.instance.postAddClass(RoomClassName(name)).await()
            }
        }
    }
}