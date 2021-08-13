package com.zettafantasy.giraffe.common

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val KEY_SHOWN_COACH_MARK_FIND_EMOTION = "shownCoachMarkFindEmotion"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    var shownCoachMarkFindEmotion: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_COACH_MARK_FIND_EMOTION, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_COACH_MARK_FIND_EMOTION, value).commit()
        }
}