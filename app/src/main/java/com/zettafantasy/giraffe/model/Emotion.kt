package com.zettafantasy.giraffe.model

import android.os.Parcel
import android.os.Parcelable
import com.zettafantasy.giraffe.common.Item

data class Emotion(
    val id: Int?,
    private val name: String,
    val type: EmotionType
) : Item, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()!!,
        EmotionType.values()[parcel.readInt()]
    )

    override fun getName(): String {
        return name
    }

    override fun getType(): Int {
        return type.nameRes
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeInt(type.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Emotion> {
        override fun createFromParcel(parcel: Parcel): Emotion {
            return Emotion(parcel)
        }

        override fun newArray(size: Int): Array<Emotion?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return name
    }
}
