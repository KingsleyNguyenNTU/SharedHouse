package com.example.mkhoi.sharedhouse.settings

import android.arch.persistence.room.Transaction
import com.example.mkhoi.sharedhouse.database.bean.SettingKey
import com.example.mkhoi.sharedhouse.database.dao.SettingDao
import com.example.mkhoi.sharedhouse.database.entity.Setting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(private val settingDao: SettingDao) {
    fun getSetting(key: SettingKey) = settingDao.getSetting(key)

    @Transaction
    fun saveSetting(setting: Setting, oldSetting: Setting?){
        oldSetting?.let { settingDao.deleteSetting(it) }
        settingDao.insertSetting(setting)
    }

}
