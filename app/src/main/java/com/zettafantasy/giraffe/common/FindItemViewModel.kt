package com.zettafantasy.giraffe.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FindItemViewModel : ViewModel() {
    private val list = mutableListOf<Item>()
    private val _selectedItems = MutableLiveData<List<Item>>(list)
    val selectedItems: LiveData<List<Item>>
        get() = _selectedItems

    fun toggle(item: Item): Int {
        Log.d(javaClass.simpleName, String.format("onClick %s", item))

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
