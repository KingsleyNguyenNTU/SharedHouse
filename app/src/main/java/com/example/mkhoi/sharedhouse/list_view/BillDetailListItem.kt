package com.example.mkhoi.sharedhouse.list_view

data class BillDetailListItem(var mainName: String,
                              var amount: Float,
                              var roommates: MutableList<BillRoommateListItem>)