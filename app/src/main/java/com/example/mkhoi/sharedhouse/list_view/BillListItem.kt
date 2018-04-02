package com.example.mkhoi.sharedhouse.list_view

import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import com.example.mkhoi.sharedhouse.database.entity.Person

data class BillListItem(var mainName: String,
                        var amount: Float,
                        var profilePicture: MutableLiveData<Uri?> = MutableLiveData(),
                        internal var roommates: List<Person>,
                        var billDetails: MutableList<BillDetailListItem>)