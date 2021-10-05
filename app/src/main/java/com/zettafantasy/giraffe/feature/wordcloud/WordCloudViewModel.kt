package com.zettafantasy.giraffe.feature.wordcloud

import android.content.res.Resources
import androidx.lifecycle.*
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.feature.record.RecordWrapper
import net.alhazmy13.wordcloud.WordCloud
import java.util.*

class WordCloudViewModel(
    private val repository: GiraffeRepository,
    private val emotionInventory: EmotionInventory,
    private val needInventory: NeedInventory
) : ViewModel() {

    private val _emotions = MutableLiveData<List<WordCloud>>()
    val emotions: LiveData<List<WordCloud>>
        get() = _emotions

    private val _needs = MutableLiveData<List<WordCloud>>()
    val needs: LiveData<List<WordCloud>>
        get() = _needs

    fun load(viewLifecycleOwner: LifecycleOwner) {
        Transformations.map(repository.findRecordsSince(oneMonthAgo()).asLiveData()) { data ->
            data.map { RecordWrapper(it, emotionInventory, needInventory) }.toList()
        }.observe(viewLifecycleOwner) { records ->

            _emotions.value =
                records.map { it.emotions }.flatten().groupingBy { it }.eachCount().toList()
                    .map { WordCloud(it.first?.getName(), it.second) }

            _needs.value =
                records.map { it.needs }.flatten().groupingBy { it }.eachCount().toList()
                    .map { WordCloud(it.first?.getName(), it.second) }

            //Log.d(javaClass.simpleName, _emotions.value.toString())
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