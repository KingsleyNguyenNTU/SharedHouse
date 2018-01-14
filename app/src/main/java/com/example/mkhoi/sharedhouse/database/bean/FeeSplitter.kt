package com.example.mkhoi.sharedhouse.database.bean

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import android.os.Parcel
import android.os.Parcelable
import com.example.mkhoi.sharedhouse.database.entity.FeeShare
import com.example.mkhoi.sharedhouse.database.entity.Person


class FeeSplitter(@Embedded var feeShare: FeeShare): Parcelable {
    @Relation(parentColumn = "personId", entityColumn = "id")
    var splitter: Person? = null

    constructor(parcel: Parcel) : this(feeShare = parcel.readParcelable(FeeShare::class.java.classLoader)) {
        splitter = parcel.readParcelable(Person::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(feeShare, flags)
        parcel.writeParcelable(splitter, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeeSplitter> {
        override fun createFromParcel(parcel: Parcel): FeeSplitter {
            return FeeSplitter(parcel)
        }

        override fun newArray(size: Int): Array<FeeSplitter?> {
            return arrayOfNulls(size)
        }
    }
}