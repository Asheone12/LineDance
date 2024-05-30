package com.muen.linedance.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.muen.linedance.ui.view.GameView

class GameActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.repeatCount == 0) {
            val intent = Intent(this, MainActivity::class.java)
            gameView.recycleBitmap()
            startActivity(intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}