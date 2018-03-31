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

    fun saveSetting(setting: Setting){
        val oldSetting = when(setting.key){
            SettingKey.HOUSE_PICTURE -> housePictureSetting.value
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