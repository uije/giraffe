package com.zettafantasy.giraffe.common.item

interface Item {
    fun getId(): Int

    fun getName(): String

    fun getType(): Int
}
