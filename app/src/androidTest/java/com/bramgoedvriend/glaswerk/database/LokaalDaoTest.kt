package com.bramgoedvriend.glaswerk.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bramgoedvriend.glaswerk.database.DAOs.LokaalDao
import com.bramgoedvriend.glaswerk.utils.getValue
import com.bramgoedvriend.glaswerk.utils.lokaal1
import com.bramgoedvriend.glaswerk.utils.lokaal2
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LokaalDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var lokaalDao: LokaalDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        lokaalDao = database.lokaalDao
        lokaalDao.insertAll(*arrayOf(lokaal1, lokaal2))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetItems() {
        val lokaalList = getValue(lokaalDao.getRooms())
        Assert.assertThat(lokaalList.size, Matchers.equalTo(2))
        Assert.assertThat(lokaalList[0], Matchers.equalTo(lokaal1))
        Assert.assertThat(lokaalList[1], Matchers.equalTo(lokaal2))
    }

    @Test
    fun testClear() {
        lokaalDao.delete()
        val lokaalList = getValue(lokaalDao.getRooms())
        Assert.assertThat(lokaalList.size, Matchers.equalTo(0))
    }
}
