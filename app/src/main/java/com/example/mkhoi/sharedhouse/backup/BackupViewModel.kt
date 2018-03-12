package com.example.mkhoi.sharedhouse.backup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.FragmentActivity
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.AppDatabase.Companion.DB_NAME
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask


class BackupViewModel(private val activity: FragmentActivity,
                      private val backupRepository: BackupRepository): ViewModel() {
    val backupResultLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun backup() {
        val currentDBPath = activity.getDatabasePath(DB_NAME).absolutePath
        DatabaseAsyncTask().execute({
            backupRepository.backup(currentDBPath, backupResultLiveData)
        })
    }

    class Factory(private val activity: FragmentActivity) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BackupViewModel(activity, MyApp.component.backupRepository()) as T
        }
    }
}