package com.example.mkhoi.sharedhouse.database.converter

import android.arch.persistence.room.TypeConverter
import android.databinding.InverseMethod
import com.example.mkhoi.sharedhouse.database.bean.FeeType


class FeeTypeConverters {
    companion object {
        @JvmStatic
        @InverseMethod("toFeeType")
        fun toInt(value: FeeType) = value.ordinal

        @JvmStatic
        fun toFeeType(value: Int) = FeeType.values()[value]

        @JvmStatic
        fun toList() = FeeType.values().map { it.name.toLowerCase().capitalize() }.toList()
    }

    @TypeConverter
    fun fromStringToFeeType(value: String): FeeType = FeeType.valueOf(value)

    @TypeConverter
    @InverseMethod("fromStringToFeeType")
    fun fromFeeTypeToString(value: FeeType): String = value.toString()
}