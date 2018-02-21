package com.example.mkhoi.sharedhouse.database.converter

import android.arch.persistence.room.TypeConverter
import android.databinding.InverseMethod
import com.example.mkhoi.sharedhouse.database.bean.FeeType


class FeeTypeConverters {
    @TypeConverter
    fun fromStringToFeeType(value: String): FeeType = FeeType.valueOf(value)

    @TypeConverter
    @InverseMethod("fromStringToFeeType")
    fun fromFeeTypeToString(value: FeeType): String = value.toString()
}