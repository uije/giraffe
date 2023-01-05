package com.zettafantasy.giraffe.common

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.zettafantasy.giraffe.feature.remind.RemindNotificationType
import com.zettafantasy.giraffe.feature.wordcloud.WordCloudPeriod

object Preferences {
    private const val KEY_SHOWN_COACH_MARK_SWIPE_CARD = "shownCoachMarkSwipeCard"
    private const val KEY_SHOWN_RECORD_INTRO = "shownRecordIntro"
    private const val KEY_SHOWN_START_BTN = "shownStartBtn"
    private const val KEY_SHOWN_COACH_MARK_PROGRESSBAR = "shownCoachMarkProgressBar"
    private const val KEY_SHOWN_COACH_MARK_COMPLETE = "shownCoachMarkComplete"
    private const val KEY_LAST_USED_TIME = "lastUsedTime"
    private const val KEY_WORD_CLOUD_PERIOD = "wordCloudPeriod"
    private const val KEY_DEFAULT_SCREEN = "defaultScreen"
    private const val KEY_SHOWN_CELEBRATE_SCREEN = "shownCelebrateScreen"
    private const val KEY_LAST_REMIND_TYPE = "lastRemindType"
    private const val KEY_SHOWN_ONBOARD_SCREEN = "shownOnboardScreen"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    var shownCoachMarkStartBtn: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_START_BTN, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_START_BTN, value).commit()
        }

    var shownCoachMarkSwipeCard: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_COACH_MARK_SWIPE_CARD, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_COACH_MARK_SWIPE_CARD, value).commit()
        }

    var shownCoachMarkProgressBar: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_COACH_MARK_PROGRESSBAR, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_COACH_MARK_PROGRESSBAR, value).commit()
        }

    var shownCoachMarkComplete: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_COACH_MARK_COMPLETE, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_COACH_MARK_COMPLETE, value).commit()
        }

    var shownRecordIntro: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_RECORD_INTRO, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_RECORD_INTRO, value).commit()
        }

    var lastUsedTime: Long
        get() = preferences.getLong(KEY_LAST_USED_TIME, 0)
        set(value) {
            preferences.edit().putLong(KEY_LAST_USED_TIME, value).commit()
        }

    var wordCloudPeriod: WordCloudPeriod
        get() = WordCloudPeriod.values()[preferences.getInt(
            KEY_WORD_CLOUD_PERIOD,
            WordCloudPeriod.RECENT_MONTH.ordinal
        )]
        set(value) {
            preferences.edit().putInt(KEY_WORD_CLOUD_PERIOD, value.ordinal).commit()
        }

    var defaultScreen: Int
        get() = preferences.getInt(KEY_DEFAULT_SCREEN, 0)
        set(value) {
            preferences.edit().putInt(KEY_DEFAULT_SCREEN, value).commit()
        }

    var shownCelebrateScreen: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_CELEBRATE_SCREEN, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_CELEBRATE_SCREEN, value).commit()
        }

    var shownOnboardScreen: Boolean
        get() = preferences.getBoolean(KEY_SHOWN_ONBOARD_SCREEN, false)
        set(value) {
            preferences.edit().putBoolean(KEY_SHOWN_ONBOARD_SCREEN, value).commit()
        }

    var lastRemindType: RemindNotificationType
        get() = RemindNotificationType.values()[preferences.getInt(
            KEY_LAST_REMIND_TYPE,
            RemindNotificationType.DEFAULT.ordinal
        )]
        set(value) {
            preferences.edit().putInt(KEY_LAST_REMIND_TYPE, value.ordinal).commit()
        }

    fun clear(): Boolean {
        return preferences.edit().clear().commit()
    }
}