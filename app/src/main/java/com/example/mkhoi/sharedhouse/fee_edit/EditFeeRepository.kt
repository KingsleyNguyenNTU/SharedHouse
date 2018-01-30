package com.example.mkhoi.sharedhouse.fee_edit

import com.example.mkhoi.sharedhouse.database.dao.FeeDao
import com.example.mkhoi.sharedhouse.database.dao.PersonDao
import com.example.mkhoi.sharedhouse.database.dao.UnitDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditFeeRepository @Inject constructor(val feeDao: FeeDao,
                                            val unitDao: UnitDao,
                                            val personDao: PersonDao) {
    fun getActiveRooms() = unitDao.getAllActiveUnits()

    fun getActivePersons() = personDao.getAllActivePersons()
}