package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity
class FeePrepaid(@PrimaryKey(autoGenerate = true) var id: Int? = null,
                 var feeId: Int,
                 var personId: Int,
                 var amount: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeInt(feeId)
        parcel.writeInt(personId)
        parcel.writeDouble(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeePrepaid> {
        override fun createFromParcel(parcel: Parcel): FeePrepaid {
            return FeePrepaid(parcel)
        }

        override fun newArray(size: Int): Array<FeePrepaid?> {
            return arrayOfNulls(size)
        }
    }
}