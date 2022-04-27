package com.zettafantasy.giraffe.feature.remind

import android.content.Context
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.DestinationScreen
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.model.Emotion
import com.zettafantasy.giraffe.model.Need

enum class RemindNotificationType(val title: Int, val text: Int, val dest: DestinationScreen) {
    DEFAULT(
        R.string.remind_noti_default_title,
        R.string.remind_noti_default_text,
        DestinationScreen.RECORD
    ),
    EMOTION(
        R.string.remind_noti_emotion_title,
        R.string.remind_noti_emotion_text,
        DestinationScreen.INSIGHT_EMOTION
    ),
    NEED(
        R.string.remind_noti_need_title,
        R.string.remind_noti_need_text,
        DestinationScreen.INSIGHT_NEED
    )
}

fun RemindNotificationType.getDecoratedTitle(context: Context, records: List<Record>?) =
    when (this) {
        RemindNotificationType.DEFAULT -> context.resources.getString(RemindNotificationType.DEFAULT.title)
        RemindNotificationType.EMOTION -> context.resources.getString(
            RemindNotificationType.EMOTION.title,
            getMostCommonEmotion(context, records!!)?.getName() ?: "?"
        )
        RemindNotificationType.NEED -> context.resources.getString(
            RemindNotificationType.NEED.title,
            getMostCommonNeed(context, records!!)?.getName() ?: "?"
        )
    }

private fun getMostCommonEmotion(
    context: Context,
    records: List<Record>
): Emotion? = EmotionInventory.getInstance(context.resources)
    .getEmotionById(
        records.map { it.emotionIds }.flatten().groupingBy { it }.eachCount().toList()
            .maxByOrNull { it.second }?.first
    )

private fun getMostCommonNeed(
    context: Context,
    records: List<Record>
): Need? = NeedInventory.getInstance(context.resources)
    .getNeedById(
        records.map { it.needIds }.flatten().groupingBy { it }.eachCount().toList()
            .maxByOrNull { it.second }?.first
    )
