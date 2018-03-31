package com.example.mkhoi.sharedhouse.backup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.FragmentActivity
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.AppDatabase.Companion.DB_NAME
import com.example.mkhoi.sharedhouse.database.BackgroundAsyncTask


class BackupViewModel(private val currentDBPath: String,
                      private val backupRepository: BackupRepository): ViewModel() {
    val backupResultLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun backup() {
        BackgroundAsyncTask().execute({
            backupRepository.backup(currentDBPath, backupResultLiveData)
        })
    }

    class Factory(private val activity: FragmentActivity) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BackupViewModel(
                    activity.getDatabasePath(DB_NAME).absolutePath,
                    MyApp.component.backupRepository()) as T
        }
    }
}