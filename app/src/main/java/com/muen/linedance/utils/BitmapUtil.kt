package com.muen.linedance.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object BitmapUtil {
    fun getTime(timeInt: Int): String? {
        val timeString: String
        val minutes: Int = timeInt / 1000 % 3600 / 60
        val seconds: Int = timeInt / 1000 % 60
        timeString = minutes.toString() + ":" + seconds + ":" + timeInt % 100
        return timeString
    }


    fun decodeBitmap(path: String?): ByteArray? {
        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = true // 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts)
        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800)
        opts.inJustDecodeBounds = false // 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true
        opts.inInputShareable = true
        opts.inDither = false
        opts.inPurgeable = true
        opts.inTempStorage = ByteArray(16 * 1024)
        var fis: FileInputStream? = null
        var bmp: Bitmap? = null
        var baos: ByteArrayOutputStream? = null
        try {
            fis = FileInputStream(path)
            bmp = BitmapFactory.decodeFileDescriptor(fis.fd, null, opts)
            val scale = getScaling(
                opts.outWidth * opts.outHeight,
                1024 * 600
            )
            val bmp2 = Bitmap.createScaledBitmap(
                bmp,
                (opts.outWidth * scale).toInt(),
                (opts.outHeight * scale).toInt(),
                true
            )
            bmp.recycle()
            baos = ByteArrayOutputStream()
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            bmp2.recycle()
            return baos.toByteArray()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fis!!.close()
                baos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            System.gc()
        }
        return baos!!.toByteArray()
    }

    private fun getScaling(src: Int, des: Int): Double {
        return Math.sqrt(des.toDouble() / src.toDouble())
    }


    fun computeSampleSize(
        options: BitmapFactory.Options,
        minSideLength: Int, maxNumOfPixels: Int
    ): Int {
        val initialSize = computeInitialSampleSize(
            options, minSideLength,
            maxNumOfPixels
        )
        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }
        return roundedSize
    }

    private fun computeInitialSampleSize(
        options: BitmapFactory.Options,
        minSideLength: Int, maxNumOfPixels: Int
    ): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()
        val lowerBound = if (maxNumOfPixels == -1) 1 else ceil(
            sqrt(w * h / maxNumOfPixels)
        ).toInt()
        val upperBound = if (minSideLength == -1) 128 else floor(w / minSideLength).coerceAtMost(
            floor(h / minSideLength)
        ).toInt()
        if (upperBound < lowerBound) {
            return lowerBound
        }
        return if (maxNumOfPixels == -1 && minSideLength == -1) {
            1
        } else if (minSideLength == -1) {
            lowerBound
        } else {
            upperBound
        }
    }

    fun resizeBitmap(bitmap: Bitmap?, w: Int, h: Int): Bitmap? {
        return if (bitmap != null) {
            val width = bitmap.width
            val height = bitmap.height
            val scaleWidth = w.toFloat() / width
            val scaleHeight = h.toFloat() / height
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            Bitmap.createBitmap(
                bitmap, 0, 0, width,
                height, matrix, true
            )
        } else {
            null
        }
    }
}