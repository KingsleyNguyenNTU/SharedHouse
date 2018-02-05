package com.example.mkhoi.sharedhouse.fee_edit

import android.arch.lifecycle.MutableLiveData
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.RoomSplitter
import com.example.mkhoi.sharedhouse.database.dao.SplitterDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditFeeRepository @Inject constructor(private val splitterDao: SplitterDao) {
    fun getRoomSplitters(result: MutableLiveData<List<RoomSplitter>>, feeId: Int?) {
        result.value = emptyList()
        val task = DatabaseAsyncTask()
        task.execute({
            result.postValue(splitterDao.getAllRoomSplitters(feeId = feeId))
        })
    }
}