package com.bramgoedvriend.glaswerk.stock.stockDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class StockDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockDetailViewModel::class.java)) {
            return StockDetailViewModel(application) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}