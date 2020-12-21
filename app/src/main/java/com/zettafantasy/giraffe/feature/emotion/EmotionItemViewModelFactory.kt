package com.zettafantasy.giraffe.feature.emotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zettafantasy.giraffe.model.Emotion
import java.lang.IllegalArgumentException

class EmotionItemViewModelFactory<T>(val emotion: Emotion) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmotionItemViewModel::class.java)) {
            return EmotionItemViewModel(emotion) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
