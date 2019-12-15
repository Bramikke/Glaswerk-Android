package com.bramgoedvriend.glaswerk.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bramgoedvriend.glaswerk.database.DAOs.StudentDao
import com.bramgoedvriend.glaswerk.utils.getValue
import com.bramgoedvriend.glaswerk.utils.student1
import com.bramgoedvriend.glaswerk.utils.student2
import com.bramgoedvriend.glaswerk.utils.student3
import com.bramgoedvriend.glaswerk.utils.student4
import com.bramgoedvriend.glaswerk.utils.studentAndItem1
import com.bramgoedvriend.glaswerk.utils.studentAndItem2
import com.bramgoedvriend.glaswerk.utils.studentAndItem3
import com.bramgoedvriend.glaswerk.utils.studentAndItem4
import com.bramgoedvriend.glaswerk.utils.studentItem1
import com.bramgoedvriend.glaswerk.utils.studentItem2
import com.bramgoedvriend.glaswerk.utils.studentItem3
import com.bramgoedvriend.glaswerk.utils.studentItem4
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudentDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var studentDao: StudentDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        studentDao = database.studentDao
        studentDao.insertAll(*arrayOf(student1, student2, student3, student4))
        studentDao.insertStudentItems(*arrayOf(studentItem1, studentItem2, studentItem3, studentItem4))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetItems() {
        val studentList = getValue(studentDao.getStudents())
        Assert.assertThat(studentList.size, Matchers.equalTo(4))
        Assert.assertThat(studentList[0], Matchers.equalTo(studentAndItem1))
        Assert.assertThat(studentList[1], Matchers.equalTo(studentAndItem2))
        Assert.assertThat(studentList[2], Matchers.equalTo(studentAndItem3))
        Assert.assertThat(studentList[3], Matchers.equalTo(studentAndItem4))
    }

    @Test
    fun testClear() {
        studentDao.delete()
        val studentList = getValue(studentDao.getStudents())
        Assert.assertThat(studentList.size, Matchers.equalTo(0))
    }
}
