package com.muen.linedance.ui.view.thread

import com.muen.linedance.GameConfig
import com.muen.linedance.ui.view.GameView

class MoveThread(gameView: GameView) : Thread() {
    private val gameView: GameView
    private var mMoveThreadLock: Object
    private var nonEndFlag = true
    private var pauseFlag = false
    private var screenHeight = 0
    private val sleepSpan = 10

    init {
        this.gameView = gameView
        this.screenHeight = gameView.screenHeight
        mMoveThreadLock = Object()
        pauseFlag = false
        nonEndFlag = true
    }

    fun resumeThread() {
        synchronized(mMoveThreadLock) {
            pauseFlag = false
            mMoveThreadLock.notify()
        }
    }

    override fun run() {
        super.run()
        while (nonEndFlag) {
            if (pauseFlag) {
                synchronized(mMoveThreadLock) {
                    try {
                        mMoveThreadLock.wait()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            } else {
                try {
                    for (line in gameView.lines) {
                        line.move() //调用line自身的move方法使直线往下运动
                        if (line.getY() >= gameView.characterPosY + GameConfig.characterSize && !line.isCountFlag()) {
                            //当直线通过character时统计分数
                            gameView.playSound(GameConfig.through, 0)
                            when (line.type) {
                                0 -> gameView.addScore(20)
                                1 -> gameView.addScore(30)
                                2 -> gameView.addScore(40)
                                3 -> gameView.addScore(50)
                                4 -> gameView.addScore(60)
                            }
                            line.setCountFlag(true)
                        }
                        if (line.getY() >= screenHeight) {
                            gameView.lines.remove(line) //每当一条直线超过屏幕距离的时候删除该直线对象
                        }
                        if (line.collision(gameView)) {
                            gameView.endGame()
                        }
                    }
                } catch (e: Exception) {
                }
                try {
                    sleep(sleepSpan.toLong())
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
}