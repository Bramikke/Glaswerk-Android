package com.bramgoedvriend.glaswerk.orders

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.data.AppDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.data.Student
import com.bramgoedvriend.glaswerk.repository.ClassRepository
import com.bramgoedvriend.glaswerk.repository.StudentRepository
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

    private val database = AppDatabase.getInstance(application)
    private val studentsRepository = StudentRepository.getInstance(database.studentDao)
    private val classRepository = ClassRepository.getInstance(database.klasDao)

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