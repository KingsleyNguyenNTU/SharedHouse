package com.example.mkhoi.sharedhouse.room_edit

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.databinding.FragmentEditRoomBinding
import kotlinx.android.synthetic.main.fragment_edit_room.*


class EditRoomFragment : Fragment() {

    companion object {
        val ROOM_BUNDLE_KEY = "ROOM_BUNDLE_KEY"

        fun newInstance(room: UnitWithPersons? = null): EditRoomFragment {
            val fragment = EditRoomFragment()
            var arguments = Bundle()
            arguments.putParcelable(ROOM_BUNDLE_KEY, room)
            fragment.arguments = arguments
            return fragment
        }
    }

    internal lateinit var viewModel: EditRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, EditRoomViewModel.Factory(arguments[ROOM_BUNDLE_KEY] as UnitWithPersons?))
                .get(EditRoomViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_edit_room, container, false)

        val binding = FragmentEditRoomBinding.bind(view)
        binding.viewModel = this.viewModel

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.findViewById(R.id.toolbar) as Toolbar).title = getString(R.string.add_rooms_fragment_title)

        roommates_list.layoutManager = LinearLayoutManager(context)
        roommates_list.adapter = RoommatesRecyclerViewAdapter(emptyList(), this)

        initButtonListener()

        viewModel.roommates.observe(this, Observer {
            roommates_list.adapter = it?.let { roommates -> RoommatesRecyclerViewAdapter(roommates, this) }
        })

        viewModel.isSaving.observe(this, Observer {
            when (it) {
                true -> {
                    save_room_btn.isEnabled = false
                    (activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = View.VISIBLE
                }
                false -> {
                    (activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = View.GONE
                    activity.supportFragmentManager.popBackStack()
                }
            }
        })
    }

    private fun initButtonListener() {
        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = GONE

        save_room_btn.setOnClickListener {
            viewModel.save()
        }

        add_room_btn.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.add_roommate_dialog, null)

            AlertDialog.Builder(context)
                    .setTitle(getString(R.string.add_roommate_dialog_title))
                    .setView(dialogView)
                    .setPositiveButton(getString(R.string.ok_btn_label), { dialog, whichButton ->

                        val roommateName = (dialogView.findViewById(R.id.input_roommate_name) as EditText).text.toString()
                        val roomatePhone = (dialogView.findViewById(R.id.input_roommate_phone) as EditText).text.toString()
                        val newRoomate = Person(name = roommateName, phone = roomatePhone)

                        viewModel.addRoommate(newRoomate)
                    })
                    .setNegativeButton(getString(R.string.cancel_btn_label), { dialog, whichButton -> dialog.cancel()})
                    .show()
        }
    }
}
