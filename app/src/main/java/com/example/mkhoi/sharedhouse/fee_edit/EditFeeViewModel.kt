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
import java.util.*


class EditFeeViewModel(private val feeWithSplitters: FeeWithSplitters?,
                       private val editFeeRepository: EditFeeRepository): ViewModel() {

    val fee: MutableLiveData<Fee> = MutableLiveData()
        get() {
            if (field.value == null ) {
                field.value = feeWithSplitters?.fee ?: Fee(
                        name = "",
                        feeType = FeeType.ENTERTAINMENT,
                        shareType = ShareType.SHARE_BY_ROOM_WITHOUT_TIME,
                        month = Calendar.getInstance().get(Calendar.MONTH),
                        year = Calendar.getInstance().get(Calendar.YEAR),
                        amount = 0.0)
            }
            return field
        }

    val roomSplitters: MutableLiveData<List<RoomSplitter>> = MutableLiveData()
        get(){
            if (fee.value?.isSharedByRoom() == false){
                field.value = emptyList()
            }
            else if (field.value == null) {
                editFeeRepository.getRoomSplitters(field, fee.value?.id)
            }
            return field
        }
    val isSaving: MutableLiveData<Boolean> = MutableLiveData()

    fun save() {
        isSaving.value = true
        fee.value?.let {fee ->
            roomSplitters.value?.let {roomSplitters ->
                editFeeRepository.saveFee(fee, roomSplitters, isSaving)
            }
        }

    }

    class Factory(private val feeWithSplitters: FeeWithSplitters?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditFeeViewModel(feeWithSplitters,MyApp.component.editFeeRepository()) as T
        }
    }
}