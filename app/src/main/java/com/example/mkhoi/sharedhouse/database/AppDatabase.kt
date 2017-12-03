package com.example.mkhoi.sharedhouse.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.mkhoi.sharedhouse.database.converter.BooleanConverters
import com.example.mkhoi.sharedhouse.database.converter.DateConverters
import com.example.mkhoi.sharedhouse.database.dao.PersonDao
import com.example.mkhoi.sharedhouse.database.dao.UnitDao
import com.example.mkhoi.sharedhouse.database.dao.UnitPersonDao
import com.example.mkhoi.sharedhouse.database.entity.LeaveRecord
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit


@Database(entities = arrayOf(Unit::class, Person::class, LeaveRecord::class), version = 1)
@TypeConverters(DateConverters::class, BooleanConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun unitPersonDao(): UnitPersonDao
    abstract fun unitDao(): UnitDao
    abstract fun personDao(): PersonDao

    companion object {
        val DB_NAME = "SharedHouseDB"
    }
}