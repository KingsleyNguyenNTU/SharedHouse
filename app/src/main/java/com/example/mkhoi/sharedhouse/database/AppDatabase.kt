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
    (FeePrepaid::class),
    (Setting::class)],
        version = 3)
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

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `FeePrepaid` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`feeId` INTEGER NOT NULL, " +
                        "`personId` INTEGER NOT NULL, " +
                        "`amount` REAL NOT NULL)")
            }
        }
    }
}