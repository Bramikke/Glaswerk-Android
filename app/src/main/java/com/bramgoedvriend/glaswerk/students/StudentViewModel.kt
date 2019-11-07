package com.bramgoedvriend.glaswerk.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>>
        get() = _students

    init {
        var result = RetrofitClient.instance.getStudentsByClass(1)
        result.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _status.value = ApiStatus.LOADING }
            .doOnTerminate { _status.value = ApiStatus.DONE }
            .doOnError { _status.value = ApiStatus.ERROR }
            .subscribe { res -> _students.value = res }
    }
}