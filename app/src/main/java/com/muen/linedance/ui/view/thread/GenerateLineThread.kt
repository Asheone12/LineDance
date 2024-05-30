package com.muen.linedance.ui.view.thread

import android.util.Log
import com.muen.linedance.GameConfig
import com.muen.linedance.bean.LineBean
import com.muen.linedance.ui.view.GameView
import java.util.Random

class GenerateLineThread(gameView: GameView) : Thread() {
    private val gameView: GameView
    private var nonEndFlag = false
    private var pauseFlag = false
    private val random = Random()
    private var lineType = 0
    private val mGeneraThreadLock: Object
    private var generateTotal = 0

    init {
        this.gameView = gameView
        this.mGeneraThreadLock = Object()
        nonEndFlag = true
        pauseFlag = false
        generateTotal = 0
    }

    override fun run() {
        super.run()
        while (nonEndFlag) {
            if (pauseFlag) {
                synchronized(mGeneraThreadLock) {
                    try {
                        mGeneraThreadLock.wait()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            } else {
                try {
                    lineType = if (generateTotal <= 15) {
                        Math.abs(random.nextInt()) % 3
                    } else {
                        Math.abs(random.nextInt()) % 5
                    }
                    Log.d("GenerateLineThread:", "line type:$lineType")
                    gameView.lines.add(LineBean(lineType, gameView))
                    generateTotal++
                } catch (e: Exception) {
                }
                try {
                    sleep(GameConfig.GenerateLineSpan)
                } catch (e: Exception) {
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

    fun setGenerateTotal(generateTotal: Int) {
        this.generateTotal = generateTotal
    }

    fun resumeThread() {
        synchronized(mGeneraThreadLock) {
            setPauseFlag(false)
            mGeneraThreadLock.notify()
        }
    }

}