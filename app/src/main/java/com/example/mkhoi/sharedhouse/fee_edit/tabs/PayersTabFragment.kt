package com.example.mkhoi.sharedhouse.fee_edit.tabs

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.FeePayer
import com.example.mkhoi.sharedhouse.database.entity.FeePrepaid
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeViewModel
import com.example.mkhoi.sharedhouse.list_view.ListItem
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.util.getProfilePictureLiveData
import com.example.mkhoi.sharedhouse.util.showCustomDialog
import com.example.mkhoi.sharedhouse.util.showMultipleChoicesDialog
import kotlinx.android.synthetic.main.fragment_edit_fee_payers_tab.*


class PayersTabFragment: Fragment() {
    companion object {
        fun createInstance() = PayersTabFragment()
    }

    internal lateinit var viewModel: EditFeeViewModel
    internal var fab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(EditFeeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_edit_fee_payers_tab, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        payers_list.layoutManager = LinearLayoutManager(context)
        viewModel.feePayers.observe(this, Observer {
            it?.let { updatePayerList(it) }
        })
    }

    private fun updatePayerList(payers: List<FeePayer>) {
        val listItems = payers.filter { it.feePrepaid != null }.map {
            val uriLiveData: MutableLiveData<Uri?> = MutableLiveData()
            it.person.getProfilePictureLiveData(activity!!, uriLiveData)
            ListItem(
                    mainName = it.person.name,
                    caption = getString(R.string.payers_caption_text, String.format("%.2f", it.feePrepaid!!.amount)),
                    profilePicture = uriLiveData
            ).apply {
                onClickAction = {
                    val feePrepaid = it.feePrepaid!!
                    val dialogView = LayoutInflater.from(context).inflate(R.layout.add_feeshare_dialog, null)
                    val inputShareFraction = dialogView.findViewById(R.id.input_splitter_fraction) as EditText
                    inputShareFraction.setText((feePrepaid.amount).toString())

                    context?.showCustomDialog(
                            customView = dialogView,
                            titleResId = R.string.edit_splitter_fraction_dialog_title,
                            positiveFunction = {
                                feePrepaid.amount = inputShareFraction.text.toString().toDouble()
                                updatePayerList(payers)
                            }
                    )
                }
            }
        }

        payers_list.adapter = ListItemRecyclerViewAdapter(data = listItems)
    }

    fun addPayers(){
        val selectedItems: MutableList<Int> = mutableListOf()
        viewModel.feePayers.value?.let {
            Log.d("EditFeeFragment", "Active persons: ${it.map { it.person.name }}")
            val multipleChoices: Array<String> = Array(it.size, {""})
            var index = 0
            it.forEach {
                multipleChoices[index] = it.person.name
                it.feePrepaid?.let {
                    selectedItems.add(index)
                }
                index++
            }

            context?.showMultipleChoicesDialog(
                    titleResId = R.string.add_person_splitter_dialog_title,
                    selectedItems = selectedItems,
                    multipleChoices = multipleChoices,
                    positiveFunction = {
                        updateFeePayers(selectedItems)
                    }
            )
        }
    }

    private fun updateFeePayers(selectedItems: MutableList<Int>) {
        viewModel.feePayers.value?.let {
            for (i in 0 until it.size){
                val feePayer = it[i]
                if (selectedItems.contains(i)){
                    if (feePayer.feePrepaid == null){
                        feePayer.feePrepaid = FeePrepaid(
                                feeId = viewModel.fee.value?.id ?: 0,
                                personId = feePayer.person.id!!,
                                amount = 0.0
                        )
                    }
                }
                else {
                    feePayer.feePrepaid = null
                }
            }

            updatePayerList(it)
        }
    }
}