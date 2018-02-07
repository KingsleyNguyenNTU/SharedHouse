package com.example.mkhoi.sharedhouse.fees_view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters

class FeesViewModel(private val repository: FeesRepository): ViewModel() {
    val fees: LiveData<List<FeeWithSplitters>> = repository.getCurrentFees()

    fun deleteFee(fee: FeeWithSplitters) {
        val task = DatabaseAsyncTask()
        task.execute({
            repository.deleteFee(fee)
        })
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FeesViewModel(MyApp.component.feesRepository()) as T
        }
    }
}