package com.bramgoedvriend.glaswerk.viewmodels.damage

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.database.AppDatabase
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.ApiStatus
import com.bramgoedvriend.glaswerk.network.DamageItemNavigate
import com.bramgoedvriend.glaswerk.network.GlaswerkConnectivityManager
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

    private val manager = GlaswerkConnectivityManager.getInstance(application)
    private val database = AppDatabase.getInstance(application)
    private val itemsRepository = ItemsRepository.getInstance(database.itemDao, manager)
    private val roomRepository = RoomRepository.getInstance(database.lokaalDao, manager)
    private val classRepository = ClassRepository.getInstance(database.klasDao, manager)

    private val prefs = application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    init {
        coroutineScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                if (!itemsRepository.refresh() ||
                    !roomRepository.refresh() ||
                    !classRepository.refresh()) {
                    _status.value = ApiStatus.OFFLINE
                } else {
                    _status.value = ApiStatus.DONE
                }
            } catch (t: Throwable) {
                _status.value = ApiStatus.ERROR
            }
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
                lokaal = MutableLiveData(
                    Lokaal(
                        -1,
                        "geen lokaal"
                    )
                )
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
            item.naam,
            item.aantal
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
