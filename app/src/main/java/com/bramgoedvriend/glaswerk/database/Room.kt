package com.bramgoedvriend.glaswerk.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.domain.StudentItem

@Dao
interface ItemDao {
    @Query("select * from databaseitem order by naam")
    fun getItems(): LiveData<List<DatabaseItem>>

    @Query("select * from databaseitem where lokaal_id = :roomid order by naam")
    fun getItemsByRoom(roomid: Int): LiveData<List<DatabaseItem>>

    @Query("select * from databaseitem, databaseroom " +
            "where lokaal_id=roomId and aantal < min_aantal " +
            "order by name, naam")
    fun getItemOrders(): LiveData<List<DatabaseItemRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg item: DatabaseItem)

    @Query("DELETE FROM databaseitem")
    fun dropItems()
}

@Dao
interface StudentDao {
    @Query("select ds.studentId, classId, firstName, lastName, count(dsi.studentId) as brokenAmount " +
            "from databasestudent ds left join databasestudentitem dsi on ds.studentId = dsi.studentId " +
            "group by ds.studentId " +
            "order by firstName")
    fun getStudents(): LiveData<List<Student>>

    @Query("select ds.studentId, ds.classId, ds.firstName, ds.lastName, count(CASE WHEN dsi.itemId=:itemid THEN 1 ELSE NULL END) as brokenAmount from databasestudent ds left join databasestudentitem dsi on ds.studentId = dsi.studentId where ds.classId = :classid group by ds.studentId")
    fun getStudentsByClassByItem(itemid: Int, classid: Int): LiveData<List<Student>>

    @Query("select ds.studentId, ds.classId, ds.firstName, ds.lastName, count(dsi.itemId) as brokenAmount from databasestudent ds left join databasestudentitem dsi on ds.studentId = dsi.studentId where classId = :classid group by ds.studentId order by firstName")
    fun getStudentsByClass(classid: Int): LiveData<List<Student>>

    @Query("select * from databasestudentitem")
    fun getStudentItem(): LiveData<List<StudentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg student: DatabaseStudent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudentItems(vararg studentItem: DatabaseStudentItem)

    @Query("DELETE FROM databasestudentitem")
    fun dropStudentItems()

    @Query("DELETE FROM databasestudent")
    fun dropStudents()
}

@Dao
interface RoomDao {
    @Query("select * from databaseroom")
    fun getRooms(): LiveData<List<DatabaseRoom>>

    @Query("select * from databaseroom where roomId = :roomid limit 1")
    fun getRoom(roomid: Int): LiveData<Lokaal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg room: DatabaseRoom)
}

@Dao
interface ClassDao {
    @Query("select * from databaseclass")
    fun getClasses(): LiveData<List<DatabaseClass>>

    @Query("select * from databaseclass where classId = :classid limit 1")
    fun getClass(classid: Int): LiveData<Klas>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg clazz: DatabaseClass)
}

@androidx.room.Database(
    entities = [
        DatabaseItem::class,
        DatabaseStudent::class,
        DatabaseRoom::class,
        DatabaseClass::class,
        DatabaseStudentItem::class
    ], version = 3,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract val itemDao: ItemDao
    abstract val studentDao: StudentDao
    abstract val roomDao: RoomDao
    abstract val classDao: ClassDao
}

private lateinit var INSTANCE: Database

fun getDatabase(context: Context): Database {
    synchronized(Database::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                "database"
            ).fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}