package com.example.mkhoi.sharedhouse.fee_edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.mkhoi.sharedhouse.MainActivity
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import java.util.*


class EditFeeActivity: MainActivity() {

    companion object {
        const val FEE_BUNDLE_KEY = "FEE_BUNDLE_KEY"
        const val SELECTED_MONTH_KEY = "SELECTED_MONTH_KEY"

        fun createIntent(context: Context,
                         selectedMonth: Calendar,
                         feeWithSplitters: FeeWithSplitters? = null): Intent = Intent(context, EditFeeActivity::class.java).apply {
            putExtra(SELECTED_MONTH_KEY, selectedMonth)
            putExtra(FEE_BUNDLE_KEY, feeWithSplitters)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.findFragmentById(R.id.main_content_fragment) == null) {
            val selectedMonth = intent.extras[SELECTED_MONTH_KEY] as Calendar
            val feeWithSplitters = intent.extras[FEE_BUNDLE_KEY] as FeeWithSplitters?

            supportFragmentManager.beginTransaction()
                    .add(R.id.main_content_fragment, EditFeeFragment.newInstance(selectedMonth, feeWithSplitters))
                    .commitNow()
        }
    }
}