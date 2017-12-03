package com.example.mkhoi.sharedhouse.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import com.example.mkhoi.sharedhouse.database.entity.Unit

@Dao
interface UnitDao {
    @Insert
    fun insertUnit(unit: Unit): Long
}