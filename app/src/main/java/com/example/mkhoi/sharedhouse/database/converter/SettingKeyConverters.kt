package com.example.mkhoi.sharedhouse.database.converter

import android.arch.persistence.room.TypeConverter
import android.databinding.InverseMethod
import com.example.mkhoi.sharedhouse.database.bean.SettingKey


class SettingKeyConverters {

    @TypeConverter
    fun fromStringToSettingKey(value: String): SettingKey = SettingKey.valueOf(value)

    @TypeConverter
    @InverseMethod("fromStringToSettingKey")
    fun fromSettingKeyToString(value: SettingKey): String = value.toString()
}