package com.example.mkhoi.sharedhouse.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons

@Dao
interface UnitPersonDao {

    @Query("select * from unit")
    fun getAllUnits(): List<UnitWithPersons>

    @Query("select * from unit where unit.active = :active")
    fun getAllActiveUnits(active: Boolean = true): LiveData<List<UnitWithPersons>>
}