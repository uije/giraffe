package com.zettafantasy.giraffe.feature.record

import android.content.res.Resources
import androidx.lifecycle.*
import androidx.paging.*
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecordViewModel(
    private val repository: GiraffeRepository,
    emotionInventory: EmotionInventory,
    needInventory: NeedInventory,
    highLightItemId: Long?
) : ViewModel() {

    val allRecords: Flow<PagingData<RecordWrapper>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = true,
            maxSize = 200
        )
    ) { repository.getPagingRecords() }.flow
        .map { pagingData ->
            pagingData.map { record ->
                RecordWrapper(
                    record,
                    emotionInventory,
                    needInventory,
                    highLightItemId == record.id
                )
            }
        }.cachedIn(viewModelScope)
}

class RecordViewModelFactory(
    private val repository: GiraffeRepository,
    private val resources: Resources,
    private val highLightItemId: Long?
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordViewModel(
                repository,
                EmotionInventory.getInstance(resources),
                NeedInventory.getInstance(resources),
                highLightItemId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}