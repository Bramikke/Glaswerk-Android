package com.bramgoedvriend.glaswerk.damage.damage_student

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DamageStudentViewModelFactory(private val application: Application, private  val args: DamageStudentFragmentArgs) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DamageStudentViewModel::class.java)) {
            return DamageStudentViewModel(application, args) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}