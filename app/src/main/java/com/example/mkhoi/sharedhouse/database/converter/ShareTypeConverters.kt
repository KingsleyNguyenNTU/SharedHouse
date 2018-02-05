package com.example.mkhoi.sharedhouse.database.converter

import android.arch.persistence.room.TypeConverter
import com.example.mkhoi.sharedhouse.database.bean.ShareType


class ShareTypeConverters {

    @TypeConverter
    fun fromStringToShareType(value: String): ShareType = ShareType.valueOf(value)

    @TypeConverter
    fun fromShareTypeToString(value: ShareType): String = value.toString()
}