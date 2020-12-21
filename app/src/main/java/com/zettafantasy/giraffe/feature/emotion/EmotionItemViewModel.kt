package com.zettafantasy.giraffe.feature.emotion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zettafantasy.giraffe.model.Emotion

class EmotionItemViewModel(
    argEmotion: Emotion, argChecked: Boolean = false
) : ViewModel() {
    private val _checked = MutableLiveData(argChecked)
    val checked: LiveData<Boolean>
        get() = _checked

    private val _emotion = MutableLiveData(argEmotion)
    val emotion: LiveData<Emotion>
        get() = _emotion

    fun onCheck() {
        _checked.value = !_checked?.value!!

        Log.d(this.javaClass.simpleName, String.format("%s", checked.value))
    }
}