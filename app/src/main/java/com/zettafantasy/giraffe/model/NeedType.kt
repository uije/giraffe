package com.zettafantasy.giraffe.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.zettafantasy.giraffe.R
import java.io.Serializable

@Keep
enum class NeedType(@StringRes val nameRes: Int) : Serializable {
    SURVIVAL(R.string.need_type_survival),
    SOCIAL(R.string.need_type_social),
    STRONG(R.string.need_type_strong),
    FREEDOM(R.string.need_type_freedom),
    FUN(R.string.need_type_fun),
    MEANING(R.string.need_type_meaning)
}