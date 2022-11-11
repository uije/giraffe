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
    private val idMap = mutableMapOf<Int, Emotion>()
    private val nameMap: Map<String, Emotion> by lazy {
        mutableMapOf<String, Emotion>().apply {
            list.forEach { item ->
                this[item.getName()] = item
            }
        }.toMap()
    }

    init {
        load(resources.getStringArray(R.array.emotion_satisfied), EmotionType.SATISFIED)
        load(resources.getStringArray(R.array.emotion_unsatisfied), EmotionType.UNSATISFIED)
    }

    private fun load(stringArray: Array<String>, type: EmotionType) {
        for (item in stringArray) {
            val emotion = Emotion(list.size, item, type)
            list.add(emotion)
            idMap[emotion.getId()] = emotion
        }
    }

    fun getListByType(type: EmotionType) =
        if (type == EmotionType.UNCLEAR) list.toList() else list.filter { it.type == type }.toList()

    fun getListByIds(ids: List<Int>) = ids.mapNotNull { idMap[it] }.toList()

    fun getEmotionById(id: Int?) = idMap[id]

    fun getEmotionByName(name: String) = nameMap[name]
}