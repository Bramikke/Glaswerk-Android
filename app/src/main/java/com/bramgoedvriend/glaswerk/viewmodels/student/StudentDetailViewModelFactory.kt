package com.bramgoedvriend.glaswerk.viewmodels.student

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class StudentDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentDetailViewModel::class.java)) {
            return StudentDetailViewModel(application) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}
