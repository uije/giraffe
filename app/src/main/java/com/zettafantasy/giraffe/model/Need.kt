package com.zettafantasy.giraffe.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zettafantasy.giraffe.common.Item

@Entity
data class Need(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo private val name: String,
) : Item, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString()!!,
    )

    override fun getName(): String {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
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
