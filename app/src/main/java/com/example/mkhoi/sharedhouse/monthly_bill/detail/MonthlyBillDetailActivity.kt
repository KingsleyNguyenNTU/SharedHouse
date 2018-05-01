package com.example.mkhoi.sharedhouse.monthly_bill.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.mkhoi.sharedhouse.MainActivity
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.list_view.BillDetailListItem
import kotlinx.android.synthetic.main.app_bar_main.*

class MonthlyBillDetailActivity : MainActivity() {

    companion object {
        const val BILL_DETAIL_LIST_KEY = "BILL_DETAIL_LIST_KEY"
        private const val ROOM_NAME_KEY = "ROOM_NAME_KEY"

        fun createIntent(context: Context,
                         billDetailList: ArrayList<BillDetailListItem>,
                         name: String) =
                Intent(context, MonthlyBillDetailActivity::class.java).apply {
                    putExtra(BILL_DETAIL_LIST_KEY, billDetailList)
                    putExtra(ROOM_NAME_KEY, name)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent.getStringExtra(ROOM_NAME_KEY)

        fab.visibility = View.GONE

        if (supportFragmentManager.findFragmentById(R.id.main_content_fragment) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_content_fragment, MonthlyBillDetailFragment.newInstance(
                            intent.getParcelableArrayListExtra(BILL_DETAIL_LIST_KEY)))
                    .commitNow()
        }
    }
}
