package com.example.mkhoi.sharedhouse.fee_edit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.RoomSplitter
import com.example.mkhoi.sharedhouse.database.entity.FeeShare
import com.example.mkhoi.sharedhouse.databinding.FragmentEditFeeBinding
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.util.showMultipleChoicesDialog
import kotlinx.android.synthetic.main.fragment_edit_fee.*


class EditFeeFragment: Fragment() {
    companion object {
        fun newInstance() = EditFeeFragment()
    }

    internal lateinit var viewModel: EditFeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, EditFeeViewModel.Factory(null))
                .get(EditFeeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_edit_fee, container, false)

        val binding = FragmentEditFeeBinding.bind(view)
        binding.viewModel = this.viewModel

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.findViewById(R.id.toolbar) as Toolbar).title = getString(R.string.edit_fee_fragment_title)

        initView()
    }

    private fun initView() {
        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = View.GONE

        save_fee_btn.setOnClickListener {
            //viewModel.save()
        }

        splitters_list.layoutManager = LinearLayoutManager(context)
        splitters_list.adapter = ListItemRecyclerViewAdapter<RoomSplitter>(emptyList())

        viewModel.fee.observe(this, Observer {
            updateAddSplitterBtnListener()
        })

        viewModel.roomSplitters.observe(this, Observer {
            updateAddSplitterBtnListener()
        })
    }

    private fun updateAddSplitterBtnListener() {
        add_splitters_btn.setOnClickListener{
            when(viewModel.fee.value?.isSharedByRoom()){
                true -> {
                    val selectedItems: MutableList<Int> = mutableListOf()
                    viewModel.roomSplitters.value?.let {
                        Log.d("EditFeeFragment", "Active rooms: ${it.map { it.room.name }}")
                        val multipleChoices: Array<String> = Array(it.size, {""})
                        var index = 0
                        it.forEach {
                            multipleChoices[index] = it.room.name
                            index++
                        }

                        context.showMultipleChoicesDialog(
                                titleResId = R.string.add_room_splitter_dialog_title,
                                selectedItems = selectedItems,
                                multipleChoices = multipleChoices,
                                positiveFunction = {
                                    updateRoomSplitters(selectedItems)
                                }
                        )
                    }
                }

                false -> {
                    //TODO open dialog to add person
                }
            }
        }
    }

    private fun updateRoomSplitters(selectedItems: MutableList<Int>) {
        viewModel.roomSplitters.value?.let {
            for (i in 0 until it.size){
                val roomSplitter = it[i]
                if (selectedItems.contains(i)){
                    if (roomSplitter.feeShare == null){
                        roomSplitter.feeShare = FeeShare(
                                feeId = viewModel.fee.value?.id ?: 0,
                                personId = 0,
                                unitId = roomSplitter.room.id!!,
                                share = 0
                        )
                    }
                }
                else {
                    roomSplitter.feeShare = null
                }
                Log.d("EditFeeFragment", "Room Splitter: ${roomSplitter.room.name}, " +
                        "Fee Share: ${roomSplitter.feeShare?.share}")
            }
        }
    }
}