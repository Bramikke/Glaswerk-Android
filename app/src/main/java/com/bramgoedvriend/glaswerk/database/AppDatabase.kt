package com.bramgoedvriend.glaswerk.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bramgoedvriend.glaswerk.database.DAOs.ItemDao
import com.bramgoedvriend.glaswerk.database.DAOs.KlasDao
import com.bramgoedvriend.glaswerk.database.DAOs.LokaalDao
import com.bramgoedvriend.glaswerk.database.DAOs.StudentDao
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.domain.Student
import com.bramgoedvriend.glaswerk.domain.StudentItem
import com.bramgoedvriend.glaswerk.utils.DATABASE_NAME

@Database(
    entities = [Item::class, Student::class, Lokaal::class, Klas::class, StudentItem::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val itemDao: ItemDao
    abstract val studentDao: StudentDao
    abstract val lokaalDao: LokaalDao
    abstract val klasDao: KlasDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration().build()
        }
    }
}
