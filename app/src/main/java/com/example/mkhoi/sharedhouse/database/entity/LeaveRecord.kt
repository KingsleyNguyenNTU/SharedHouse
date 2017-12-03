package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class LeaveRecord(@PrimaryKey(autoGenerate = true) var id: Int,
                       var personId: Int,
                       var startLeave: Date,
                       var endLeave: Date)