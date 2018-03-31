package com.example.mkhoi.sharedhouse.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.mkhoi.sharedhouse.database.bean.SettingKey
import com.example.mkhoi.sharedhouse.database.entity.Setting

@Dao
interface SettingDao {
    @Insert
    fun insertSetting(setting: Setting): Long

    @Delete
    fun deleteSetting(setting: Setting)

    @Query("select * from setting where `key` = :key")
    fun getSetting(key: SettingKey): LiveData<Setting>
}