package com.example.mkhoi.sharedhouse.list_view

data class BillListItem(var mainName: String,
                        var amount: Float,
                        var phoneNumbers: List<String>,
                        var billDetails: MutableList<BillDetailListItem>)