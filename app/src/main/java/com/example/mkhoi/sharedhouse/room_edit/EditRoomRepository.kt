package com.example.mkhoi.sharedhouse.room_edit

import com.example.mkhoi.sharedhouse.database.dao.PersonDao
import com.example.mkhoi.sharedhouse.database.dao.UnitDao
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditRoomRepository @Inject constructor(val unitDao: UnitDao, val personDao: PersonDao) {
    fun addRoom(unit: Unit, roommates: List<Person>){
        val unitId = unitDao.insertUnit(unit).toInt()
        for (roommate in roommates){
            roommate.unitId = unitId
        }
        personDao.insertPersons(roommates)
    }
}