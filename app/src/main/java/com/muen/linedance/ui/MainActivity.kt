package com.muen.linedance.ui

import android.content.Intent
import com.muen.linedance.GameConfig
import com.muen.linedance.databinding.ActivityMainBinding
import com.muen.linedance.utils.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        val widthPixels = resources.displayMetrics.widthPixels
        val heightPixels = resources.displayMetrics.heightPixels
        GameConfig.widthPixels = widthPixels
        GameConfig.heightPixels = heightPixels
        GameConfig.characterSize = (widthPixels / 4.32).toInt()
        GameConfig.generateLineSpan = (heightPixels / 2.4).toLong()
        GameConfig.spanBlue = (GameConfig.characterSize / 25)
        GameConfig.spanGreen = (GameConfig.characterSize / 19)
        GameConfig.spanPink = (GameConfig.characterSize / 14.7).toInt()
        GameConfig.spanOrange = (GameConfig.characterSize / 12.5).toInt()
        GameConfig.spanPurple = (GameConfig.characterSize / 10)
        GameConfig.gap = (heightPixels / 12)
        GameConfig.pauseButtonX = (widthPixels - GameConfig.gap)
        GameConfig.pauseButtonY = (GameConfig.characterSize / 10)
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnStart.setOnClickListener {
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        }
    }


}