package com.example.mkhoi.sharedhouse.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons

@Dao
interface UnitPersonDao {
    @Query("select * from unit")
    fun getAllActiveUnits(): LiveData<List<UnitWithPersons>>
}