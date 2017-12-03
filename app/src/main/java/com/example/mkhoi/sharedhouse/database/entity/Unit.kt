package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Relation

@Entity
data class Unit(@PrimaryKey(autoGenerate = true) var id: Int,
                var name: String,
                var active: Boolean)