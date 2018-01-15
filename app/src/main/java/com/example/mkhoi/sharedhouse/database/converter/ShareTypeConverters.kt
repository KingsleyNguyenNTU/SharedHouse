package com.example.mkhoi.sharedhouse.database.converter

import android.arch.persistence.room.TypeConverter
import android.databinding.InverseMethod
import com.example.mkhoi.sharedhouse.database.bean.ShareType


class ShareTypeConverters {
    companion object {
        @JvmStatic
        @InverseMethod("toShareType")
        fun toInt(value: ShareType) = value.ordinal

        @JvmStatic
        fun toShareType(value: Int) = ShareType.values()[value]

        @JvmStatic
        fun toList(): List<String>{
            val list = ArrayList<String>()
            list.add("Person")
            list.add("Room")
            return list
        }
    }

    @TypeConverter
    fun fromStringToShareType(value: String): ShareType = ShareType.valueOf(value)

    @TypeConverter
    fun fromShareTypeToString(value: ShareType): String = value.toString()
}