package com.zettafantasy.giraffe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zettafantasy.giraffe.common.DestinationScreen
import com.zettafantasy.giraffe.common.SingleLiveEvent

class MainViewModel : ViewModel() {
    var highLightItemId: Long? = null
    var destinationScreen: DestinationScreen? = null

    private val _shareClickEvent = SingleLiveEvent<Any>()
    val shareClickEvent: LiveData<Any>
        get() = _shareClickEvent

    fun callShareClickEvent() {
        _shareClickEvent.call()
    }

    private val _shareEmotionEvent = SingleLiveEvent<Any>()
    val shareEmotionEvent: LiveData<Any>
        get() = _shareEmotionEvent

    fun callShareEmotionEvent() {
        _shareEmotionEvent.call()
    }

    private val _shareNeedEvent = SingleLiveEvent<Any>()
    val shareNeedEvent: LiveData<Any>
        get() = _shareNeedEvent

    fun callShareNeedEvent() {
        _shareNeedEvent.call()
    }
}