package com.zettafantasy.giraffe.feature.record

import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.model.Emotion
import com.zettafantasy.giraffe.model.Need
import java.util.*

data class RecordWrapper(
    val record: Record,
    val emotionInventory: EmotionInventory,
    val needInventory: NeedInventory
) {
    val emotions: List<Emotion?> = emotionInventory.getListByIds(record.emotionIds)
    val needs: List<Need?> = needInventory.getListByIds(record.needIds)

    val date: Date
        get() = record.date

    val id: Long?
        get() = record.id

    val stimulus: String
        get() = record.stimulus
}