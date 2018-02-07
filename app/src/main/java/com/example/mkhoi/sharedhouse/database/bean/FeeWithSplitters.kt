package com.example.mkhoi.sharedhouse.database.bean

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import android.os.Parcel
import android.os.Parcelable
import com.example.mkhoi.sharedhouse.database.entity.Fee
import com.example.mkhoi.sharedhouse.database.entity.FeeShare

class FeeWithSplitters(@Embedded var fee: Fee): Parcelable {
    @Relation(parentColumn = "id", entityColumn = "feeId")
    var splitters: List<FeeShare>? = null

    constructor(parcel: Parcel) : this(fee = parcel.readParcelable(Fee::class.java.classLoader)) {
        splitters = parcel.createTypedArrayList(FeeShare)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(fee, flags)
        parcel.writeTypedList(splitters)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeeWithSplitters> {
        override fun createFromParcel(parcel: Parcel): FeeWithSplitters {
            return FeeWithSplitters(parcel)
        }

        override fun newArray(size: Int): Array<FeeWithSplitters?> {
            return arrayOfNulls(size)
        }
    }
}