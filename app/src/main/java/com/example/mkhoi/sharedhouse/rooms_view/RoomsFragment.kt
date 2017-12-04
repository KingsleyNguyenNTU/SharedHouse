package com.example.mkhoi.sharedhouse.rooms_view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.room_edit.EditRoomFragment
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
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_room_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        room_list.layoutManager = LinearLayoutManager(context)
        room_list.adapter = MyRoomRecyclerViewAdapter(emptyList(), context)
        (activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = View.VISIBLE

        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = VISIBLE
        fab?.setOnClickListener { view ->
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content_fragment, EditRoomFragment.newInstance())
                    .addToBackStack(EditRoomFragment::class.java.canonicalName)
                    .commit()
        }

        viewModel.rooms.observe(this, Observer {
            it?.let {
                (activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = GONE
                room_list.adapter = MyRoomRecyclerViewAdapter(it, context)
            }
        })
    }
}
