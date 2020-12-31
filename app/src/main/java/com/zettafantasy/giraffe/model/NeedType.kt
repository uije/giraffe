package com.zettafantasy.giraffe.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
enum class NeedType : Serializable {
    CONNECTION,
    PHYSICAL_WB,
    TRUTH,
    PLAY,
    PEACE,
    AUTONOMY,
    MEANING,
    SELF_REALIZATION
}