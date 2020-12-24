package com.zettafantasy.giraffe.model

import android.os.Parcel
import android.os.Parcelable

data class Emotion(
    val name: String
) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
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
