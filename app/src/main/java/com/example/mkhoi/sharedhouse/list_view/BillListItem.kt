package com.example.mkhoi.sharedhouse.list_view

import android.net.Uri

data class BillListItem(var mainName: String,
                        var amount: Float,
                        var profilePicture: Uri?,
                        var phoneNumbers: List<String>,
                        var billDetails: MutableList<BillDetailListItem>)