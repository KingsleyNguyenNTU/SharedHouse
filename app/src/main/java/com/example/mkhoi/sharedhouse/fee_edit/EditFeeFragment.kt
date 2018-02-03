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
import com.example.mkhoi.sharedhouse.database.bean.FeeSplitter
import com.example.mkhoi.sharedhouse.database.bean.ShareType
import com.example.mkhoi.sharedhouse.database.entity.FeeShare
import com.example.mkhoi.sharedhouse.database.entity.Unit
import com.example.mkhoi.sharedhouse.databinding.FragmentEditFeeBinding
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.util.showMultipleChoicesDialog
import kotlinx.android.synthetic.main.fragment_edit_fee.*
import kotlinx.android.synthetic.main.fragment_edit_room.*
import kotlin.collections.HashMap


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

        splitters_list.layoutManager = LinearLayoutManager(context)
        splitters_list.adapter = ListItemRecyclerViewAdapter<FeeSplitter>(emptyList())

        initButtonListener()

        viewModel.roomSplitters.observe(this, Observer {
            Log.d("EditFeeFragment", "Splitters Room: $it")
            viewModel.inactiveRoomSplitters.value?.putAll(viewModel.activeRoomSplitters.value!!)
            viewModel.activeRoomSplitters.value = mutableMapOf()
            it?.forEach {
                addSelectRoomToActiveList(it)
            }

            Log.d("EditFeeFragment", "inactiveRoomSplitters: ${viewModel.inactiveRoomSplitters.value}")
            Log.d("EditFeeFragment", "activeRoomSplitters: ${viewModel.activeRoomSplitters.value}")
        })
    }

    private fun addSelectRoomToActiveList(selectedRoom: Unit){
        viewModel.inactiveRoomSplitters.value?.let {inActiveList ->
            inActiveList[selectedRoom.id]?.let {
                viewModel.activeRoomSplitters.value!!.put(it.feeShare.unitId, it)
                inActiveList.remove(it.feeShare.unitId)
            }

            if (inActiveList[selectedRoom.id] == null){
                //create new fee splitter for this room
                val newFeeSplitter = FeeSplitter(feeShare = FeeShare(
                        id = null,
                        feeId = viewModel.fee.value?.id ?: 0,
                        personId = 0,
                        unitId = selectedRoom.id!!,
                        share = 0
                )).apply {
                    splitterAsUnit = selectedRoom
                }

                viewModel.activeRoomSplitters.value!!.put(newFeeSplitter.feeShare.unitId, newFeeSplitter)
            }
        }
    }

    private fun initButtonListener() {
        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = View.GONE

        save_fee_btn.setOnClickListener {
            //viewModel.save()
        }

        add_splitters_btn.setOnClickListener{
            when(viewModel.fee.value?.isSharedByRoom()){
                true -> {
                    val selectedItems: MutableList<Int> = mutableListOf()
                    val listOptions: HashMap<Int, Unit> = HashMap()

                    viewModel.activeRooms.observe(this, Observer {
                        Log.d("EditFeeFragment", "Active rooms: $it")
                        var index = 0
                        it?.map {
                            listOptions.put(index, it)
                            index++
                        }
                        val multipleChoices: Array<String> = Array(listOptions.size, {""})
                        listOptions.map { multipleChoices[it.key] = it.value.name }

                        context.showMultipleChoicesDialog(
                                titleResId = R.string.add_room_splitter_dialog_title,
                                selectedItems = selectedItems,
                                multipleChoices = multipleChoices,
                                positiveFunction = {
                                    viewModel.roomSplitters.value = listOptions.filter { selectedItems.contains(it.key) }.map { it.value }
                                }
                        )
                    })
                }

                false -> {
                    //TODO open dialog to add person
                }
            }
        }
    }
}