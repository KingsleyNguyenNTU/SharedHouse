package com.example.mkhoi.sharedhouse.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.mkhoi.sharedhouse.database.converter.BooleanConverters
import com.example.mkhoi.sharedhouse.database.converter.DateConverters
import com.example.mkhoi.sharedhouse.database.converter.FeeTypeConverters
import com.example.mkhoi.sharedhouse.database.converter.ShareTypeConverters
import com.example.mkhoi.sharedhouse.database.dao.*
import com.example.mkhoi.sharedhouse.database.entity.*
import com.example.mkhoi.sharedhouse.database.entity.Unit


@Database(entities = arrayOf(Unit::class, Person::class, LeaveRecord::class, Fee::class, FeeShare::class), version = 1)
@TypeConverters(
        DateConverters::class,
        BooleanConverters::class,
        FeeTypeConverters::class,
        ShareTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun unitPersonDao(): UnitPersonDao
    abstract fun splitterDao(): SplitterDao
    abstract fun unitDao(): UnitDao
    abstract fun personDao(): PersonDao
    abstract fun feeDao(): FeeDao

    companion object {
        val DB_NAME = "SharedHouseDB"
    }
}