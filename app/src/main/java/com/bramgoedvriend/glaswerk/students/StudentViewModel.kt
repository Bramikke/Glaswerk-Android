package com.bramgoedvriend.glaswerk.orders

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.database.getDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.repository.ClassRepository
import com.bramgoedvriend.glaswerk.repository.StudentRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _navigateToDetail = MutableLiveData<Student>()
    val navigateToDetail: LiveData<Student>
        get() = _navigateToDetail

    private val database = getDatabase(application)
    private val studentsRepository = StudentRepository(database)
    private val classRepository = ClassRepository(database)

    private val prefs =  application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    init {
        coroutineScope.launch {
            try {
                studentsRepository.fullRefresh()
                classRepository.refresh()
            } catch (t:Throwable) {
                _status.value = ApiStatus.OFFLINE
            }
        }
    }

    var klas = classRepository.getClass(prefs.getInt("class", 1))
    var students = studentsRepository.studentsByClass(prefs.getInt("class", 1))

    fun updateClass() {
        klas = classRepository.getClass(prefs.getInt("class", 1))
        students = studentsRepository.studentsByClass(prefs.getInt("class", 1))
    }

    fun onStudentClicked(student: Student) {
        _navigateToDetail.value = student
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}