package com.zettafantasy.giraffe.data

import android.content.res.Resources
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.model.Emotion
import com.zettafantasy.giraffe.model.EmotionType

class EmotionInventory private constructor(resources: Resources) {

    companion object {
        @Volatile
        private var INSTANCE: EmotionInventory? = null

        @JvmStatic
        fun getInstance(resources: Resources): EmotionInventory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: EmotionInventory(resources).also {
                    INSTANCE = it
                }
            }
    }

    private val list = mutableListOf<Emotion>()

    init {
        load(resources.getStringArray(R.array.emotion_satisfied), EmotionType.SATISFIED)
        load(resources.getStringArray(R.array.emotion_unsatisfied), EmotionType.UNSATISFIED)
    }

    private fun load(stringArray: Array<String>, type: EmotionType) {
        for (item in stringArray) {
            list.add(Emotion(list.size, item, type))
        }
    }

    fun getList(type: EmotionType) = list.filter { it.type == type }.toList()
}