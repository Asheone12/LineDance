package com.muen.linedance.ui.view.thread

import android.graphics.Canvas
import android.view.SurfaceHolder
import com.muen.linedance.GameConfig
import com.muen.linedance.ui.view.GameView

class DrawThread(surfaceHolder: SurfaceHolder, gameView: GameView):Thread() {
    private val surfaceHolder: SurfaceHolder
    private val gameView: GameView
    private var flag = false

    init {
        this.surfaceHolder = surfaceHolder
        this.gameView = gameView
    }

    override fun run() {
        super.run()
        var c: Canvas?
        while (true) {
            c = null
            try {
                c = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    gameView.startDraw(c)
                    gameView.systemSecondCurrent = gameView.getSystemCurrentSecond()
                }
            } catch (e: Exception) {

            } finally {
                if (c != null) surfaceHolder.unlockCanvasAndPost(c)
            }
            try {
                sleep(GameConfig.sleepPeriod)
            } catch (e: Exception) {
                e.printStackTrace() //打印堆栈信息}
            }
        }
    }

    fun setFlag(flag: Boolean) {
        this.flag = flag
    }

}