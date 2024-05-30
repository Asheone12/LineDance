package com.muen.linedance.bean

import android.graphics.Canvas
import android.graphics.Paint
import com.muen.linedance.GameConfig
import com.muen.linedance.ui.view.GameView

class LineBean(type: Int, gameView: GameView) {
    private var screenHeight = 0
    private var screenWidth = 0
    private var y = 160
    var type = 0
    private var spanY = 0
    private var countFlag = false
    private var gameView: GameView? = null

    init {
        this.type = type
        this.gameView = gameView
        screenHeight = gameView.screenHeight
        screenWidth = gameView.screenWidth
        countFlag = false
        spanY = when (type) {
            0 -> GameConfig.spanBlue
            1 -> GameConfig.spanGreen
            2 -> GameConfig.spanOrange
            3 -> GameConfig.spanPink
            4 -> GameConfig.spanPurple
            else -> GameConfig.spanBlue
        }
    }


    fun draw(canvas: Canvas, type: Int) {
        when (type) {
            0 -> canvas.drawBitmap(gameView?.linePicture?.get(0)!!, 0f, y.toFloat(), Paint())
            1 -> canvas.drawBitmap(gameView?.linePicture?.get(1)!!, 0f, y.toFloat(), Paint())
            2 -> canvas.drawBitmap(gameView?.linePicture?.get(2)!!, 0f, y.toFloat(), Paint())
            3 -> canvas.drawBitmap(gameView?.linePicture?.get(3)!!, 0f, y.toFloat(), Paint())
            4 -> canvas.drawBitmap(gameView?.linePicture?.get(4)!!, 0f, y.toFloat(), Paint())
        }
    }

    fun move() {
        when (type) {
            0 -> GameConfig.spanBlue
            1 -> GameConfig.spanGreen
            2 -> GameConfig.spanOrange
            3 -> GameConfig.spanPink
            4 -> GameConfig.spanPurple
        }
        if (y <= screenHeight) {
            y += spanY
        }
    }

    fun getY(): Int {
        return y
    }

    fun isCountFlag(): Boolean {
        return countFlag
    }

    fun setCountFlag(countFlag: Boolean) {
        this.countFlag = countFlag
    }

    fun collision(gameView: GameView): Boolean {
        if (!gameView.character.isHideFlag) {
            if (isCollision(gameView.character.getY(), getY())) {
                StateMachine.setState(StateMachine.StateEnum.Result)
                return true
            }
        }
        return false
    }

    private fun isCollision(cY: Int, lY: Int): Boolean {
        return lY > cY && lY + 20 < cY + GameConfig.characterSize - 120
    }
}