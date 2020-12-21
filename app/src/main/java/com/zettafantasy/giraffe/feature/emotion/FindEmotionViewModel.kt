package com.zettafantasy.giraffe.feature.emotion

import android.util.Log
import androidx.lifecycle.ViewModel

class FindEmotionViewModel : ViewModel() {
    companion object {
        const val TAG = "FindEmotionViewModel"
    }

    fun onCheck(item: EmotionItemViewModel) {
        Log.d(TAG, String.format("onClick %s", item))
        item.onCheck()

        //todo 표시
    }
}
