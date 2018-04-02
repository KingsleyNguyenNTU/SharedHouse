package com.example.mkhoi.sharedhouse.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.BackgroundAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.SettingKey
import com.example.mkhoi.sharedhouse.database.entity.Setting


class SettingsViewModel(private val repository: SettingsRepository): ViewModel() {
    val housePictureSetting: LiveData<Setting> = repository.getSetting(SettingKey.HOUSE_PICTURE)
    val houseNameSetting: LiveData<Setting> = repository.getSetting(SettingKey.HOUSE_NAME)
    val defaultWhatsappSetting: LiveData<Setting> = repository.getSetting(SettingKey.DEFAULT_WHATSAPP)
    val defaultMessageSetting: LiveData<Setting> = repository.getSetting(SettingKey.DEFAULT_MESSAGE)

    fun saveSetting(setting: Setting){
        val oldSetting = when(setting.key){
            SettingKey.HOUSE_PICTURE -> housePictureSetting.value
            SettingKey.HOUSE_NAME -> houseNameSetting.value
            SettingKey.DEFAULT_WHATSAPP -> defaultWhatsappSetting.value
            SettingKey.DEFAULT_MESSAGE -> defaultMessageSetting.value
        }
        BackgroundAsyncTask().execute({
            repository.saveSetting(setting, oldSetting)
        })
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(MyApp.component.settingsRepository()) as T
        }
    }
}