package com.example.mkhoi.sharedhouse.fee_edit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.BackgroundAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.*
import com.example.mkhoi.sharedhouse.database.entity.Fee
import java.util.*


class EditFeeViewModel(private val selectedMonth: Calendar,
                       private val feeWithSplitters: FeeWithSplitters?,
                       private val editFeeRepository: EditFeeRepository): ViewModel() {

    val fee: MutableLiveData<Fee> = MutableLiveData()
        get() {
            if (field.value == null ) {
                field.value = feeWithSplitters?.fee ?: Fee(
                        name = "",
                        feeType = FeeType.ENTERTAINMENT,
                        shareType = ShareType.SHARE_BY_ROOM_WITHOUT_TIME,
                        month = selectedMonth.get(Calendar.MONTH),
                        year = selectedMonth.get(Calendar.YEAR),
                        amount = 0.0)
            }
            return field
        }

    val roomSplitters: MutableLiveData<List<RoomSplitter>> = MutableLiveData()
        get(){
            if (field.value == null) {
                editFeeRepository.getRoomSplitters(field, fee.value?.id)
            }
            return field
        }

    val personSplitters: MutableLiveData<List<PersonSplitter>> = MutableLiveData()
        get(){
            if (field.value == null) {
                editFeeRepository.getPersonSplitters(field, fee.value?.id)
            }
            return field
        }

    val feePayers: MutableLiveData<List<FeePayer>> = MutableLiveData()
        get(){
            if (field.value == null) {
                editFeeRepository.getFeePayers(field, fee.value?.id)
            }
            return field
        }

    val updateSplittersListFlag: MutableLiveData<Boolean> = MutableLiveData()

    val isSaving: MutableLiveData<Boolean> = MutableLiveData()

    fun save() {
        isSaving.value = true
        fee.value?.let {fee ->
            BackgroundAsyncTask().execute({
                editFeeRepository.saveFee(fee, roomSplitters.value, personSplitters.value, feePayers.value)
                isSaving.postValue(false)
            })
        }

    }

    fun saveValidation(): Boolean{
        return fee.value?.let {fee ->
            val totalPrepaid = feePayers.value?.map { it.feePrepaid?.amount ?: 0.0 }?.sum() ?: 0.0
            (totalPrepaid <= fee.amount)
        } ?: false

    }

    class Factory(private val selectedMonth: Calendar,
                  private val feeWithSplitters: FeeWithSplitters?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditFeeViewModel(
                    selectedMonth,
                    feeWithSplitters,MyApp.component.editFeeRepository()) as T
        }
    }
}