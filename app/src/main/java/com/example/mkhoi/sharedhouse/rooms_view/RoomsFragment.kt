package com.example.mkhoi.sharedhouse.rooms_view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup

import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.room_edit.EditRoomFragment
import com.example.mkhoi.sharedhouse.rooms_view.dummy.DummyContent
import kotlinx.android.synthetic.main.app_bar_main.*

class RoomsFragment : Fragment() {
    // TODO: Customize parameters
    private var mColumnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            view.adapter = MyRoomRecyclerViewAdapter(DummyContent.ITEMS)
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = VISIBLE
        fab?.setOnClickListener { view ->
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content_fragment, EditRoomFragment.newInstance())
                    .addToBackStack(EditRoomFragment::class.java.canonicalName)
                    .commit()
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): RoomsFragment {
            val fragment = RoomsFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
