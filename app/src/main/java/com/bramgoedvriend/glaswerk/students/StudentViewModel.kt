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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>>
        get() = _students

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _status.value = ApiStatus.LOADING
        coroutineScope.launch {
            try {
                val result = RetrofitClient.instance.getStudentsByClassAsync(1).await()
                _students.value = result
                _status.value = ApiStatus.DONE
            } catch (t:Throwable) {
                _status.value = ApiStatus.ERROR
            }
        }
    }
}