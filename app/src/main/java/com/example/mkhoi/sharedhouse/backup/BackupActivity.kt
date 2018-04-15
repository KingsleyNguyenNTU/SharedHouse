package com.example.mkhoi.sharedhouse.backup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.mkhoi.sharedhouse.MainActivity
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.room_edit.EditRoomFragment
import kotlinx.android.synthetic.main.app_bar_main.*


class BackupActivity: MainActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, BackupActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.findFragmentById(R.id.main_content_fragment) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_content_fragment, BackupFragment.newInstance())
                    .commitNow()
        }

        fab.visibility = View.GONE
    }
}