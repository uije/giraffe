package com.zettafantasy.giraffe.feature.need

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zettafantasy.giraffe.model.Need

class FindNeedViewModel : ViewModel() {
    companion object {
        const val TAG = "FindNeedViewModel"
    }

    private val list = mutableListOf<Need>()
    private val _selectedItems = MutableLiveData<List<Need>>(list)
    val selectedItems: LiveData<List<Need>>
        get() = _selectedItems

    fun toggle(item: Need): Int {
        Log.d(TAG, String.format("onClick %s", item))

        if (list.contains(item)) {
            list.remove(item)
        } else {
            list.add(item)
        }

        _selectedItems.value = list
        return list.indexOf(item)
    }

    fun hasItem(): Boolean {
        return list.isNotEmpty()
    }
}
