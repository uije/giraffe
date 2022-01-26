package com.zettafantasy.giraffe.feature.wordcloud

import com.zettafantasy.giraffe.R
import java.util.*

enum class WordCloudPeriod(val desc: Int) {
    RECENT_YEAR(R.string.recent_year),
    RECENT_3MONTH(R.string.recent_3month),
    RECENT_MONTH(R.string.recent_month),
    RECENT_WEEK(R.string.recent_week);

    fun getTime(): Long {
        return Calendar.getInstance().run {
            when (this@WordCloudPeriod) {
                RECENT_YEAR -> add(Calendar.YEAR, -1)
                RECENT_3MONTH -> add(Calendar.MONTH, -3)
                RECENT_MONTH -> add(Calendar.MONTH, -1)
                RECENT_WEEK -> add(Calendar.DAY_OF_YEAR, -7)
            }
            timeInMillis
        }
    }
}