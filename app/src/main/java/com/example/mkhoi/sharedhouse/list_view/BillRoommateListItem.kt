package com.example.mkhoi.sharedhouse.list_view

import android.os.Parcel
import android.os.Parcelable

data class BillRoommateListItem(var mainName: String,
                                var amount: Float): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readFloat()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mainName)
        parcel.writeFloat(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BillRoommateListItem> {
        override fun createFromParcel(parcel: Parcel): BillRoommateListItem {
            return BillRoommateListItem(parcel)
        }

        override fun newArray(size: Int): Array<BillRoommateListItem?> {
            return arrayOfNulls(size)
        }
    }
}