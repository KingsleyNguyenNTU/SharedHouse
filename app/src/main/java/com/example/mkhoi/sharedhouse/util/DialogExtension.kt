package com.example.mkhoi.sharedhouse.util

import android.app.AlertDialog
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import com.example.mkhoi.sharedhouse.R
import java.util.*

fun Context.showCustomDialog(customView: View,
                             @StringRes titleResId: Int,
                             positiveFunction: () -> Unit): AlertDialog? {
    return AlertDialog.Builder(this)
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

fun Context.showMultipleChoicesDialog(@StringRes titleResId: Int,
                                      multipleChoices: Array<String>,
                                      selectedItems: MutableList<Int>,
                                      positiveFunction: () -> Unit){
    val checkedItems = BooleanArray(multipleChoices.size, {false})
    selectedItems.forEach { checkedItems[it] = true }

    AlertDialog.Builder(this)
            .setTitle(this.getString(titleResId))
            .setMultiChoiceItems(multipleChoices, checkedItems, {
                dialog: DialogInterface, selectedPosition: Int, isChecked: Boolean ->
                if (isChecked) {
                    selectedItems.add(selectedPosition)
                } else if (selectedItems.contains(selectedPosition)) {
                    selectedItems.remove(selectedPosition)
                }
            })
            .setPositiveButton(this.getString(R.string.ok_btn_label), { _, _ ->
                positiveFunction.invoke()
            })
            .setNegativeButton(this.getString(R.string.cancel_btn_label), {
                dialog, whichButton -> dialog.cancel()
            })
            .show()
}

fun Context.showMonthPickerDialog(currentMonth: MutableLiveData<Calendar>){
    currentMonth.value?.let {

        val displayMonths = arrayOf("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December")
        val dialogView = LayoutInflater.from(this).inflate(R.layout.month_picker_dialog, null)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.pickerMonth)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.pickerYear)

        monthPicker.minValue = 0
        monthPicker.maxValue = 11
        monthPicker.displayedValues = displayMonths
        monthPicker.value = it.get(Calendar.MONTH)

        yearPicker.minValue = Calendar.getInstance().get(Calendar.YEAR) - 100
        yearPicker.maxValue = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.value = it.get(Calendar.YEAR)

        showCustomDialog(
                customView = dialogView,
                titleResId = R.string.action_month_picker_title,
                positiveFunction = {
                    currentMonth.value = Calendar.getInstance().apply {
                        set(Calendar.DATE, 1)
                        set(Calendar.MONTH, monthPicker.value)
                        set(Calendar.YEAR, yearPicker.value)
                    }
                }
        )
    }
}
