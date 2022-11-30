package com.zettafantasy.giraffe.common.ext

import android.os.Handler
import android.os.Looper
import com.rizafu.coachmark.CoachMark
import com.zettafantasy.giraffe.GiraffeConstant

fun CoachMark.hideDelayed(
    delayMillis: Long = GiraffeConstant.HIDE_COACH_MARK_MILLIS,
    callback: (() -> Unit)? = null
) {
    Handler(Looper.getMainLooper()).postDelayed({
        dismiss()
        callback?.invoke()
    }, delayMillis)
}