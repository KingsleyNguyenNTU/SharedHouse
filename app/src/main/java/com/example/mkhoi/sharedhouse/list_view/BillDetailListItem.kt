package com.example.mkhoi.sharedhouse.list_view

import android.os.Parcel
import android.os.Parcelable

data class BillDetailListItem(var mainName: String,
                              var amount: Float,
                              var roommates: MutableList<BillRoommateListItem>): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readFloat(),
            parcel.createTypedArrayList(BillRoommateListItem.CREATOR).toMutableList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mainName)
        parcel.writeFloat(amount)
        parcel.writeTypedList(roommates)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BillDetailListItem> {
        override fun createFromParcel(parcel: Parcel): BillDetailListItem {
            return BillDetailListItem(parcel)
        }

        override fun newArray(size: Int): Array<BillDetailListItem?> {
            return arrayOfNulls(size)
        }
    }
}