package com.muen.linedance.ui

import android.content.Intent
import android.view.KeyEvent
import com.muen.linedance.databinding.ActivityGameBinding
import com.muen.linedance.utils.BaseActivity

class GameActivity : BaseActivity<ActivityGameBinding>() {
    override fun onCreateViewBinding(): ActivityGameBinding {
        return ActivityGameBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
    }

    override fun onResume() {
        super.onResume()
        viewBinding.gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        viewBinding.gameView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.gameView.destroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.repeatCount == 0) {
            val intent = Intent(this, MainActivity::class.java)
            viewBinding.gameView.recycleBitmap()
            startActivity(intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}