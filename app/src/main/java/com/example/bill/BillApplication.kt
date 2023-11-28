package com.example.bill

import android.app.Application
import android.content.Context

/**
 * @Author       : dejin
 * @Date         : 2023-11-19
 * @Description  : 描述
 */
class BillApplication : Application(){
    companion object {
        @SuppressWarnings("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}