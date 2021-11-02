package com.zettafantasy.giraffe.feature.wordcloud

import android.content.res.Resources
import androidx.lifecycle.*
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.feature.record.RecordWrapper
import net.alhazmy13.wordcloud.WordCloud

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

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun load(viewLifecycleOwner: LifecycleOwner) {
        _isLoading.value = true
        Transformations.map(
            repository.findRecordsSince(Preferences.wordCloudPeriod.getTime()).asLiveData()
        ) { data ->
            data.map { RecordWrapper(it, emotionInventory, needInventory) }.toList()
        }.observe(viewLifecycleOwner) { records ->

            _emotions.value =
                records.map { it.emotions }.flatten().groupingBy { it }.eachCount().toList()
                    .map { WordCloud(it.first?.getName(), it.second) }

            _needs.value =
                records.map { it.needs }.flatten().groupingBy { it }.eachCount().toList()
                    .map { WordCloud(it.first?.getName(), it.second) }

            _isLoading.value = false
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