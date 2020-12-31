package com.zettafantasy.giraffe.data

import android.content.res.Resources
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.model.Need
import com.zettafantasy.giraffe.model.NeedType

class NeedInventory private constructor(resources: Resources) {

    companion object {
        @Volatile
        private var INSTANCE: NeedInventory? = null

        @JvmStatic
        fun getInstance(resources: Resources): NeedInventory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NeedInventory(resources).also {
                    INSTANCE = it
                }
            }
    }

    private val list = mutableListOf<Need>()

    init {
        load(resources.getStringArray(R.array.need_0_autonomy), NeedType.AUTONOMY)
        load(resources.getStringArray(R.array.need_1_physical_wb), NeedType.PHYSICAL_WB)
        load(resources.getStringArray(R.array.need_2_connection), NeedType.CONNECTION)
        load(resources.getStringArray(R.array.need_3_play), NeedType.PLAY)
        load(resources.getStringArray(R.array.need_4_meaning), NeedType.MEANING)
        load(resources.getStringArray(R.array.need_5_truth), NeedType.TRUTH)
        load(resources.getStringArray(R.array.need_6_peace), NeedType.PEACE)
        load(
            resources.getStringArray(R.array.need_7_self_realization),
            NeedType.SELF_REALIZATION
        )
    }

    private fun load(stringArray: Array<String>, type: NeedType) {
        for (item in stringArray) {
            list.add(Need(list.size, item, type))
        }
    }

    fun getList(): List<Need> = list.toList()
}