package com.example.mkhoi.sharedhouse.database.bean

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit

class UnitWithPersons(@Embedded var unit: Unit) {
    @Relation(parentColumn = "id", entityColumn = "unitId")
    var roommates: List<Person>? = null
}
