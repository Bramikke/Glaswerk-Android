package com.bramgoedvriend.glaswerk.damage.damage_student

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

    init {
        getItems()
    }

    private fun getItems() {

        val result = RetrofitClient.instance.getStudentsByClassByItem(1, args.itemId) //TODO: get class of student
        result.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _status.value = ApiStatus.LOADING }
            .doOnTerminate { _status.value = ApiStatus.DONE }
            .doOnError { _status.value = ApiStatus.ERROR }
            .subscribe { res -> _students.value = res }

    }

    fun onStudentClicked(student: Student) {
        _selectStudent.value = student
    }

    fun onNavigated() {
        _selectStudent.value = null
    }

    fun studentBroke(student: Student, onPurpose: Int) : String {
        var mapStudentItemBroken = HashMap<String, Any>()
        mapStudentItemBroken["leerlingid"] = student.leerlingid
        mapStudentItemBroken["itemid"] = args.itemId
        mapStudentItemBroken["opzettelijk"] = onPurpose
        val result1 = RetrofitClient.instance.postStudentItemBroken(mapStudentItemBroken)
        result1.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        var mapReduceItem = HashMap<String, Int>()
        mapReduceItem["itemid"] = args.itemId
        mapReduceItem["aantal"] = args.itemAmount - 1
        val result2 = RetrofitClient.instance.postReduceItem(mapReduceItem)
        result2.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        return "${student.voornaam} heeft een ${args.itemName} ${if(onPurpose==0) "niet" else ""} opzettelijk gebroken."
    }
}