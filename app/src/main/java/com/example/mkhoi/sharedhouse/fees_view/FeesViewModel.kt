package com.example.mkhoi.sharedhouse.fees_view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import java.util.*

class FeesViewModel(private val repository: FeesRepository): ViewModel() {
    val fees: MutableLiveData<List<FeeWithSplitters>> = MutableLiveData()
    val selectedMonth: MutableLiveData<Calendar> = MutableLiveData()
        get() {
            if (field.value == null){
                field.value = Calendar.getInstance().apply {
                    set(Calendar.DATE, 1)
                }
            }
            return field
        }

    fun reloadFees(selectedMonth: Calendar) {
        DatabaseAsyncTask().execute({
            fees.postValue(repository.getFeesByMonth(selectedMonth))
        })
    }

    fun copyFees(selectedItems: MutableList<Int>) {
        fees.value?.let {
            DatabaseAsyncTask().execute({
                repository.copyFee(it.filterIndexed{index, value -> selectedItems.contains(index) })
            })
        }
    }

    fun deleteFee(fee: FeeWithSplitters) {
        DatabaseAsyncTask().execute({
            repository.deleteFee(fee)
            selectedMonth.value?.let {
                fees.postValue(repository.getFeesByMonth(it))
            }
        })
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FeesViewModel(MyApp.component.feesRepository()) as T
        }
    }
}