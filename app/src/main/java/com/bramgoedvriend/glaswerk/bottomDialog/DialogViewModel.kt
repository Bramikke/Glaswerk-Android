package com.bramgoedvriend.glaswerk.bottomDialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

@Suppress("UNCHECKED_CAST")
class DialogViewModel<T> (application: Application, private val type: Class<T>) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _items = MutableLiveData<List<T>>()
    val items: LiveData<List<T>>
        get() = _items

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getItems()
    }

    private fun getItems() {
        _status.value = ApiStatus.LOADING
        coroutineScope.launch {
            try {
                val result: List<T>
                if(type == Lokaal::class.java) {
                    result = RetrofitClient.instance.getRoomAsync().await() as List<T>
                } else {
                    result = RetrofitClient.instance.getClassAsync().await() as List<T>
                }
                _items.value = result
                _status.value = ApiStatus.DONE
            } catch (t:Throwable) {
                _status.value = ApiStatus.ERROR
            }
        }
    }
}