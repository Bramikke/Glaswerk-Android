package com.bramgoedvriend.glaswerk.damage.damage_student

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.data.AppDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.data.Klas
import com.bramgoedvriend.glaswerk.data.Student
import com.bramgoedvriend.glaswerk.data.StudentAndStudentItem
import com.bramgoedvriend.glaswerk.network.*
import com.bramgoedvriend.glaswerk.repository.ClassRepository
import com.bramgoedvriend.glaswerk.repository.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DamageStudentViewModel(application: Application, private val item: DamageItemNavigate) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = AppDatabase.getInstance(application)
    private val studentRepository = StudentRepository.getInstance(database.studentDao)
    private val classRepository = ClassRepository.getInstance(database.klasDao)

    private val prefs =  application.getSharedPreferences("ClassRoom", AppCompatActivity.MODE_PRIVATE)

    init {
        coroutineScope.launch {
            try {
                studentRepository.refresh()
                classRepository.refresh()
            } catch (t:Throwable) {
                _status.value = ApiStatus.OFFLINE
            }
        }
    }

    var klas = classRepository.getClass(prefs.getInt("class", 1))
    var students = studentRepository.studentsByClass(prefs.getInt("class", 1))

    fun updateClass() {
        try {
            klas = classRepository.getClass(prefs.getInt("class", 1))
            students = studentRepository.studentsByClass(prefs.getInt("class", 1))
        } catch (t:Throwable) {
            klas = MutableLiveData(Klas(-1, "geen klas"))
            students = MutableLiveData(null)
        }
    }

    fun studentBroke(student: StudentAndStudentItem, onPurpose: Int) : String {
        var returnMessage: String;
        if(status.value != ApiStatus.OFFLINE) {
            coroutineScope.launch {
                try {
                    RetrofitClient.instance.postReduceItemAsync(ItemId(item.itemid)).await()
                } catch (t: Throwable) {
                    returnMessage = "Error"
                }
            }
            coroutineScope.launch {
                try {
                    val studentItem = StudentItem(
                        student.student.leerlingId,
                        item.itemid,
                        onPurpose
                    )
                    RetrofitClient.instance.postStudentItemBrokenAsync(studentItem).await()
                } catch (t: Throwable) {
                    returnMessage = "Error"
                }
            }
            returnMessage =
                "${student.student.voornaam} heeft een ${item.itemName} ${if (onPurpose == 0) "niet" else ""} opzettelijk gebroken."
        } else {
            returnMessage = "Je bent momenteel offline. Probeer later opnieuw."
        }
        return returnMessage
    }

    fun filter(studentAndStudentItem: List<StudentAndStudentItem>): List<StudentAndStudentItem>? {
        return studentAndStudentItem.map {
            StudentAndStudentItem(
                it.student,
                it.studentItems.filter {
                    it.itemId == item.itemid
                }
            )
        }
    }
}