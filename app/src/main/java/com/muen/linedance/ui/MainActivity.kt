package com.muen.linedance.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.muen.linedance.R
import com.muen.linedance.databinding.ActivityMainBinding
import com.muen.linedance.utils.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnStart.setOnClickListener {
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        }
    }


}