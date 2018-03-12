package com.example.mkhoi.sharedhouse.backup

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.mkhoi.sharedhouse.database.AppDatabase
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestoreRepository @Inject constructor() {

    fun restore(context: Context, backupDB: Uri, restoreResultLiveData: MutableLiveData<Boolean>) {
        try{
            val currentDB = File(context.getDatabasePath(AppDatabase.DB_NAME).absolutePath)
            val src = context.contentResolver.openInputStream(backupDB)
            val dst = FileOutputStream(currentDB)
            src.copyTo(dst)
            src.close()
            dst.close()
            Log.d("RestoreRepository", "Restore DB from: ${backupDB.path}")
            restoreResultLiveData.postValue(true)
        } catch (e: Exception){
            restoreResultLiveData.postValue(false)
            Log.e("RestoreRepository", "Fail to backup database", e)
        }
    }
}