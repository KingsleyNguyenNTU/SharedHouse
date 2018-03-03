package com.example.mkhoi.sharedhouse.monthly_bill

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.list_view.BillListItem


class MonthlyBillViewModel(private val repository: MonthlyBillRepository): ViewModel() {
    val monthlyBillList : MutableLiveData<List<BillListItem>> = MutableLiveData()
        get(){
            if (field.value == null){
                field.value = emptyList()
                DatabaseAsyncTask().execute({
                    repository.getMonthlyBills(field)
                })
            }
            return field
        }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MonthlyBillViewModel(MyApp.component.monthlyBillRepository()) as T
        }
    }
}