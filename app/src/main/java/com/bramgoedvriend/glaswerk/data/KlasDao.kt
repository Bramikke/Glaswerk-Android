package com.bramgoedvriend.glaswerk.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KlasDao {
    @Query("select * from klas")
    fun getClasses(): LiveData<List<Klas>>

    @Query("select * from klas where klasid = :classid limit 1")
    fun getClass(classid: Int): LiveData<Klas>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg clazz: Klas)
}