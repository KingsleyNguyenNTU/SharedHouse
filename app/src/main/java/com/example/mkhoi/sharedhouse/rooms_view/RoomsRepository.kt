package com.example.mkhoi.sharedhouse.rooms_view

import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.database.dao.PersonDao
import com.example.mkhoi.sharedhouse.database.dao.UnitDao
import com.example.mkhoi.sharedhouse.database.dao.UnitPersonDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomsRepository @Inject constructor(val unitPersonDao: UnitPersonDao,
                                          val unitDao: UnitDao,
                                          val personDao: PersonDao) {
    fun getActiveRooms() = unitPersonDao.getAllActiveUnits()

    fun deleteRoom(unitWithPersons: UnitWithPersons) {
        unitWithPersons.roommates?.map { it.apply { active = false } }?.let { personDao.updatePersons(it) }
        unitDao.updateUnit(unitWithPersons.unit.apply { active = false })
    }
}