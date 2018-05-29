package com.example.mkhoi.sharedhouse.fees_view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.mkhoi.sharedhouse.MainActivity
import com.example.mkhoi.sharedhouse.R


class FeesActivity: MainActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, FeesActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.findFragmentById(R.id.main_content_fragment) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_content_fragment, FeesFragment.newInstance())
                    .commitNow()
        }
    }
}