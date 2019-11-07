package com.bramgoedvriend.glaswerk.bottomDialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DialogViewModelFactory <E> (private val application: Application, private val type: Class<E>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DialogViewModel::class.java)) {
            return DialogViewModel <E> (application, type) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}