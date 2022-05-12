package com.zettafantasy.giraffe.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun Bitmap.save(context: Context): File? {
    // path to /data/data/yourapp/app_data/imageDir
    // Create imageDir
    val directory = context.externalCacheDir ?: context.cacheDir
    val images = File(directory, "images")
    if (!images.exists()) {
        images.mkdir()
    }

    val file = File(images, "wordcloud_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use {
        compress(Bitmap.CompressFormat.JPEG, 100, it)
    }

    Log.d("Share Image", "Image created ${file.absolutePath}")
    return file
}

fun File.getUri(context: Context): Uri? =
    try {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            this
        ).also {
            Log.d("GetUri", "Uri created $it")
        }
    } catch (e: Exception) {
        Log.e(
            "GetUri",
            "Uri can't be created: $this"
        )
        null
    }

fun Uri.getShareIntent() = Intent().also {
    it.action = Intent.ACTION_SEND
    it.putExtra(Intent.EXTRA_STREAM, this)
    it.type = "image/jpeg"
}

fun Context.grantUriPermission(intent: Intent, uri: Uri) {
    val resInfoList: List<ResolveInfo> =
        packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
    for (resolveInfo in resInfoList) {
        val packageName: String = resolveInfo.activityInfo.packageName
        grantUriPermission(
            packageName,
            uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}