package com.example.mkhoi.sharedhouse.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import com.example.mkhoi.sharedhouse.database.converter.*
import com.example.mkhoi.sharedhouse.database.dao.*
import com.example.mkhoi.sharedhouse.database.entity.*
import com.example.mkhoi.sharedhouse.database.entity.Unit




@Database(entities = [
    (Unit::class),
    (Person::class),
    (LeaveRecord::class),
    (Fee::class),
    (FeeShare::class),
    (Setting::class)],
        version = 2)
@TypeConverters(
        DateConverters::class,
        BooleanConverters::class,
        FeeTypeConverters::class,
        ShareTypeConverters::class,
        SettingKeyConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun unitPersonDao(): UnitPersonDao
    abstract fun splitterDao(): SplitterDao
    abstract fun unitDao(): UnitDao
    abstract fun personDao(): PersonDao
    abstract fun feeDao(): FeeDao
    abstract fun settingDao(): SettingDao

    companion object {
        const val DB_NAME = "SharedHouseDB"

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `Setting` (`key` TEXT NOT NULL, " + "`value` TEXT NOT NULL, PRIMARY KEY(`key`))")
            }
        }
    }
}