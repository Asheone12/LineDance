package com.muen.linedance

import com.tencent.mmkv.MMKV

object GameConfig {
    private val mmkv = MMKV.defaultMMKV()

    //常量
    const val recordFileName = "Skip_the_line.txt"
    const val through = 1
    const val touch = 2
    const val breakRecord = 3
    const val end = 4
    const val sleepPeriod = 1L
    const val infinite = 999999999

    //屏幕参数
    private const val KEY_WIDTH_PIXELS = "width_pixels"
    private const val KEY_HEIGHT_PIXELS = "height_pixels"

    private const val KEY_CHARACTER_SIZE = "character_size"
    private const val KEY_GENERATE_LINE_SPAN = "generate_line_span"
    private const val KEY_SPAN_BLUE = "span_blue"
    private const val KEY_SPAN_GREEN = "span_green"
    private const val KEY_SPAN_ORANGE = "span_orange"
    private const val KEY_SPAN_PINK = "span_pink"
    private const val KEY_SPAN_PURPLE = "span_purple"
    private const val KEY_GAP = "span_gap"
    private const val KEY_PAUSE_BUTTON_X = "pause_button_x"
    private const val KEY_PAUSE_BUTTON_Y = "pause_button_y"

    //const val characterSize = 250
    //const val GenerateLineSpan = 800L
    //const val spanBlue = 10
    //const val spanGreen = 13
    //const val spanOrange = 17
    //const val spanPink = 20
    //const val spanPurple = 25
    //const val gap = 160
    //const val pauseButtonX = 940
    //const val pauseButtonY = 23

    //缓存的变量
    var widthPixels: Int
        set(value) {
            mmkv.encode(KEY_WIDTH_PIXELS, value)
        }
        get() = mmkv.decodeInt(KEY_WIDTH_PIXELS)

    var heightPixels: Int
        set(value) {
            mmkv.encode(KEY_HEIGHT_PIXELS, value)
        }
        get() = mmkv.decodeInt(KEY_HEIGHT_PIXELS)

    var characterSize: Int
        set(value) {
            mmkv.encode(KEY_CHARACTER_SIZE, value)
        }
        get() = mmkv.decodeInt(KEY_CHARACTER_SIZE)

    var generateLineSpan: Long
        set(value) {
            mmkv.encode(KEY_GENERATE_LINE_SPAN, value)
        }
        get() = mmkv.decodeLong(KEY_GENERATE_LINE_SPAN)

    var spanBlue: Int
        set(value) {
            mmkv.encode(KEY_SPAN_BLUE, value)
        }
        get() = mmkv.decodeInt(KEY_SPAN_BLUE)

    var spanGreen: Int
        set(value) {
            mmkv.encode(KEY_SPAN_GREEN, value)
        }
        get() = mmkv.decodeInt(KEY_SPAN_GREEN)

    var spanOrange: Int
        set(value) {
            mmkv.encode(KEY_SPAN_ORANGE, value)
        }
        get() = mmkv.decodeInt(KEY_SPAN_ORANGE)

    var spanPink: Int
        set(value) {
            mmkv.encode(KEY_SPAN_PINK, value)
        }
        get() = mmkv.decodeInt(KEY_SPAN_PINK)

    var spanPurple: Int
        set(value) {
            mmkv.encode(KEY_SPAN_PURPLE, value)
        }
        get() = mmkv.decodeInt(KEY_SPAN_PURPLE)

    var gap: Int
        set(value) {
            mmkv.encode(KEY_GAP, value)
        }
        get() = mmkv.decodeInt(KEY_GAP)

    var pauseButtonX: Int
        set(value) {
            mmkv.encode(KEY_PAUSE_BUTTON_X, value)
        }
        get() = mmkv.decodeInt(KEY_PAUSE_BUTTON_X)

    var pauseButtonY: Int
        set(value) {
            mmkv.encode(KEY_PAUSE_BUTTON_Y, value)
        }
        get() = mmkv.decodeInt(KEY_PAUSE_BUTTON_Y)

}