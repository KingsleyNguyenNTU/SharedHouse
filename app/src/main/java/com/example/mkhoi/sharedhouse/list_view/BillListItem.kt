package com.example.mkhoi.sharedhouse.list_view

data class BillListItem(var mainName: String,
                        var amount: Float,
                        var billDetails: MutableList<BillDetailListItem>)