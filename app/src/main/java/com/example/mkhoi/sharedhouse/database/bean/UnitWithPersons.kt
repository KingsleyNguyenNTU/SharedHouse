package com.example.mkhoi.sharedhouse.database.bean

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import android.content.Context
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.example.mkhoi.sharedhouse.database.BackgroundAsyncTask
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit
import com.example.mkhoi.sharedhouse.util.combineProfilePictures
import com.example.mkhoi.sharedhouse.util.getProfilePicture

class UnitWithPersons(@Embedded var unit: Unit): Parcelable {
    @Relation(parentColumn = "id", entityColumn = "unitId")
    var roommates: List<Person>? = null
        get() = field?.filter { it.active }

    constructor(parcel: Parcel) : this(unit = parcel.readParcelable(Unit::class.java.classLoader)) {
        roommates = parcel.createTypedArrayList(Person)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(unit, flags)
        parcel.writeTypedList(roommates)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UnitWithPersons> {
        override fun createFromParcel(parcel: Parcel): UnitWithPersons {
            return UnitWithPersons(parcel)
        }

        override fun newArray(size: Int): Array<UnitWithPersons?> {
            return arrayOfNulls(size)
        }
    }

    fun getProfilePictureLiveData(context: Context, uriLiveData: MutableLiveData<Uri?>){
        BackgroundAsyncTask().execute({
            uriLiveData.postValue(this.getProfilePicture(context))
        })
    }

    private fun getProfilePicture(context: Context) =
            roommates?.map { it.getProfilePicture(context) }?.toList()?.combineProfilePictures(context)

}
