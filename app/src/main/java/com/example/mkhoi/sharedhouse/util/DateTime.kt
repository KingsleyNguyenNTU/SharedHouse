package com.example.mkhoi.sharedhouse.util

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.toString(pattern: String): String{
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    return simpleDateFormat.format(this.time)
}
