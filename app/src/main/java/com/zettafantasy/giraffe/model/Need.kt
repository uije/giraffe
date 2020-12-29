package com.zettafantasy.giraffe.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.zettafantasy.giraffe.common.Item

@Entity
data class Need(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo private val name: String,
    @ColumnInfo var type: NeedType,
) : Item, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString()!!,
        NeedType.values()[parcel.readInt()]
    )

    override fun getName(): String {
        return name
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

    enum class NeedType {
        CONNECTION,
        PHYSICAL_WELLBEING,
        HONESTY,
        PLAY,
        PEACE,
        AUTONOMY,
        MEANING
    }
}
