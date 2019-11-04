package com.bramgoedvriend.glaswerk.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private var _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>>
        get() = _students

    init {
        var result = RetrofitClient.instance.getStudentsByClass(1)
        result.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res ->  _students.value = res }
    }
}