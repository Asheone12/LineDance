package com.muen.linedance.ui.view.thread

import com.muen.linedance.ui.view.GameView

class TimeThread(gameView: GameView):Thread() {
    private val gameView:GameView
    private val mTimePauseLock: Object
    private var nonEndFlag = false
    private var pauseFlag = false

    init {
        this.gameView = gameView
        this.mTimePauseLock = Object()
        this.pauseFlag = false
        this.nonEndFlag = true
    }

    override fun run() {
        super.run()
        while (nonEndFlag) {
            if (pauseFlag) {
                synchronized(mTimePauseLock) {
                    try {
                        mTimePauseLock.wait()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            } else {
                try {
                    gameView.addTimeCount()
                } catch (e: Exception) {
                }
                try {
                    sleep(1)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun setNonEndFlag(flag: Boolean) {
        nonEndFlag = flag
    }

    fun setPauseFlag(flag: Boolean) {
        pauseFlag = flag
    }

    fun resumeThread() {
        synchronized(mTimePauseLock) {
            setPauseFlag(false)
            mTimePauseLock.notify()
        }
    }
}