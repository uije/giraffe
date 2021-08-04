package com.zettafantasy.giraffe.model

import android.os.Parcelable
import com.zettafantasy.giraffe.common.Item
import kotlinx.parcelize.Parcelize

@Parcelize
data class Emotion(
    private val id: Int,
    private val name: String,
    val type: EmotionType
) : Item, Parcelable {
    override fun getId(): Int {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getType(): Int {
        return type.nameRes
    }

    override fun toString(): String {
        return name
    }
}
