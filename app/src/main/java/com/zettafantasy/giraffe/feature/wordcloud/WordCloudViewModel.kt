package com.zettafantasy.giraffe.feature.wordcloud

import android.content.res.Resources
import androidx.lifecycle.*
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.feature.record.RecordWrapper
import com.zettafantasy.giraffe.model.Emotion
import net.alhazmy13.wordcloud.WordCloud
import java.util.*
import kotlin.collections.map
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toList

class WordCloudViewModel(
    private val repository: GiraffeRepository,
    private val emotionInventory: EmotionInventory,
    private val needInventory: NeedInventory
) : ViewModel() {

    private val _emotions = MutableLiveData<List<WordCloud>>()
    val emotions: LiveData<List<WordCloud>>
        get() = _emotions

    fun load(viewLifecycleOwner: LifecycleOwner) {
        val emotionMap = mutableMapOf<Emotion, WordCloud>()

        Transformations.map(repository.findRecordsSince(oneMonthAgo()).asLiveData()) { data ->
            data.map { RecordWrapper(it, emotionInventory, needInventory) }.toList()
        }.observe(viewLifecycleOwner) { records ->
            for (record in records) {
                for (emotion in record.emotions) {
                    if (emotionMap.containsKey(emotion)) {
                        emotionMap[emotion]?.weight = emotionMap[emotion]!!.weight + 1
                    } else {
                        emotionMap[emotion!!] = WordCloud(emotion.getName(), 1)
                    }
                }
            }

            _emotions.value = emotionMap.values.toList()
        }
    }

    private fun oneMonthAgo(): Long {
        return Calendar.getInstance().run {
            add(Calendar.MONTH, -1)
            timeInMillis
        }
    }
}

class WordCloudViewModelFactory(
    private val repository: GiraffeRepository,
    private val resources: Resources
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordCloudViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordCloudViewModel(
                repository,
                EmotionInventory.getInstance(resources),
                NeedInventory.getInstance(resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}