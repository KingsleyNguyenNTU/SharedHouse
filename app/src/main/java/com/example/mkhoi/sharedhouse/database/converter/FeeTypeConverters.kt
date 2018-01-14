package com.example.mkhoi.sharedhouse.database.converter

import android.arch.persistence.room.TypeConverter
import com.example.mkhoi.sharedhouse.database.bean.FeeType


class FeeTypeConverters {
    @TypeConverter
    fun fromStringToFeeType(value: String): FeeType = FeeType.valueOf(value)

    @TypeConverter
    fun fromFeeTypeToString(value: FeeType): String = value.toString()
}