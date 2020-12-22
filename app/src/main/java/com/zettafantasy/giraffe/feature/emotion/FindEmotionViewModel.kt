package com.zettafantasy.giraffe.feature.emotion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zettafantasy.giraffe.model.Emotion

class FindEmotionViewModel : ViewModel() {
    companion object {
        const val TAG = "FindEmotionViewModel"
    }

    private val list = mutableListOf<Emotion>()
    private val _selectedItems = MutableLiveData<List<Emotion>>(list)
    val selectedItems: LiveData<List<Emotion>>
        get() = _selectedItems

    fun onCheck(item: Emotion) {
        Log.d(TAG, String.format("onClick %s", item))

        if (list.contains(item)) {
            list.remove(item)
        } else {
            list.add(item)
        }

        _selectedItems.value = list
    }
}
