package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.example.mkhoi.sharedhouse.database.bean.FeeType
import com.example.mkhoi.sharedhouse.database.bean.ReducedShareType
import com.example.mkhoi.sharedhouse.database.bean.ShareType

@Entity
data class Fee(@PrimaryKey(autoGenerate = true) var id: Int? = null,
               var name: String,
               var feeType: FeeType,
               var shareType: ShareType,
               var month: Int,
               var year: Int,
               var amount: Double): Parcelable {

    @Transient
    var displayFeeType: String = feeType.name.toLowerCase().capitalize()
        get(){
            field = feeType.name.toLowerCase().capitalize()
            return field
        }
        set(value){
            feeType = FeeType.valueOf(value.toUpperCase())
        }

    @Transient
    var displayShareType: String = ReducedShareType.ROOM.name.toLowerCase().capitalize()
        get(){
            field = when (isSharedByRoom()){
                true -> ReducedShareType.ROOM.name.toLowerCase().capitalize()
                false -> ReducedShareType.PERSON.name.toLowerCase().capitalize()
            }
            return field
        }
        set(value){
            val newShareType = ReducedShareType.valueOf(value.toUpperCase())
            shareType = when (newShareType){
                ReducedShareType.PERSON -> ShareType.SHARE_BY_PERSON_WITHOUT_TIME
                ReducedShareType.ROOM -> ShareType.SHARE_BY_ROOM_WITHOUT_TIME
            }
        }

    @Transient
    var displayAmount: String = ""
        get(){
            field = String.format("%.2f", amount)
            return field
        }
        set(value){
            if (value.isNotBlank()){
                amount = value.toDouble()
            }
        }

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