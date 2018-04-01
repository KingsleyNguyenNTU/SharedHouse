package com.example.mkhoi.sharedhouse

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.database.bean.SettingKey
import com.example.mkhoi.sharedhouse.database.entity.Setting
import com.example.mkhoi.sharedhouse.settings.SettingsRepository


class MainViewModel(private val repository: SettingsRepository): ViewModel() {
    val housePictureSetting: LiveData<Setting> = repository.getSetting(SettingKey.HOUSE_PICTURE)
    val houseNameSetting: LiveData<Setting> = repository.getSetting(SettingKey.HOUSE_NAME)

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(MyApp.component.settingsRepository()) as T
        }
    }
}