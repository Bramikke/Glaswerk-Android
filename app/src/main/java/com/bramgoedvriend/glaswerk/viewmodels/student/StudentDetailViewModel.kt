package com.bramgoedvriend.glaswerk.viewmodels.student

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.bramgoedvriend.glaswerk.network.GlaswerkConnectivityManager
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.Student
import com.bramgoedvriend.glaswerk.network.StudentId
import com.bramgoedvriend.glaswerk.network.StudentNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StudentDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val manager = GlaswerkConnectivityManager.getInstance(application)
    val offline = !manager.hasInternet()

    private val prefs = application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    fun addStudent(student: StudentNavigate?, firstName: String, lastName: String) {
        coroutineScope.launch {
            if (student != null) {
                val postItem = Student(student.studentId, prefs.getInt("class", 1), firstName, lastName)
                RetrofitClient.instance.postEditStudent(postItem).await()
            } else {
                val postItem = Student(null, prefs.getInt("class", 1), firstName, lastName)
                RetrofitClient.instance.postAddStudent(postItem).await()
            }
        }
    }

    fun remove(student: StudentNavigate?) {
        coroutineScope.launch {
            if (student != null) {
                val postItem = StudentId(student.studentId)
                RetrofitClient.instance.postRemoveStudent(postItem).await()
            }
        }
    }
}
