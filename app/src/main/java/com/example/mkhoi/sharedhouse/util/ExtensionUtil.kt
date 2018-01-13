package com.example.mkhoi.sharedhouse.util

import android.app.AlertDialog
import android.content.Context
import android.support.annotation.StringRes
import android.view.View
import com.example.mkhoi.sharedhouse.R

fun Context.showCustomDialog(customView: View,
                             @StringRes titleResId: Int,
                             positiveFunction: () -> Unit){
    AlertDialog.Builder(this)
            .setTitle(this.getString(titleResId))
            .setView(customView)
            .setPositiveButton(this.getString(R.string.ok_btn_label), { _, _ ->
                positiveFunction.invoke()
            })
            .setNegativeButton(this.getString(R.string.cancel_btn_label), {
                dialog, whichButton -> dialog.cancel()
            })
            .show()
}

fun Context.showBasicDialog(@StringRes titleResId: Int,
                            message: String,
                            positiveFunction: () -> Unit){
    AlertDialog.Builder(this)
            .setTitle(this.getString(titleResId))
            .setMessage(message)
            .setPositiveButton(this.getString(R.string.ok_btn_label), { _, _ ->
                positiveFunction.invoke()
            })
            .setNegativeButton(this.getString(R.string.cancel_btn_label), {
                dialog, whichButton -> dialog.cancel()
            })
            .show()
}
