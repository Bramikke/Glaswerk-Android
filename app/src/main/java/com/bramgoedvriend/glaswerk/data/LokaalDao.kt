package com.bramgoedvriend.glaswerk.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LokaalDao {
    @Query("select * from lokaal")
    fun getRooms(): LiveData<List<Lokaal>>

    @Query("select * from lokaal where lokaalid = :roomid limit 1")
    fun getRoom(roomid: Int): LiveData<Lokaal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg room: Lokaal)
}