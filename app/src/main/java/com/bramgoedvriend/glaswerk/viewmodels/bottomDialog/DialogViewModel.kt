package com.bramgoedvriend.glaswerk.viewmodels.bottomDialog

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.database.AppDatabase
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.ApiStatus
import com.bramgoedvriend.glaswerk.network.GlaswerkConnectivityManager
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.RoomClassName
import com.bramgoedvriend.glaswerk.repository.ClassRepository
import com.bramgoedvriend.glaswerk.repository.Repository
import com.bramgoedvriend.glaswerk.repository.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class DialogViewModel<T> (
    application: Application,
    private val type: Class<T>
) : AndroidViewModel(application) {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val manager = GlaswerkConnectivityManager.getInstance(application)
    private val database = AppDatabase.getInstance(application)
    private val repository: Repository <T>
    private val sharedPreferences = application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    val items: LiveData<List<T>>

    init {
        if (type == Lokaal::class.java) {
            repository = RoomRepository.getInstance(database.lokaalDao, manager) as Repository<T>
        } else {
            repository = ClassRepository.getInstance(database.klasDao, manager) as Repository<T>
        }

        coroutineScope.launch {
            try {
                repository.refresh()
            } catch (t: Throwable) {}
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
            if (type == Lokaal::class.java) {
                RetrofitClient.instance.postAddRoom(RoomClassName(name)).await()
            } else {
                RetrofitClient.instance.postAddClass(RoomClassName(name)).await()
            }
        }
    }
}
