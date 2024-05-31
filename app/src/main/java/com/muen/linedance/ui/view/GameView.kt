package com.muen.linedance.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import com.muen.linedance.GameConfig
import com.muen.linedance.R
import com.muen.linedance.bean.CharacterBean
import com.muen.linedance.bean.LineBean
import com.muen.linedance.ui.view.thread.DrawThread
import com.muen.linedance.ui.view.thread.GenerateLineThread
import com.muen.linedance.ui.view.thread.MoveThread
import com.muen.linedance.ui.view.thread.TimeThread
import com.muen.linedance.utils.BitmapUtil
import java.text.SimpleDateFormat
import java.util.Date

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private var moveThread: MoveThread? = null //直线运动线程
    private var thread: DrawThread? = null //刷屏线程
    private var generateLineThread: GenerateLineThread? = null //产生直线的线程
    private var timeThread: TimeThread? = null //计时线程

    var linePicture = arrayOfNulls<Bitmap>(5)
    var lines: ArrayList<LineBean> = ArrayList() //直线的集合

    private var scoreBitmap: Bitmap? = null
    private var timeBitmap: Bitmap? = null
    private var pauseButtonBitmap: Bitmap? = null
    private var pauseBGBitmap: Bitmap? = null
    private var gameBgBitmap: Bitmap? = null
    var characterBitmap: Bitmap? = null
    var characterHideBitmap: Bitmap? = null
    private var gameOverBitmap: Bitmap? = null

    private var paint: Paint? = null
    private var textPaint = Paint()
    private var textPaintResult = Paint()
    val character = CharacterBean(this)

    private var bgMusicFlag = false         //背景音乐标识
    private var breakRecordFlag = false     //破记录标识
    private var pauseFlag = false           //游戏暂停标识
    private var gameOverFlag = false        //游戏结束标识

    private val wm = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val screenWidth: Int = wm.defaultDisplay.width
    val screenHeight: Int = wm.defaultDisplay.height
    var characterPosX = 0
    var characterPosY = 0
    private var pauseButtonPosX = 0
    private var widgetSize = 0

    private var score = 0
    private var timeCount = 0
    private var timeStart: Int = GameConfig.infinite
    private var systemSecondStart = 0
    var systemSecondCurrent = 0
    private var characterFlag = true //characterFlag为true的时候显示为非隐藏


    init {
        //注册接口
        holder.addCallback(this)
        //初始化线程
        thread = DrawThread(holder, this) //初始化刷帧线程
        moveThread = MoveThread(this) //初始化移动的线程,move线程管画面所有的物体的运动
        generateLineThread = GenerateLineThread(this) //初始化产生直线的线程
        timeThread = TimeThread(this) //初始化计时线程
        //初始化文字画笔
        textPaint.setARGB(255, 255, 255, 255)
        textPaint.isAntiAlias = true
        textPaint.textSize = 70f
        textPaintResult.setARGB(255, 255, 255, 255)
        textPaintResult.isAntiAlias = true
        textPaintResult.textSize = 70f

        //TODO 在此处获取系统时间

        //初始化标识
        pauseFlag = false
        gameOverFlag = false
        bgMusicFlag = false
        breakRecordFlag = false

        widgetSize = (0.0625 * screenHeight).toInt()
        characterPosX = screenWidth / 2 - GameConfig.characterSize / 2
        characterPosY = (0.625 * screenHeight).toInt() - GameConfig.characterSize / 2
        pauseButtonPosX = screenWidth - widgetSize - 20

        iniBitmap()
        initSounds()

        //当surfaceView创建的时候，所有的线程开始运行
        if (thread != null) {
            thread?.setFlag(true)
            thread?.start()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) { characterFlag = true }
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    fun startDraw(canvas: Canvas?) {
        //绘画背景
        canvas!!.drawBitmap(gameBgBitmap!!, 0f, 0f, paint)
        //画分数图标
        canvas.drawBitmap(scoreBitmap!!, 50f, 20f, paint)
        //画计时图标
        canvas.drawBitmap(timeBitmap!!, (0.333 * screenWidth).toInt().toFloat(), 23f, paint)

        //画暂停按钮图标
        canvas.drawBitmap(
            pauseButtonBitmap!!,
            pauseButtonPosX.toFloat(),
            GameConfig.pauseButtonY.toFloat(),
            paint
        )

        //绘制时间
        canvas.drawText(
            BitmapUtil.getTime(timeCount)!!,
            (0.333 * screenWidth + widgetSize + 20).toInt().toFloat(),
            (widgetSize / 1.1).toInt().toFloat(), textPaint
        )
        //绘制分数
        canvas.drawText(
            score.toString() + "", (widgetSize + 70).toFloat(), (widgetSize / 1.1).toInt()
                .toFloat(), textPaint
        )


        if (timeCount - timeStart >= GameConfig.gap) {
            characterFlag = true
            timeStart = GameConfig.infinite
        }
        character.draw(canvas, characterFlag)
        for (l in lines) {
            l.draw(canvas, l.type)
        }

        if (pauseFlag) canvas.drawBitmap(pauseBGBitmap!!, 0f, 0f, paint)
        if (gameOverFlag) {
            canvas.drawBitmap(gameOverBitmap!!, 0f, 0f, paint)
            canvas.drawText(
                "score:" + score + "  time:" + BitmapUtil.getTime(timeCount),
                (0.25 * screenWidth).toInt().toFloat(),
                (0.600 * screenHeight).toInt().toFloat(),
                textPaintResult
            )
        }
        if (systemSecondCurrent < systemSecondStart) {
            systemSecondCurrent += 60
        }
        threadStart()
    }

    private fun threadStart() {
        moveThread?.setNonEndFlag(true)
        moveThread?.setPauseFlag(false)
        moveThread?.start()
        generateLineThread?.setNonEndFlag(true)
        generateLineThread?.setPauseFlag(false)
        generateLineThread?.start()
        timeThread?.setNonEndFlag(true)
        timeThread?.setPauseFlag(false)
        timeThread?.start()
    }

    private fun restartGame() {
        timeCount = 0
        score = 0
        systemSecondStart = getSystemCurrentSecond()
        gameOverFlag = false
        characterFlag = true
        breakRecordFlag = false
        resume()
    }

    fun pause() {
        moveThread!!.setPauseFlag(true)
        generateLineThread!!.setPauseFlag(true)
        timeThread!!.setPauseFlag(true)
        pauseFlag = true
    }

    fun resume() {
        generateLineThread!!.resumeThread()
        moveThread!!.resumeThread()
        timeThread!!.resumeThread()
        pauseFlag = false
    }

    fun endGame() {
        playSound(GameConfig.end, 0)
        val time: String = getTime()
        moveThread?.setPauseFlag(true)
        generateLineThread?.setPauseFlag(true)
        generateLineThread?.setGenerateTotal(0)
        timeThread?.setPauseFlag(true)
        lines.clear()
        gameOverFlag = true
    }

    fun destroy() {
        if (thread != null) {
            thread?.setFlag(false)
        }
        moveThread?.setNonEndFlag(false)
        moveThread?.interrupt()

        generateLineThread?.setNonEndFlag(true)
        generateLineThread?.interrupt()

        characterFlag = false
        timeThread?.setNonEndFlag(false)
        timeThread?.interrupt()

        recycleBitmap()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val eventType = event?.action
        val touchPosX: Int
        val touchPosY: Int

        when (eventType) {
            MotionEvent.ACTION_DOWN -> {
                touchPosX = event.rawX.toInt()
                touchPosY = event.rawY.toInt()
                playSound(GameConfig.touch, 0)
                if (gameOverFlag) {
                    restartGame()
                } else {
                    if (pauseFlag) {
                        pauseFlag = false
                        this.resume()
                    } else {
                        if (touchPosX <= GameConfig.pauseButtonX + widgetSize + 30 && touchPosX >= GameConfig.pauseButtonX && touchPosY <= GameConfig.pauseButtonY + widgetSize + 60 && touchPosY >= GameConfig.pauseButtonY) {
                            this.pause()
                        } else {
                            timeStart = timeCount
                            characterFlag = false
                        }
                    }
                }
            }

            MotionEvent.ACTION_UP -> {}
        }
        return true
    }

    fun addScore(score: Int) {
        this.score += score
    }

    fun addTimeCount() {
        timeCount++
    }

    private fun getTime(): String {
        val formatter = SimpleDateFormat("MM月dd日HH:mm:ss")
        val curDate = Date(System.currentTimeMillis())
        return formatter.format(curDate)
    }

    fun getSystemCurrentSecond(): Int {
        val time: String = getTime()
        val secondCurrent: Int
        val timeSet = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray();
        secondCurrent = timeSet[2].toInt()
        return secondCurrent
    }

    private fun initSounds() {

    }

    fun playSound(sound: Int, loop: Int) {

    }

    private fun iniBitmap() {
        paint = Paint()
        val opts = BitmapFactory.Options()
        opts.inSampleSize = 3
        gameBgBitmap = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.bg, opts),
            screenWidth,
            screenHeight
        )
        characterBitmap = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.character),
            GameConfig.characterSize,
            GameConfig.characterSize
        )
        linePicture[0] = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.lineblue,
                opts
            ), screenWidth, 20
        )
        linePicture[1] = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.linegreen,
                opts
            ), screenWidth, 20
        )
        linePicture[2] = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.lineorange,
                opts
            ), screenWidth, 20
        )
        linePicture[3] = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.linepink,
                opts
            ), screenWidth, 20
        )
        linePicture[4] = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.linepurle,
                opts
            ), screenWidth, 20
        )
        scoreBitmap = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.score, opts),
            widgetSize,
            widgetSize
        )
        timeBitmap = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.time, opts),
            widgetSize - 9,
            widgetSize + 2
        )
        characterHideBitmap = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.characterhide
            ), GameConfig.characterSize, GameConfig.characterSize
        )
        pauseButtonBitmap = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.pause, opts),
            widgetSize - 10,
            widgetSize
        )
        pauseBGBitmap = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.pausebg),
            screenWidth,
            screenHeight
        )
        gameOverBitmap = BitmapUtil.resizeBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.gameover),
            screenWidth,
            screenHeight
        )
    }

    fun recycleBitmap() {
        gameOverBitmap?.recycle()
        scoreBitmap?.recycle()
        timeBitmap?.recycle()
        pauseBGBitmap?.recycle()
        gameBgBitmap?.recycle()
        characterHideBitmap?.recycle()
        characterBitmap?.recycle()
        for (iterator in linePicture) {
            iterator?.recycle()
        }
        System.gc()
    }

}