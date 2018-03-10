package com.example.mkhoi.sharedhouse.fees_view

import android.arch.persistence.room.Transaction
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import com.example.mkhoi.sharedhouse.database.dao.FeeDao
import com.example.mkhoi.sharedhouse.database.dao.SplitterDao
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeesRepository @Inject constructor(private val splitterDao: SplitterDao,
                                         private val feeDao: FeeDao) {
    fun getFeesByMonth(selectedMonth: Calendar) = splitterDao.getAllFeesFromMonth(
            selectedMonth.get(Calendar.MONTH),
            selectedMonth.get(Calendar.YEAR))

    @Transaction
    fun copyFee(copiedFees: List<FeeWithSplitters>) {
        copiedFees.forEach {it ->
            val fee = it.fee.copy(
                    id = null,
                    month = Calendar.getInstance().get(Calendar.MONTH),
                    year =  Calendar.getInstance().get(Calendar.YEAR))
            val feeId = feeDao.insertFee(fee).toInt()

            it.splitters?.let {splitters ->
                splitterDao.insertFeeShares(splitters.map { it.copy(id = null, feeId = feeId) })
            }
        }
    }

    @Transaction
    fun deleteFee(fee: FeeWithSplitters) {
        fee.splitters?.let { splitterDao.deleteSplitters(it) }
        feeDao.deleteFee(fee.fee)
    }
}