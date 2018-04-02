package com.example.mkhoi.sharedhouse.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.example.mkhoi.sharedhouse.database.bean.SettingKey

@Entity
data class Setting(@PrimaryKey var key: SettingKey,
                   var value: String){
    companion object {
        const val IMAGE_MAX_SIZE = 2000000 // 2MB
        const val TRUE_VALUE = "TRUE"
        const val FALSE_VALUE = "FALSE"
    }
}