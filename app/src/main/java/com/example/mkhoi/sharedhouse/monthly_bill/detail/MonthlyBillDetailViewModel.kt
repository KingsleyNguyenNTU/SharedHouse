package com.example.mkhoi.sharedhouse.monthly_bill.detail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.list_view.BillDetailListItem


class MonthlyBillDetailViewModel(val billDetailList: List<BillDetailListItem>): ViewModel() {

    class Factory(val billDetailList: List<BillDetailListItem>?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MonthlyBillDetailViewModel(billDetailList ?: emptyList()) as T
        }
    }
}