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
    private val idMap = mutableMapOf<Int?, Need>()
    private val nameMap: Map<String, Need> by lazy {
        mutableMapOf<String, Need>().apply {
            list.forEach { item ->
                this[item.getName()] = item
            }
        }.toMap()
    }

    init {
        load(resources.getStringArray(R.array.need_0_survival), NeedType.SURVIVAL)
        load(resources.getStringArray(R.array.need_1_social), NeedType.SOCIAL)
        load(resources.getStringArray(R.array.need_2_strong), NeedType.STRONG)
        load(resources.getStringArray(R.array.need_3_freedom), NeedType.FREEDOM)
        load(resources.getStringArray(R.array.need_4_fun), NeedType.FUN)
        load(resources.getStringArray(R.array.need_5_meaning), NeedType.MEANING)
    }

    private fun load(stringArray: Array<String>, type: NeedType) {
        for (item in stringArray) {
            val need = Need(list.size, item, type)
            list.add(need)
            idMap[need.getId()] = need
        }
    }

    fun getList(): List<Need> = list.toList()

    fun getListByIds(ids: List<Int>) = ids.mapNotNull { idMap[it] }.toList()

    fun getNeedById(id: Int?) = idMap[id]

    fun getNeedByName(name: String?) = nameMap[name]
}