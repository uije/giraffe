package com.zettafantasy.giraffe.feature.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.Record

class RecordViewModel(val repository: GiraffeRepository) : ViewModel() {

    val allRecords: LiveData<List<Record>> = repository.allRecords.asLiveData()

}

class RecordViewModelFactory(private val repository: GiraffeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}