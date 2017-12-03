package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Person(@PrimaryKey(autoGenerate = true) var id: Int? = null,
                  var unitId: Int = 0,
                  var name: String,
                  var phone: String,
                  var active: Boolean = true)