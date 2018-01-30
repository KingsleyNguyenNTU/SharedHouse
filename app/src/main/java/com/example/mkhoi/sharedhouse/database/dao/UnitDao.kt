package com.example.mkhoi.sharedhouse.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.example.mkhoi.sharedhouse.database.entity.Unit

@Dao
interface UnitDao {
    @Insert
    fun insertUnit(unit: Unit): Long

    @Update
    fun updateUnit(unit: Unit)

    @Query("select * from unit where active = :active")
    fun getAllActiveUnits(active: Boolean = true): LiveData<List<Unit>>
}