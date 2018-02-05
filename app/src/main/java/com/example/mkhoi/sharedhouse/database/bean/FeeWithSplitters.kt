package com.example.mkhoi.sharedhouse.database.bean

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.example.mkhoi.sharedhouse.database.entity.Fee
import com.example.mkhoi.sharedhouse.database.entity.FeeShare

class FeeWithSplitters(@Embedded var fee: Fee) {
    @Relation(parentColumn = "id", entityColumn = "feeId")
    var splitters: List<FeeShare>? = null
}