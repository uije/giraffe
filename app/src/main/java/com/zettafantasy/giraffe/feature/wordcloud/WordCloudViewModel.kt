package com.zettafantasy.giraffe.feature.wordcloud

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory

class WordCloudViewModel(
    val repository: GiraffeRepository,
    emotionInventory: EmotionInventory,
    needInventory: NeedInventory
) : ViewModel() {
    val name = "hi"
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