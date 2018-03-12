package com.example.mkhoi.sharedhouse.backup

import android.arch.lifecycle.MutableLiveData
import android.os.Environment
import android.util.Log
import com.example.mkhoi.sharedhouse.database.AppDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupRepository @Inject constructor() {

    fun backup(currentDBPath: String, backupResultLiveData: MutableLiveData<Boolean>) {
        try{
            val currentDB = File(currentDBPath)
            val documentFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val backupDB = File(documentFolder, "${AppDatabase.DB_NAME}_${Calendar.getInstance().time}.db")

            if (currentDB.exists()) {
                val src = FileInputStream(currentDB).channel
                val dst = FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                Log.d("BackupRepository", "Save DB into: ${backupDB.absolutePath}")
                backupResultLiveData.postValue(true)
            }
        } catch (e: Exception){
            backupResultLiveData.postValue(false)
            Log.e("BackupRepository", "Fail to backup database", e)
        }
    }
}