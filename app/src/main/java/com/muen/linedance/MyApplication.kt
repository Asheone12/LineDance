package com.muen.linedance

import android.app.Application
import android.content.Context

class MyApplication:Application() {
    private lateinit var mContext: Context
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    fun getContext():Context{
        return mContext
    }
}