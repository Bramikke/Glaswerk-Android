package com.bramgoedvriend.glaswerk.damage.damage_student

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bramgoedvriend.glaswerk.database.getDatabase
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.network.*
import com.bramgoedvriend.glaswerk.repository.ClassRepository
import com.bramgoedvriend.glaswerk.repository.StudentRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class DamageStudentViewModel(application: Application, private val item: DamageItemNavigate) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val studentRepository = StudentRepository(database)
    private val classRepository = ClassRepository(database)

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
    var students = studentRepository.studentsByClassByItem(item.itemid, prefs.getInt("class", 1))

    fun updateClass() {
        try {
            klas = classRepository.getClass(prefs.getInt("class", 1))
            students = studentRepository.studentsByClassByItem(item.itemid, prefs.getInt("class", 1))
        } catch (t:Throwable) {
            klas = MutableLiveData(Klas(-1, "geen klas"))
            students = MutableLiveData(null)
        }
    }

    fun studentBroke(student: Student, onPurpose: Int) : String {
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
                        student.studentId,
                        item.itemid,
                        onPurpose
                    )
                    RetrofitClient.instance.postStudentItemBrokenAsync(studentItem).await()
                } catch (t: Throwable) {
                    returnMessage = "Error"
                }
            }
            returnMessage =
                "${student.firstName} heeft een ${item.itemName} ${if (onPurpose == 0) "niet" else ""} opzettelijk gebroken."
        } else {
            returnMessage = "Je bent momenteel offline. Probeer later opnieuw."
        }
        return returnMessage
    }
}