package com.example.mkhoi.sharedhouse.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.mkhoi.sharedhouse.database.bean.RoomSplitter
import com.example.mkhoi.sharedhouse.database.entity.FeeShare

@Dao
interface SplitterDao {
    @Insert
    fun insertFeeShares(feeShares: List<FeeShare>): List<Long>

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
}