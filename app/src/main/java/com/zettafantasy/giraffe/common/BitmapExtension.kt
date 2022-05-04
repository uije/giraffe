package com.zettafantasy.giraffe.common

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun Bitmap.save(context: Context): String {
    // path to /data/data/yourapp/app_data/imageDir
    // Create imageDir
    val directory = context.externalCacheDir ?: context.cacheDir
    val file = File(directory, "wordcloud_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use {
        compress(Bitmap.CompressFormat.JPEG, 100, it)
    }

    return file.absolutePath
}