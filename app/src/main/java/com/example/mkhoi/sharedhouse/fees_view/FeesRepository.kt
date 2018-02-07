package com.example.mkhoi.sharedhouse.fees_view

import com.example.mkhoi.sharedhouse.database.dao.FeeDao
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeesRepository @Inject constructor(val feeDao: FeeDao) {
    fun getCurrentFees() = feeDao.getAllFeesFromMonth(
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.YEAR))
}