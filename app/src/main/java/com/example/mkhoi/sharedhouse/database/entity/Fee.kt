package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.example.mkhoi.sharedhouse.database.bean.FeeType
import com.example.mkhoi.sharedhouse.database.bean.ShareType

@Entity
data class Fee(@PrimaryKey(autoGenerate = true) var id: Int? = null,
               var name: String,
               var feeType: FeeType,
               var shareType: ShareType,
               var month: Int,
               var year: Int,
               var amount: Double): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            FeeType.valueOf(parcel.readString()),
            ShareType.valueOf(parcel.readString()),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(feeType.toString())
        parcel.writeString(shareType.toString())
        parcel.writeInt(month)
        parcel.writeInt(year)
        parcel.writeDouble(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Fee> {
        override fun createFromParcel(parcel: Parcel): Fee {
            return Fee(parcel)
        }

        override fun newArray(size: Int): Array<Fee?> {
            return arrayOfNulls(size)
        }
    }

    fun isSharedByRoom(): Boolean{
        return (shareType == ShareType.SHARE_BY_ROOM_WITH_TIME ||
                shareType == ShareType.SHARE_BY_ROOM_WITHOUT_TIME)
    }
}