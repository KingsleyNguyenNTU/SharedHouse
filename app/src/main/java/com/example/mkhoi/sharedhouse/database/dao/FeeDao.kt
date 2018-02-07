package com.example.mkhoi.sharedhouse.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import com.example.mkhoi.sharedhouse.database.entity.Fee

@Dao
interface FeeDao {
    @Insert
    fun insertFee(fee: Fee): Long

    @Update
    fun updateFee(fee: Fee)

    @Delete
    fun deleteFee(fee: Fee)
}