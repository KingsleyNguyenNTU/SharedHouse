package com.example.mkhoi.sharedhouse.room_edit

import com.example.mkhoi.sharedhouse.database.dao.PersonDao
import com.example.mkhoi.sharedhouse.database.dao.UnitDao
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditRoomRepository @Inject constructor(val unitDao: UnitDao, val personDao: PersonDao) {
    fun saveRoom(unit: Unit, roommates: List<Person>, deletedRoommates: List<Person>){
        val unitId: Int
        if (unit.id == null) {
            unitId = unitDao.insertUnit(unit).toInt()
        } else {
            unitId = unit.id!!
            unitDao.updateUnit(unit)
        }

        roommates.forEach { roommate -> roommate.unitId = unitId }
        val newRoommates = roommates.filter { it.id == null }.toList()
        val existingRoommates = roommates.filter { it.id != null }.toList()

        personDao.insertPersons(newRoommates)
        personDao.updatePersons(existingRoommates)
        personDao.updatePersons(deletedRoommates.filter { it.id != null }.toList().map { it.apply { active = false } })
    }
}