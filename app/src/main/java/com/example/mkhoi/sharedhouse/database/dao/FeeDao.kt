package com.example.mkhoi.sharedhouse.database.dao

import android.arch.persistence.room.*
import com.example.mkhoi.sharedhouse.database.bean.FeePayer
import com.example.mkhoi.sharedhouse.database.bean.PersonSplitter
import com.example.mkhoi.sharedhouse.database.entity.Fee
import com.example.mkhoi.sharedhouse.database.entity.FeePrepaid

@Dao
interface FeeDao {
    @Insert
    fun insertFee(fee: Fee): Long

    @Update
    fun updateFee(fee: Fee)

    @Delete
    fun deleteFee(fee: Fee)

    @Query("select person.*, " +
            "FeePrepaid.id as FeePrepaid_id, " +
            "FeePrepaid.feeId as FeePrepaid_feeId, " +
            "FeePrepaid.personId as FeePrepaid_personId, " +
            "FeePrepaid.amount as FeePrepaid_amount " +
            "from Person left join FeePrepaid " +
            "on person.id = FeePrepaid.personId and FeePrepaid.feeId = :feeId " +
            "where person.active = :active")
    fun getAllFeePayers(feeId: Int?, active: Boolean = true): List<FeePayer>

    @Insert
    fun insertFeePrepaids(feeShares: List<FeePrepaid>): List<Long>

    @Query("delete from feePrepaid where feeId = :feeId")
    fun deleteFeePayersByFeeId(feeId: Int)
}