package com.example.mkhoi.sharedhouse.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons

@Dao
interface UnitPersonDao {
    @Query("select * from unit where unit.active = :active")
    fun getAllActiveUnits(active: Boolean = true): LiveData<List<UnitWithPersons>>
}