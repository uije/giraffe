package com.zettafantasy.giraffe.feature.record

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory

class RecordViewModel(
    val repository: GiraffeRepository,
    emotionInventory: EmotionInventory,
    needInventory: NeedInventory
) : ViewModel() {

    val allRecords: LiveData<List<RecordWrapper>> =
        Transformations.map(repository.allRecords.asLiveData()) { data ->
            Log.d(javaClass.simpleName, String.format("uije %s", data.isEmpty()))
            data.map { RecordWrapper(it, emotionInventory, needInventory) }.toList()
        }
}

class RecordViewModelFactory(
    private val repository: GiraffeRepository,
    private val resources: Resources
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordViewModel(
                repository,
                EmotionInventory.getInstance(resources),
                NeedInventory.getInstance(resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}