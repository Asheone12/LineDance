package com.muen.linedance.bean

import android.graphics.Canvas
import android.graphics.Paint
import com.muen.linedance.ui.view.GameView

class CharacterBean(gameView: GameView){
    private var gameView: GameView
    var isHideFlag = false

    init {
        this.gameView = gameView
    }

    fun draw(canvas: Canvas, flag: Boolean) {
        if (flag) {
            canvas.drawBitmap(
                gameView.characterBitmap!!,
                gameView.characterPosX.toFloat(),
                gameView.characterPosY.toFloat(),
                Paint()
            )
            isHideFlag = false
        } else {
            canvas.drawBitmap(
                gameView.characterHideBitmap!!,
                gameView.characterPosX.toFloat(),
                gameView.characterPosY.toFloat(),
                Paint()
            )
            isHideFlag = true
        }
    }

    fun getY(): Int {
        return gameView.characterPosY + 30
    }
}