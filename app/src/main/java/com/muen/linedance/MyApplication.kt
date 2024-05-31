package com.muen.linedance

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV

class MyApplication:Application() {
    private lateinit var mContext: Context
    override fun onCreate() {
        super.onCreate()
        mContext = this
        //初始化MMKV，返回缓存地址
        MMKV.initialize(this)
    }

    fun getContext():Context{
        return mContext
    }
}