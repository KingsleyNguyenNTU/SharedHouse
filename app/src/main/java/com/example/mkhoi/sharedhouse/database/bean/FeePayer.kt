package com.example.mkhoi.sharedhouse.database.bean

import android.arch.persistence.room.Embedded
import com.example.mkhoi.sharedhouse.database.entity.FeePrepaid
import com.example.mkhoi.sharedhouse.database.entity.Person

class FeePayer(@Embedded var person: Person) {
    @Embedded(prefix = "FeePrepaid_")
    var feePrepaid: FeePrepaid?=null
}