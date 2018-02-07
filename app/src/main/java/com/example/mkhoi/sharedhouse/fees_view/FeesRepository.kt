package com.example.mkhoi.sharedhouse.fees_view

import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import com.example.mkhoi.sharedhouse.database.dao.FeeDao
import com.example.mkhoi.sharedhouse.database.dao.SplitterDao
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeesRepository @Inject constructor(private val splitterDao: SplitterDao,
                                         private val feeDao: FeeDao) {
    fun getCurrentFees() = splitterDao.getAllFeesFromMonth(
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.YEAR))

    fun deleteFee(fee: FeeWithSplitters) {
        val task = DatabaseAsyncTask()
        task.execute({
            fee.splitters?.let { splitterDao.deleteSplitters(it) }
            feeDao.deleteFee(fee.fee)
        })
    }
}