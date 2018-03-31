package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.example.mkhoi.sharedhouse.database.bean.SettingKey

@Entity
data class Setting(@PrimaryKey var key: SettingKey,
                   var value: String)