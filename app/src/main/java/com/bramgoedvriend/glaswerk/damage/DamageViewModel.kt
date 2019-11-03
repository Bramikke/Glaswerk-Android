package com.bramgoedvriend.glaswerk.damage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private var _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    init {
        getItems()
    }

    private fun getItems() {
        var result = RetrofitClient.instance.getItems()
        result.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                    res ->  _items.value = res
            }
    }
}