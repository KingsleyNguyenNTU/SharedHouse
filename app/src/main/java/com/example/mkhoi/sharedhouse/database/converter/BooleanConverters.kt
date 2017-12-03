package com.example.mkhoi.sharedhouse.database.converter

import android.arch.persistence.room.TypeConverter

class BooleanConverters() {
    val TRUE_VALUE = 0L
    val FALSE_VALUE = 1L

    @TypeConverter
    fun fromIntValue(value: Long?): Boolean {
        return value === TRUE_VALUE
    }

    @TypeConverter
    fun booleanToIntValue(value: Boolean): Long? {
        return if (value) TRUE_VALUE else FALSE_VALUE
    }
}
