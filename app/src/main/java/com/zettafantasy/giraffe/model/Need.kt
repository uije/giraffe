package com.zettafantasy.giraffe.model

import android.os.Parcel
import android.os.Parcelable
import com.zettafantasy.giraffe.common.Item

data class Need(
    val id: Int?,
    private val name: String,
    val type: NeedType,
) : Item, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()!!,
        NeedType.values()[parcel.readInt()]
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

    companion object CREATOR : Parcelable.Creator<Need> {
        override fun createFromParcel(parcel: Parcel): Need {
            return Need(parcel)
        }

        override fun newArray(size: Int): Array<Need?> {
            return arrayOfNulls(size)
        }
    }

}
