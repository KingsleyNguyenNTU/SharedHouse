package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity
data class FeeShare(@PrimaryKey(autoGenerate = true) var id: Int? = null,
                    var feeId: Int,
                    var personId: Int,
                    var share: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeInt(feeId)
        parcel.writeInt(personId)
        parcel.writeInt(share)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeeShare> {
        override fun createFromParcel(parcel: Parcel): FeeShare {
            return FeeShare(parcel)
        }

        override fun newArray(size: Int): Array<FeeShare?> {
            return arrayOfNulls(size)
        }
    }
}