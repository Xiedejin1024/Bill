package com.example.bill.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * @Author       : dejin
 * @Date         : 2023-11-20
 * @Description  : 描述
 */
@Dao
interface RecordDao {
    @Insert
    fun insertRecord(record: Record)

    @Delete
    fun deleteRecord(record: Record)

    @Update
    fun updateRecord(newRecord: Record)

    @Query("select * from Record")
    fun loadAllRecord(): List<Record>

}