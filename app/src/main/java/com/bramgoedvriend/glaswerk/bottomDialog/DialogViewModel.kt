package com.bramgoedvriend.glaswerk.bottomDialog

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.database.getDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.repository.ClassRepository
import com.bramgoedvriend.glaswerk.repository.ItemsRepository
import com.bramgoedvriend.glaswerk.repository.Repository
import com.bramgoedvriend.glaswerk.repository.RoomRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    private val database = getDatabase(application)
    private val repository: Repository <T>
    private val sharedPreferences =  application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    val items : LiveData<List<T>>

    init {
        if (type == Lokaal::class.java) {
            repository = RoomRepository(database) as Repository<T>
        } else {
            repository = ClassRepository(database) as Repository<T>
        }
        coroutineScope.launch {
            repository.refresh()
        }
        items = repository.data
    }

    fun onItemClicked(item: Lokaal) {
        Log.i("loglog","lokaalClick: "+item.roomId)
        sharedPreferences.edit().putInt("room", item.roomId).apply()
    }

    fun onItemClicked(item: Klas) {
        sharedPreferences.edit().putInt("class", item.classId).apply()
    }
}