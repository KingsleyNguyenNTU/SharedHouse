package com.example.mkhoi.sharedhouse.database.bean

import android.arch.persistence.room.Embedded
import com.example.mkhoi.sharedhouse.database.entity.FeeShare
import com.example.mkhoi.sharedhouse.database.entity.Person

class PersonSplitter(@Embedded var person: Person) {
    @Embedded(prefix = "FeeShare_")
    var feeShare: FeeShare?=null
}