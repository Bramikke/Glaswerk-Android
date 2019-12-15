package com.bramgoedvriend.glaswerk.viewmodels.damage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bramgoedvriend.glaswerk.network.DamageItemNavigate

@Suppress("UNCHECKED_CAST")
class DamageStudentViewModelFactory(private val application: Application, private val item: DamageItemNavigate) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DamageStudentViewModel::class.java)) {
            return DamageStudentViewModel(
                application,
                item
            ) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}
