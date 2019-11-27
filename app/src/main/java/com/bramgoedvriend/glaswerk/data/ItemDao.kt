package com.bramgoedvriend.glaswerk.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemDao {
    @Query("select * from item order by naam")
    fun getItems(): LiveData<List<Item>>

    @Query("select * from item where lokaal_id = :roomid order by naam")
    fun getItemsByRoom(roomid: Int): LiveData<List<Item>>

    @Query("select * from item join lokaal on item.lokaal_id = lokaal.lokaalid where aantal < min_aantal order by naam, lokaalNaam")
    fun getItemOrders(): LiveData<List<ItemAndLokaal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg item: Item)

    @Query("DELETE FROM item")
    fun dropItems()
}