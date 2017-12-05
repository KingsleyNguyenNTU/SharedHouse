package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity
data class Unit(@PrimaryKey(autoGenerate = true) var id: Int? = null,
                var name: String,
                var active: Boolean = true): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeByte(if (active) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Unit> {
        override fun createFromParcel(parcel: Parcel): Unit {
            return Unit(parcel)
        }

        override fun newArray(size: Int): Array<Unit?> {
            return arrayOfNulls(size)
        }
    }
}