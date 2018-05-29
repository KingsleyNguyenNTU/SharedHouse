package com.example.mkhoi.sharedhouse.fee_edit.tabs

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.entity.FeeShare
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeViewModel
import com.example.mkhoi.sharedhouse.list_view.ListItem
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.util.getProfilePictureLiveData
import com.example.mkhoi.sharedhouse.util.showCustomDialog
import com.example.mkhoi.sharedhouse.util.showMultipleChoicesDialog
import kotlinx.android.synthetic.main.fragment_edit_fee_splitters_tab.*


class SplittersTabFragment: Fragment() {
    companion object {
        fun createInstance() = SplittersTabFragment()
    }

    internal lateinit var viewModel: EditFeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(EditFeeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_edit_fee_splitters_tab, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        splitters_list.layoutManager = LinearLayoutManager(context)
        viewModel.roomSplitters.observe(this, Observer {
            updateSplitterList()
        })

        viewModel.personSplitters.observe(this, Observer {
            updateSplitterList()
        })

        viewModel.updateSplittersListFlag.observe(this, Observer {
            updateSplitterList()
        })
    }

    fun addSplitters() {
        when(viewModel.fee.value?.isSharedByRoom()){
            true -> {
                val selectedItems: MutableList<Int> = mutableListOf()
                viewModel.roomSplitters.value?.let {
                    Log.d("EditFeeFragment", "Active rooms: ${it.map { it.roomWithRoommates.unit.name }}")
                    val multipleChoices: Array<String> = Array(it.size, {""})
                    var index = 0
                    it.forEach {
                        multipleChoices[index] = it.roomWithRoommates.unit.name
                        it.feeShare?.let {
                            selectedItems.add(index)
                        }
                        index++
                    }

                    context?.showMultipleChoicesDialog(
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
                val selectedItems: MutableList<Int> = mutableListOf()
                viewModel.personSplitters.value?.let {
                    Log.d("EditFeeFragment", "Active persons: ${it.map { it.person.name }}")
                    val multipleChoices: Array<String> = Array(it.size, {""})
                    var index = 0
                    it.forEach {
                        multipleChoices[index] = it.person.name
                        it.feeShare?.let {
                            selectedItems.add(index)
                        }
                        index++
                    }

                    context?.showMultipleChoicesDialog(
                            titleResId = R.string.add_person_splitter_dialog_title,
                            selectedItems = selectedItems,
                            multipleChoices = multipleChoices,
                            positiveFunction = {
                                updatePersonSplitters(selectedItems)
                            }
                    )
                }
            }
        }
    }

    private fun updatePersonSplitters(selectedItems: MutableList<Int>) {
        viewModel.personSplitters.value?.let {
            for (i in 0 until it.size){
                val personSplitter = it[i]
                if (selectedItems.contains(i)){
                    if (personSplitter.feeShare == null){
                        personSplitter.feeShare = FeeShare(
                                feeId = viewModel.fee.value?.id ?: 0,
                                personId = personSplitter.person.id!!,
                                unitId = 0,
                                share = 1
                        )
                    }
                }
                else {
                    personSplitter.feeShare = null
                }
                Log.d("EditFeeFragment", "Person Splitter: ${personSplitter.person.name}, " +
                        "Fee Share: ${personSplitter.feeShare?.share}")
            }
        }

        updateSplitterList()
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
                                unitId = roomSplitter.roomWithRoommates.unit.id!!,
                                share = 1
                        )
                    }
                }
                else {
                    roomSplitter.feeShare = null
                }
                Log.d("EditFeeFragment", "Room Splitter: ${roomSplitter.roomWithRoommates.unit.name}, " +
                        "Fee Share: ${roomSplitter.feeShare?.share}")
            }
        }

        updateSplitterList()
    }

    private fun updateSplitterList() {
        when (viewModel.fee.value?.isSharedByRoom()){
            true -> {
                viewModel.roomSplitters.value?.let {
                    val dataList = it.map{
                        val uriLiveData: MutableLiveData<Uri?> = MutableLiveData()
                        context?.let { context -> it.roomWithRoommates.getProfilePictureLiveData(context, uriLiveData) }
                        it.feeShare?.let { feeShare ->
                            Pair(
                                    first = it.roomWithRoommates.unit.name,
                                    second = Pair(feeShare, uriLiveData))
                        }
                    }
                    reloadSplitterList(dataList.filterNotNull())
                }
            }
            false -> {
                viewModel.personSplitters.value?.let {
                    val dataList = it.map{
                        val uriLiveData: MutableLiveData<Uri?> = MutableLiveData()
                        context?.let { context -> it.person.getProfilePictureLiveData(context, uriLiveData) }
                        it.feeShare?.let { feeShare ->
                            Pair(
                                    first = it.person.name,
                                    second = Pair(feeShare, uriLiveData))
                        }
                    }
                    reloadSplitterList(dataList.filterNotNull())
                }
            }
        }
    }

    private fun reloadSplitterList(dataList: List<Pair<String, Pair<FeeShare, MutableLiveData<Uri?>>>>){
        val totalShare = dataList.map { it.second.first.share }.sum()
        val totalExpense = viewModel.fee.value?.amount ?: 0.0

        splitters_list.adapter = ListItemRecyclerViewAdapter(
                data = dataList.map {
                    val shareAmount = it.second.first.share.toDouble()*totalExpense/totalShare.toDouble()
                    ListItem(
                            mainName = it.first,
                            caption = getString(R.string.splitter_caption_text,
                                    String.format("%.2f", shareAmount),
                                    it.second.first.share,
                                    totalShare),
                            profilePicture = it.second.second
                    ).apply {
                        deleteAction = null
                        onClickAction = {
                            val dialogView = LayoutInflater.from(context).inflate(R.layout.add_feeshare_dialog, null)
                            val inputShareFraction = dialogView.findViewById(R.id.input_splitter_fraction) as EditText
                            inputShareFraction.setText((it.second.first.share).toString())

                            context?.showCustomDialog(
                                    customView = dialogView,
                                    titleResId = R.string.edit_splitter_fraction_dialog_title,
                                    positiveFunction = {
                                        it.second.first.share = inputShareFraction.text.toString().toInt()
                                        updateSplitterList()
                                    }
                            )
                        }
                    }
                })
    }
}