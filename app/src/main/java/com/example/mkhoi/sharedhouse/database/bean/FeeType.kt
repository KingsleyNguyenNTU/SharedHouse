package com.example.mkhoi.sharedhouse.database.bean

import android.support.annotation.DrawableRes
import com.example.mkhoi.sharedhouse.R


enum class FeeType(@DrawableRes val drawableId : Int) {
    RENTAL(R.drawable.ic_rental_fee_icon),
    PUB(R.drawable.ic_pub_fee_icon),
    GROCERY(R.drawable.ic_grocery_fee_icon),
    EATING(R.drawable.ic_eating_fee_icon),
    ENTERTAINMENT(R.drawable.ic_entertainment_fee_icon),
    OTHERS(R.drawable.ic_others_fee_icon)
}