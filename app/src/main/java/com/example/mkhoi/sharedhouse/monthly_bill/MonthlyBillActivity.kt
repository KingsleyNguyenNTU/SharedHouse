package com.example.mkhoi.sharedhouse.monthly_bill

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import com.example.mkhoi.sharedhouse.MainActivity
import com.example.mkhoi.sharedhouse.R
import kotlinx.android.synthetic.main.app_bar_main.*


class MonthlyBillActivity: MainActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, MonthlyBillActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fab.visibility = View.GONE

        if (supportFragmentManager.findFragmentById(R.id.main_content_fragment) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_content_fragment, MonthlyBillFragment.newInstance())
                    .commitNow()
        }

        if(hasReadContactsPermission().not()) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    READ_CONTACTS_REQUEST_CODE)
        }
    }
}