package com.example.mkhoi.sharedhouse.fee_edit

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Transaction
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.RoomSplitter
import com.example.mkhoi.sharedhouse.database.dao.FeeDao
import com.example.mkhoi.sharedhouse.database.dao.SplitterDao
import com.example.mkhoi.sharedhouse.database.entity.Fee
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditFeeRepository @Inject constructor(private val splitterDao: SplitterDao,
                                            private val feeDao: FeeDao) {
    fun getRoomSplitters(result: MutableLiveData<List<RoomSplitter>>, feeId: Int?) {
        result.value = emptyList()
        val task = DatabaseAsyncTask()
        task.execute({
            result.postValue(splitterDao.getAllRoomSplitters(feeId = feeId))
        })
    }

    @Transaction
    fun saveFee(fee: Fee, roomSplitters: List<RoomSplitter>, isSaving: MutableLiveData<Boolean>) {
        if (fee.id == null){
            val feeId = feeDao.insertFee(fee).toInt()
            fee.id = feeId
            roomSplitters.map { it.feeShare?.apply { this.feeId = feeId } }.filterNotNull().let {
                splitterDao.insertFeeShares(it)
            }
        }
        else {
            feeDao.updateFee(fee)
            fee.id?.let {feeId ->
                roomSplitters.map { it.feeShare?.apply {
                    this.feeId = feeId
                    this.id = null
                } }.filterNotNull().let {
                    splitterDao.deleteSplittersByFeeId(feeId)
                    splitterDao.insertFeeShares(it)
                }
            }
        }
    }
}