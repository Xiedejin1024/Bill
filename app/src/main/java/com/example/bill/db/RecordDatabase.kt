package com.example.bill.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @Author       : dejin
 * @Date         : 2023-11-20
 * @Description  : 描述
 */
@Database(version = 1, entities = [Record::class])
abstract class RecordDatabase : RoomDatabase() {

    abstract fun recordDao():RecordDao

    companion object {
        private var instance: RecordDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): RecordDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                RecordDatabase::class.java,
                "record_database.db"
            )
                .build().apply { instance = this }
        }
    }
}