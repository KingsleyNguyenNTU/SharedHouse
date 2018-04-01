package com.example.mkhoi.sharedhouse.database.bean

import android.support.annotation.StringRes
import com.example.mkhoi.sharedhouse.R


enum class SettingKey(@StringRes val labelKey: Int) {
    HOUSE_PICTURE(R.string.house_picture_setting_label),
    HOUSE_NAME(R.string.house_name_setting_label)
}