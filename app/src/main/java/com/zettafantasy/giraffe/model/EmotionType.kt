package com.zettafantasy.giraffe.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.zettafantasy.giraffe.R
import java.io.Serializable

@Keep
enum class EmotionType(@StringRes val nameRes: Int) : Serializable {
    SATISFIED(R.string.emotion_type_satisfied),
    UNSATISFIED(R.string.emotion_type_unsatisfied)
}
