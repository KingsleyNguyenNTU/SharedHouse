package com.example.mkhoi.sharedhouse.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import com.example.mkhoi.sharedhouse.database.bean.PersonSplitter
import com.example.mkhoi.sharedhouse.database.bean.RoomSplitter
import com.example.mkhoi.sharedhouse.database.entity.FeeShare

@Dao
interface SplitterDao {
    @Insert
    fun insertFeeShares(feeShares: List<FeeShare>): List<Long>

    @Delete
    fun deleteSplitters(splitters: List<FeeShare>)

    @Query("delete from feeShare where feeId = :feeId")
    fun deleteSplittersByFeeId(feeId: Int)

    @Query("select * from fee where month = :month and year = :year")
    fun getAllFeesFromMonth(month: Int, year: Int): LiveData<List<FeeWithSplitters>>

    @Query("select * from fee where month = :month and year = :year")
    fun getAllFeesFromMonthStatic(month: Int, year: Int): List<FeeWithSplitters>

    @Query("select unit.*, " +
            "FeeShare.id as FeeShare_id, " +
            "FeeShare.feeId as FeeShare_feeId, " +
            "FeeShare.personId as FeeShare_personId, " +
            "FeeShare.unitId as FeeShare_unitId, " +
            "FeeShare.share as FeeShare_share " +
            "from Unit left join FeeShare " +
            "on unit.id = FeeShare.unitId and FeeShare.feeId = :feeId " +
            "where Unit.active = :active")
    fun getAllRoomSplitters(feeId: Int?, active: Boolean = true): List<RoomSplitter>

    @Query("select person.*, " +
            "FeeShare.id as FeeShare_id, " +
            "FeeShare.feeId as FeeShare_feeId, " +
            "FeeShare.personId as FeeShare_personId, " +
            "FeeShare.unitId as FeeShare_unitId, " +
            "FeeShare.share as FeeShare_share " +
            "from Person left join FeeShare " +
            "on person.id = FeeShare.personId and FeeShare.feeId = :feeId " +
            "where person.active = :active")
    fun getAllPersonSplitters(feeId: Int?, active: Boolean = true): List<PersonSplitter>
}