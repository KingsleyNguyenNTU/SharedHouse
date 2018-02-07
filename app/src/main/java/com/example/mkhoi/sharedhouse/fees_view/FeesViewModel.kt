package com.example.mkhoi.sharedhouse.fees_view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.entity.Fee

class FeesViewModel(private val repository: FeesRepository): ViewModel() {
    val fees: LiveData<List<Fee>> = repository.getCurrentFees()

    fun deleteFee(fee: Fee) {
        val task = DatabaseAsyncTask()
        task.execute({
            //repository.deleteRoom(room)
        })
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FeesViewModel(MyApp.component.feesRepository()) as T
        }
    }
}