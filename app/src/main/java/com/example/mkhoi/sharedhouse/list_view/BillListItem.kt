package com.example.mkhoi.sharedhouse.list_view

import android.arch.lifecycle.MutableLiveData
import android.net.Uri

data class BillListItem(var mainName: String,
                        var amount: Float,
                        var profilePicture: MutableLiveData<Uri?> = MutableLiveData(),
                        var phoneNumbers: List<String>,
                        var billDetails: MutableList<BillDetailListItem>)