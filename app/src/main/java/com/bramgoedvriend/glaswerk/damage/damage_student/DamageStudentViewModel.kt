package com.bramgoedvriend.glaswerk.damage.damage_student

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.network.ReduceItem
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import com.bramgoedvriend.glaswerk.network.StudentItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class DamageStudentViewModel(application: Application, private val args: DamageStudentFragmentArgs) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _students = MutableLiveData<List<Student>>()
    val items: LiveData<List<Student>>
        get() = _students

    private val _selectStudent = MutableLiveData<Student>()
    val selectStudent
        get() = _selectStudent

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getItems()
    }

    private fun getItems() {
        _status.value = ApiStatus.LOADING
        coroutineScope.launch {
            try {
                val result = RetrofitClient.instance.getStudentsByClassByItemAsync(1, args.itemId).await() //TODO: get class of student
                _students.value = result
                _status.value = ApiStatus.DONE
            } catch (t:Throwable) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun onStudentClicked(student: Student) {
        _selectStudent.value = student
    }

    fun onNavigated() {
        _selectStudent.value = null
    }

    fun studentBroke(student: Student, onPurpose: Int) : String {
        var returnMessage = "";
        coroutineScope.launch {
            //try {
                val studentItem = StudentItem(
                    student.leerlingid,
                    args.itemId,
                    onPurpose
                )
                RetrofitClient.instance.postStudentItemBrokenAsync(studentItem).await()

                val aantal = args.itemAmount - 1
                val reduceItem = ReduceItem(
                    args.itemId,
                    aantal
                )
                RetrofitClient.instance.postReduceItemAsync(reduceItem).await()
            /*} catch (t:Throwable) {
                returnMessage = "Error"
            }*/
        }
        returnMessage = "${student.voornaam} heeft een ${args.itemName} ${if(onPurpose==0) "niet" else ""} opzettelijk gebroken."
        return returnMessage
    }
}