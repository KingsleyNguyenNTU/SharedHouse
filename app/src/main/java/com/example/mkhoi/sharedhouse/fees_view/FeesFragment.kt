package com.example.mkhoi.sharedhouse.fees_view

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ProgressBar
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeActivity
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeFragment
import com.example.mkhoi.sharedhouse.list_view.ListItem
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.room_edit.EditRoomFragment
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import com.example.mkhoi.sharedhouse.util.showMonthPickerDialog
import com.example.mkhoi.sharedhouse.util.showMultipleChoicesDialog
import com.example.mkhoi.sharedhouse.util.toString
import kotlinx.android.synthetic.main.fragment_fees.*
import java.util.*


class FeesFragment : Fragment() {
    companion object {
        private const val EDIT_FEE_CODE = 0

        fun newInstance() = FeesFragment()
    }

    internal lateinit var viewModel: FeesViewModel
    internal var menu: Menu? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, FeesViewModel.Factory())
                .get(FeesViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fees_menu, menu)
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.action_month_picker -> {
                context?.showMonthPickerDialog(viewModel.selectedMonth)
                return true
            }
            R.id.action_copy_fee -> {
                viewModel.fees.value?.let {
                    val selectedItems: MutableList<Int> = mutableListOf()
                    context?.showMultipleChoicesDialog(
                            titleResId = R.string.action_copy_fee_title,
                            multipleChoices = it.map { it.fee.name }.toTypedArray(),
                            selectedItems = selectedItems,
                            positiveFunction = {
                                viewModel.copyFees(selectedItems)
                            }
                    )
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.action_copy_fee)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_fees, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fee_list.layoutManager = LinearLayoutManager(context)
        fee_list.adapter = ListItemRecyclerViewAdapter(emptyList())
        (activity?.findViewById(R.id.progress_bar) as? ProgressBar)?.visibility = View.VISIBLE

        val fab = activity?.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = View.VISIBLE
        fab.setOnClickListener { _ ->
            viewModel.selectedMonth.value?.let {
                startActivityForResult(EditFeeActivity.createIntent(activity!!, it), EDIT_FEE_CODE)
            }
        }

        viewModel.fees.observe(this, Observer {
            it?.let {
                (activity?.findViewById(R.id.progress_bar) as? ProgressBar)?.visibility = View.GONE
                fee_list.adapter = ListItemRecyclerViewAdapter(it.map {
                    ListItem(
                            mainName = it.fee.name,
                            caption = it.fee.displayAmount,
                            profilePictureId = it.fee.feeType.drawableId
                    ).apply {
                        deleteAction = {
                            context?.showBasicDialog(
                                    titleResId = R.string.delete_fee_dialog_title,
                                    message = getString(R.string.delete_fee_dialog_message, mainName),
                                    positiveFunction = {
                                        viewModel.deleteFee(it)
                                    }
                            )
                        }
                        onClickAction = {
                            viewModel.selectedMonth.value?.let {selectedMonth ->
                                startActivityForResult(EditFeeActivity.createIntent(activity!!, selectedMonth, it), EDIT_FEE_CODE)
                            }
                        }
                    }
                })
            }
        })

        viewModel.selectedMonth.observe(this, Observer {
            it?.let {
                (activity?.findViewById(R.id.toolbar) as? Toolbar)?.title = it.toString("MMMM yyyy")
                viewModel.reloadFees(it)
                menu?.findItem(R.id.action_copy_fee)?.isVisible =
                        (it.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                        it.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)).not()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EDIT_FEE_CODE && resultCode == RESULT_OK){
            viewModel.selectedMonth.value?.let { viewModel.reloadFees(it) }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}