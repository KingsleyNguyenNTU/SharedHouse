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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ProgressBar
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.FeeType
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import com.example.mkhoi.sharedhouse.database.bean.ReducedShareType
import com.example.mkhoi.sharedhouse.database.bean.RoomSplitter
import com.example.mkhoi.sharedhouse.database.entity.FeeShare
import com.example.mkhoi.sharedhouse.databinding.FragmentEditFeeBinding
import com.example.mkhoi.sharedhouse.list_view.ListItem
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.util.showCustomDialog
import com.example.mkhoi.sharedhouse.util.showMultipleChoicesDialog
import kotlinx.android.synthetic.main.fragment_edit_fee.*


class EditFeeFragment: Fragment() {
    companion object {
        private const val FEE_BUNDLE_KEY = "FEE_BUNDLE_KEY"

        fun newInstance(feeWithSplitters: FeeWithSplitters? = null) = EditFeeFragment().apply {
            arguments = Bundle().apply { putParcelable(FEE_BUNDLE_KEY, feeWithSplitters) }
        }
    }

    internal lateinit var viewModel: EditFeeViewModel
    private lateinit var binding: FragmentEditFeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, EditFeeViewModel.Factory(arguments[FEE_BUNDLE_KEY] as? FeeWithSplitters))
                .get(EditFeeViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_edit_fee, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditFeeBinding.bind(view)
        binding.viewModel = viewModel
        (activity.findViewById(R.id.toolbar) as Toolbar).title = getString(R.string.edit_fee_fragment_title)

        initViewModelObserver()
        initView()
    }

    private fun initViewModelObserver() {
        viewModel.fee.observe(this, Observer {
            binding.executePendingBindings()
            initDropDownLists()
            updateAddSplitterBtnListener()
        })

        viewModel.roomSplitters.observe(this, Observer {
            updateAddSplitterBtnListener()
            reloadRoomSplitterList()
        })

        viewModel.isSaving.observe(this, Observer {
            when (it) {
                true -> {
                    save_fee_btn.isEnabled = false
                    (activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = View.VISIBLE
                }
                false -> {
                    (activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = View.GONE
                    activity.supportFragmentManager.popBackStack()
                }
            }
        })
    }

    private fun initDropDownLists() {
        val feeTypeAdapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item ,
                FeeType.values().map { it.name.toLowerCase().capitalize() }.toList())
        input_fee_type.setAdapter(feeTypeAdapter)
        input_fee_type.keyListener = null
        input_fee_type.setOnTouchListener {v, _ -> v.let {
            (it as AutoCompleteTextView).showDropDown()
            false
        }}

        val shareTypeAdapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item ,
                ReducedShareType.values().map { it.name.toLowerCase().capitalize() }.toList())
        input_share_type.setAdapter(shareTypeAdapter)
        input_share_type.keyListener = null
        input_share_type.setOnTouchListener {v, _ -> v.let {
            (it as AutoCompleteTextView).showDropDown()
            false
        }}
    }

    private fun initView() {
        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = View.GONE

        save_fee_btn.setOnClickListener {
            viewModel.save()
        }

        splitters_list.layoutManager = LinearLayoutManager(context)
        splitters_list.adapter = ListItemRecyclerViewAdapter<RoomSplitter>(emptyList())
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
                            it.feeShare?.let {
                                selectedItems.add(index)
                            }
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
                                share = 1
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

        reloadRoomSplitterList()
    }

    private fun reloadRoomSplitterList(){
        val totalShare = viewModel.roomSplitters.value?.map { it.feeShare?.share ?:0 }?.sum() ?: 0
        val totalExpense = viewModel.fee.value?.amount ?: 0.0

        splitters_list.adapter = viewModel.roomSplitters.value?.let { roomSplitters ->
            ListItemRecyclerViewAdapter(
                    data = roomSplitters.filter { it.feeShare != null }.map {
                        val shareAmount = it.feeShare!!.share.toDouble()*totalExpense/totalShare.toDouble()
                        ListItem<RoomSplitter>(
                                mainName = it.room.name,
                                caption = getString(R.string.splitter_caption_text,
                                        String.format("%.2f", shareAmount),
                                        it.feeShare!!.share,
                                        totalShare)
                        ).apply {
                            deleteAction = null
                            onClickAction = {
                                val dialogView = LayoutInflater.from(context).inflate(R.layout.add_feeshare_dialog, null)
                                val inputShareFraction = dialogView.findViewById(R.id.input_splitter_fraction) as EditText
                                inputShareFraction.setText((it.feeShare?.share ?: 1).toString())

                                context.showCustomDialog(
                                        customView = dialogView,
                                        titleResId = R.string.edit_roommate_dialog_title,
                                        positiveFunction = {
                                            it.feeShare?.share = inputShareFraction.text.toString().toInt()
                                            reloadRoomSplitterList()
                                        }
                                )
                            }
                        }
                    })
        }
    }
}