package com.zettafantasy.giraffe.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.*
import android.net.Uri
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.content.FileProvider
import com.zettafantasy.giraffe.R
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

fun Context.drawAppSignature(canvas: Canvas, bitmap: Bitmap) {
    val appIcon = resources.getDrawable(R.mipmap.ic_launcher, null)
    val padding = resources.getDimensionPixelSize(R.dimen.app_icon_padding)
    appIcon.setBounds(
        bitmap.width - appIcon.intrinsicWidth - padding,
        bitmap.height - appIcon.intrinsicHeight - padding,
        bitmap.width - padding,
        bitmap.height - padding
    )
    appIcon.draw(canvas)

    val appName = resources.getString(R.string.app_name_for_share)
    val paint = Paint().apply {
        color = Color.BLACK
        textSize = resources.getDimensionPixelSize(R.dimen.app_name_size).toFloat()
    }
    val textBounds = Rect().also {
        paint.getTextBounds(appName, 0, appName.length, it)
    }
    canvas.drawText(
        appName,
        (bitmap.width - appIcon.intrinsicWidth - padding * 2 - textBounds.width()).toFloat(),
        (bitmap.height - textBounds.height()).toFloat(), paint
    )
}

fun Context.decorateForShare(bitmap: Bitmap, @ColorInt bgColor: Int): Bitmap? {
    return Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config).also {
        with(Canvas(it)) {
            drawColor(bgColor)
            drawBitmap(bitmap, 0f, 0f, null)
            drawAppSignature(this, bitmap)
        }
    }
}

fun File.getShareIntent(context: Context): Intent? = getUri(context)?.let {
    val intent = Intent.createChooser(it.getShareIntent(), context.getText(R.string.share))
    context.grantUriPermission(intent, it)
    intent
}