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

@Suppress("UNCHECKED_CAST")
class DialogViewModel<T> (application: Application, private val type: Class<T>) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _items = MutableLiveData<List<T>>()
    val items: LiveData<List<T>>
        get() = _items

    init {
        getItems()
    }

    private fun getItems() {
        val result: Observable<List<T>>
        if(type == Lokaal::class.java) {
            result = RetrofitClient.instance.getRoom() as Observable<List<T>>
        } else {
            result = RetrofitClient.instance.getClass() as Observable<List<T>>
        }
        result.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _status.value = ApiStatus.LOADING }
            .doOnTerminate { _status.value = ApiStatus.DONE }
            .doOnError { _status.value = ApiStatus.ERROR }
            .subscribe {
                    res -> _items.value = res
            }

    }
}