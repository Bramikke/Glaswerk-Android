package com.bramgoedvriend.glaswerk.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bramgoedvriend.glaswerk.database.DAOs.KlasDao
import com.bramgoedvriend.glaswerk.utils.getValue
import com.bramgoedvriend.glaswerk.utils.klas1
import com.bramgoedvriend.glaswerk.utils.klas2
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KlasDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var klasDao: KlasDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        klasDao = database.klasDao
        klasDao.insertAll(*arrayOf(klas1, klas2))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetItems() {
        val klasList = getValue(klasDao.getClasses())
        Assert.assertThat(klasList.size, Matchers.equalTo(2))
        Assert.assertThat(klasList[0], Matchers.equalTo(klas1))
        Assert.assertThat(klasList[1], Matchers.equalTo(klas2))
    }

    @Test
    fun testClear() {
        klasDao.delete()
        val klasList = getValue(klasDao.getClasses())
        Assert.assertThat(klasList.size, Matchers.equalTo(0))
    }
}
