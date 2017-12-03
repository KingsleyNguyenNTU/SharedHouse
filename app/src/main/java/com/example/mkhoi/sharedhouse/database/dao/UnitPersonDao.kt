package com.example.mkhoi.sharedhouse.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons

@Dao
interface UnitPersonDao {
    @Query("select * from unit")
    public fun getAllActiveUnits(): List<UnitWithPersons>
}