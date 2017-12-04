package com.example.mkhoi.sharedhouse.rooms_view

import com.example.mkhoi.sharedhouse.database.dao.UnitPersonDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomsRepository @Inject constructor(val unitPersonDao: UnitPersonDao) {
    fun getActiveRooms() = unitPersonDao.getAllActiveUnits()
}