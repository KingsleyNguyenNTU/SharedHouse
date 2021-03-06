package com.example.mkhoi.sharedhouse.database.bean

import android.arch.persistence.room.Embedded
import com.example.mkhoi.sharedhouse.database.entity.FeeShare

class RoomSplitter(@Embedded var roomWithRoommates: UnitWithPersons) {
    @Embedded(prefix = "FeeShare_")
    var feeShare: FeeShare?=null
}