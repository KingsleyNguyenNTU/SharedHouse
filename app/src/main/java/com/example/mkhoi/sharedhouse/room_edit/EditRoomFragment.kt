package com.example.mkhoi.sharedhouse.room_edit

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
import com.example.mkhoi.sharedhouse.list_view.ListItem
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import com.example.mkhoi.sharedhouse.util.showCustomDialog
import kotlinx.android.synthetic.main.fragment_edit_room.*


class EditRoomFragment : Fragment() {

    companion object {
        val ROOM_BUNDLE_KEY = "ROOM_BUNDLE_KEY"

        fun newInstance(room: UnitWithPersons? = null): EditRoomFragment {
            val fragment = EditRoomFragment()
            val arguments = Bundle()
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
        (activity.findViewById(R.id.toolbar) as Toolbar).title = getString(R.string.edit_rooms_fragment_title)

        roommates_list.layoutManager = LinearLayoutManager(context)
        roommates_list.adapter = ListItemRecyclerViewAdapter<Person>(emptyList())

        initButtonListener()

        viewModel.roommates.observe(this, Observer {
            roommates_list.adapter = it?.let { roommates ->
                ListItemRecyclerViewAdapter(
                        data = roommates.map {
                            ListItem<Person>(mainName = it.name, caption = it.phone).apply {
                                deleteAction = {
                                    context.showBasicDialog(
                                            titleResId = R.string.delete_roommate_dialog_title,
                                            message = getString(R.string.delete_roommate_dialog_message, it.name),
                                            positiveFunction = {
                                                viewModel.deleteRoommate(it)
                                            }
                                    )
                                }
                                onClickAction = {
                                    val dialogView = LayoutInflater.from(context).inflate(R.layout.add_roommate_dialog, null)
                                    val inputRoommateName = dialogView.findViewById(R.id.input_roommate_name) as EditText
                                    val inputRoommatePhone = dialogView.findViewById(R.id.input_roommate_phone) as EditText
                                    inputRoommateName.setText(it.name)
                                    inputRoommatePhone.setText(it.phone)

                                    context.showCustomDialog(
                                            customView = dialogView,
                                            titleResId = R.string.edit_roommate_dialog_title,
                                            positiveFunction = {
                                                it.name = inputRoommateName.text.toString()
                                                mainName = inputRoommateName.text.toString()
                                                it.phone = inputRoommatePhone.text.toString()
                                                caption = inputRoommatePhone.text.toString()
                                                roommates_list.adapter.notifyDataSetChanged()
                                            }
                                    )
                                }
                            }
                        })
            }
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
            context.showCustomDialog(
                    customView = dialogView,
                    titleResId = R.string.add_roommate_dialog_title,
                    positiveFunction = {
                        val roommateName = (dialogView.findViewById(R.id.input_roommate_name) as EditText).text.toString()
                        val roommatePhone = (dialogView.findViewById(R.id.input_roommate_phone) as EditText).text.toString()
                        val newRoomate = Person(name = roommateName, phone = roommatePhone)

                        viewModel.addRoommate(newRoomate)
                    }
            )
        }
    }
}
