package com.zettafantasy.giraffe.common

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val KEY_SHOWN_COACH_MARK_FIND_EMOTION = "shownCoachMarkFindEmotion"
    private const val KEY_SHOWN_RECORD_INTRO = "shownRecordIntro"
    private const val KEY_SHOWN_START_BTN = "shownStartBtn"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    var shownCoachMarkStartBtn: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_START_BTN, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_START_BTN, value).commit()
        }

    var shownCoachMarkFindEmotion: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_COACH_MARK_FIND_EMOTION, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_COACH_MARK_FIND_EMOTION, value).commit()
        }

    var shownRecordIntro: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_RECORD_INTRO, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_RECORD_INTRO, value).commit()
        }

    fun clear(): Boolean {
        return preferences.edit().clear().commit()
    }
}