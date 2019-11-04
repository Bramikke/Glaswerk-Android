package com.bramgoedvriend.glaswerk.damage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DamageViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DamageViewModel::class.java)) {
            return DamageViewModel(application) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}