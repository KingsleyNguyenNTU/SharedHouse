package com.example.mkhoi.sharedhouse.room_edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.mkhoi.sharedhouse.MainActivity
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.room_edit.EditRoomFragment
import com.example.mkhoi.sharedhouse.rooms_view.RoomsFragment
import kotlinx.android.synthetic.main.app_bar_main.*


class EditRoomActivity: MainActivity() {

    companion object {
        const val ROOM_BUNDLE_KEY = "ROOM_BUNDLE_KEY"

        fun createIntent(context: Context, room: UnitWithPersons? = null) =
                Intent(context, EditRoomActivity::class.java).apply {
                    putExtra(ROOM_BUNDLE_KEY, room)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.findFragmentById(R.id.main_content_fragment) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_content_fragment,
                            EditRoomFragment.newInstance(intent.extras[ROOM_BUNDLE_KEY] as UnitWithPersons?))
                    .commitNow()
        }
    }
}