package com.example.mkhoi.sharedhouse.fee_edit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.bean.FeeType
import com.example.mkhoi.sharedhouse.database.bean.ShareType
import com.example.mkhoi.sharedhouse.database.entity.Fee


class EditFeeViewModel(private val fee: Fee?,
                       private val editFeeRepository: EditFeeRepository) {
    val feeLiveData: MutableLiveData<Fee> = MutableLiveData()
        get() {
            if (field.value == null ) {
                field.value = fee ?: Fee(
                        name = "",
                        feeType = FeeType.RENTAL,
                        shareType = ShareType.SHARE_BY_ROOM_WITHOUT_TIME,
                        month = 0,
                        year = 1970,
                        amount = 0.0)
            }
            return field
        }

    class Factory(val fee: Fee?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditFeeViewModel(fee,MyApp.component.editFeeRepository()) as T
        }
    }
}