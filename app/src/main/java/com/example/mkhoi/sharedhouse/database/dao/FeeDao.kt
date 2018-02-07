package com.example.mkhoi.sharedhouse.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.mkhoi.sharedhouse.database.entity.Fee

@Dao
interface FeeDao {
    @Insert
    fun insertFee(fee: Fee): Long

    @Query("select * from fee where month = :month and year = :year")
    fun getAllFeesFromMonth(month: Int, year: Int): LiveData<List<Fee>>
}