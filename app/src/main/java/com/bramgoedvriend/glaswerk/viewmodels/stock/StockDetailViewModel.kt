package com.bramgoedvriend.glaswerk.viewmodels.stock

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.bramgoedvriend.glaswerk.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StockDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val manager = GlaswerkConnectivityManager.getInstance(application)
    val offline = !manager.hasInternet()

    private val prefs = application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    fun addItem(item: ItemNavigate?, name: String, amount: Int, minAmount: Int, maxAmount: Int, orderAmount: Int) {
        coroutineScope.launch {
            if (item != null) {
                val postItem = Item(item.id, prefs.getInt("room", 1), name, amount, minAmount, maxAmount, orderAmount)
                RetrofitClient.instance.postEditItem(postItem).await()
            } else {
                val postItem = Item(null, prefs.getInt("room", 1), name, amount, minAmount, maxAmount, orderAmount)
                RetrofitClient.instance.postAddItem(postItem).await()
            }
        }
    }

    fun remove(item: ItemNavigate?) {
        coroutineScope.launch {
            if (item != null) {
                val postItem = ItemId(item.id)
                RetrofitClient.instance.postRemoveItem(postItem).await()
            }
        }
    }
}
