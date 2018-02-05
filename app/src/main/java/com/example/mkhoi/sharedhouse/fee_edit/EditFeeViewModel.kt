package com.example.mkhoi.sharedhouse.fee_edit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.bean.FeeType
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import com.example.mkhoi.sharedhouse.database.bean.RoomSplitter
import com.example.mkhoi.sharedhouse.database.bean.ShareType
import com.example.mkhoi.sharedhouse.database.entity.Fee


class EditFeeViewModel(private val feeWithSplitters: FeeWithSplitters?,
                       private val editFeeRepository: EditFeeRepository): ViewModel() {
    val fee: MutableLiveData<Fee> = MutableLiveData()
        get() {
            if (field.value == null ) {
                field.value = feeWithSplitters?.fee ?: Fee(
                        name = "",
                        feeType = FeeType.RENTAL,
                        shareType = ShareType.SHARE_BY_ROOM_WITHOUT_TIME,
                        month = 0,
                        year = 1970,
                        amount = 0.0)
            }
            return field
        }

    val roomSplitters: MutableLiveData<List<RoomSplitter>> = MutableLiveData()
        get(){
            if (fee.value?.isSharedByRoom()?.not() ?: false){
                field.value = emptyList()
            }
            else if (field.value == null) {
                editFeeRepository.getRoomSplitters(field, fee.value?.id)
            }
            return field
        }

    class Factory(private val feeWithSplitters: FeeWithSplitters?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditFeeViewModel(feeWithSplitters,MyApp.component.editFeeRepository()) as T
        }
    }
}