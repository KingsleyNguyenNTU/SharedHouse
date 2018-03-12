package com.example.mkhoi.sharedhouse.backup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.net.Uri
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask


class RestoreViewModel(private val restoreRepository: RestoreRepository): ViewModel() {
    val backupFilePath: MutableLiveData<Uri> = MutableLiveData()
    val restoreResultLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun restore(context: Context) {
        backupFilePath.value?.let {
            DatabaseAsyncTask().execute({
                restoreRepository.restore(context, it, restoreResultLiveData)
            })
        }
    }

    class Factory() : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RestoreViewModel(MyApp.component.restoreRepository()) as T
        }
    }
}