package com.example.mkhoi.sharedhouse.monthly_bill

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.BackgroundAsyncTask
import com.example.mkhoi.sharedhouse.list_view.BillListItem
import java.util.*


class MonthlyBillViewModel(private val repository: MonthlyBillRepository): ViewModel() {
    val monthlyBillList : MutableLiveData<List<BillListItem>> = MutableLiveData()
    val selectedMonth: MutableLiveData<Calendar> = MutableLiveData()
        get() {
            if (field.value == null){
                field.value = Calendar.getInstance().apply {
                    set(Calendar.DATE, 1)
                }
            }
            return field
        }

    fun reloadMonthlyBills(selectedMonth: Calendar){
        BackgroundAsyncTask().execute({
            repository.getMonthlyBills(selectedMonth, monthlyBillList)
        })
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MonthlyBillViewModel(MyApp.component.monthlyBillRepository()) as T
        }
    }
}