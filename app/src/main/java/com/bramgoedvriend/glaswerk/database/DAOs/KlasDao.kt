package com.bramgoedvriend.glaswerk.database.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bramgoedvriend.glaswerk.domain.Klas

@Dao
interface KlasDao {
    @Query("select * from klas")
    fun getClasses(): LiveData<List<Klas>>

    @Query("select * from klas where klasid = :classid limit 1")
    fun getClass(classid: Int): LiveData<Klas>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg clazz: Klas)

    @Query("DELETE FROM klas")
    fun delete()
}
