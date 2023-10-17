package com.jssg.servicemanagersystem.utils

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/10/17.
 */
object BitmapUtils {
    fun compressFile(file: File, newPath: String): File? {
        var compressFile = File(newPath)
        if (!compressFile.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        var bos: BufferedOutputStream? = null
        try {
            bos = BufferedOutputStream(FileOutputStream(compressFile))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
            bitmap.recycle()

            return compressFile
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}