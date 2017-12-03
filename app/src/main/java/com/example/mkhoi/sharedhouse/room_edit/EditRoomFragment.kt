package com.example.mkhoi.sharedhouse.room_edit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mkhoi.sharedhouse.R
import kotlinx.android.synthetic.main.fragment_edit_room.*

class EditRoomFragment : Fragment() {

    companion object {
        fun newInstance(): EditRoomFragment {
            val fragment = EditRoomFragment()
            return fragment
        }
    }

    internal lateinit var viewModel: EditRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, EditRoomViewModel.Factory())
                .get(EditRoomViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_edit_room, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roommates_list.layoutManager = LinearLayoutManager(context)

        save_room_btn.setOnClickListener {
            viewModel.save()
        }

        viewModel.roommates.observe(this, Observer {
            roommates_list.adapter = RoommatesRecyclerViewAdapter(it ?: emptyList())
        })

        //TODO implement an dialog to add new roommate
    }
}
