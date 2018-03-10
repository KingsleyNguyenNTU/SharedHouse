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
    fun deleteFee(fee: FeeWithSplitters) {
        fee.splitters?.let { splitterDao.deleteSplitters(it) }
        feeDao.deleteFee(fee.fee)
    }
}