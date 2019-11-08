package com.bramgoedvriend.glaswerk.damage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.network.GlaswerkAPIService
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DamageViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    private val _navigateToLeerlingen = MutableLiveData<Item>()
    val navigateToLeerlingen
        get() = _navigateToLeerlingen

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getItems()
    }

    private fun getItems() {
        _status.value = ApiStatus.LOADING
        coroutineScope.launch {
            try {
                val result = RetrofitClient.instance.getItemsAsync().await()
                _items.value = result
                _status.value = ApiStatus.DONE
            } catch (t:Throwable) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun onItemClicked(item: Item) {
        _navigateToLeerlingen.value = item
    }

    fun onLeerlingenNavigated() {
        _navigateToLeerlingen.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}