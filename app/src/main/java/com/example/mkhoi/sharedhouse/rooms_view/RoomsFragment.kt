package com.example.mkhoi.sharedhouse.rooms_view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.list_view.ListItem
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.room_edit.EditRoomActivity
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import kotlinx.android.synthetic.main.fragment_room_list.*

class RoomsFragment : Fragment() {

    companion object {
        fun newInstance()= RoomsFragment()
    }

    internal lateinit var viewModel: RoomsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, RoomsViewModel.Factory())
                .get(RoomsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_room_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        room_list.layoutManager = LinearLayoutManager(context)
        room_list.adapter = ListItemRecyclerViewAdapter(emptyList())
        (activity?.findViewById(R.id.progress_bar) as? ProgressBar)?.visibility = View.VISIBLE

        viewModel.rooms.observe(this, Observer {
            it?.let {
                (activity?.findViewById(R.id.progress_bar) as? ProgressBar)?.visibility = GONE
                room_list.adapter = ListItemRecyclerViewAdapter(it.map {
                    ListItem(
                        mainName = it.unit.name,
                        caption = resources.getQuantityString(R.plurals.unitSize,
                                it.roommates?.size?:0,
                                it.roommates?.size?:0)
                    ).apply {
                        context?.let { context -> it.getProfilePictureLiveData(context, profilePicture) }
                        deleteAction = {
                            context?.showBasicDialog(
                                    titleResId = R.string.delete_room_dialog_title,
                                    message = getString(R.string.delete_room_dialog_message, mainName),
                                    positiveFunction = {
                                        viewModel.deleteRoom(it)
                                    }
                            )
                        }
                        onClickAction = {
                            startActivity(EditRoomActivity.createIntent(activity!!, it))
                        }
                    }
                })
            }
        })
    }
}
