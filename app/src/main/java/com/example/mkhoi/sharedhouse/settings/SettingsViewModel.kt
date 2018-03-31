package com.example.mkhoi.sharedhouse.settings

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider


class SettingsViewModel: ViewModel() {

    class Factory() : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel() as T
        }
    }
}