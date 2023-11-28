package com.example.bill.db

import android.widget.ImageView
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author       : dejin
 * @Date         : 2023-11-20
 * @Description  : 描述
 */
@Entity
data class Record(
    var isPay: Int,
    var money: String,//金额
    var purpose: String,//用途
    var time: String,//时间
    var desc: String,//备注
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}