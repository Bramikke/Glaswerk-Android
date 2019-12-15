package com.bramgoedvriend.glaswerk.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bramgoedvriend.glaswerk.database.DAOs.ItemDao
import com.bramgoedvriend.glaswerk.utils.getValue
import com.bramgoedvriend.glaswerk.utils.item1
import com.bramgoedvriend.glaswerk.utils.item2
import com.bramgoedvriend.glaswerk.utils.item3
import com.bramgoedvriend.glaswerk.utils.item4
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var itemDao: ItemDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        itemDao = database.itemDao
        itemDao.insertAll(*arrayOf(item1, item2, item3, item4))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetItems() {
        val itemList = getValue(itemDao.getItems())
        Assert.assertThat(itemList.size, Matchers.equalTo(4))
        Assert.assertThat(itemList[0], Matchers.equalTo(item1))
        Assert.assertThat(itemList[1], Matchers.equalTo(item2))
        Assert.assertThat(itemList[2], Matchers.equalTo(item3))
        Assert.assertThat(itemList[3], Matchers.equalTo(item4))
    }

    @Test
    fun testClear() {
        itemDao.delete()
        val itemList = getValue(itemDao.getItems())
        Assert.assertThat(itemList.size, Matchers.equalTo(0))
    }
}
