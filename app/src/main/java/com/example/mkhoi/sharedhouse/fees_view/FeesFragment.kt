package com.example.mkhoi.sharedhouse.fees_view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.entity.Fee
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeFragment
import com.example.mkhoi.sharedhouse.list_view.ListItem
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.room_edit.EditRoomFragment
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import kotlinx.android.synthetic.main.fragment_fees.*

class FeesFragment : Fragment() {
    companion object {
        fun newInstance() = FeesFragment()
    }

    internal lateinit var viewModel: FeesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, FeesViewModel.Factory())
                .get(FeesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_fees, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.findViewById(R.id.toolbar) as Toolbar).title = getString(R.string.fees_fragment_title)

        fee_list.layoutManager = LinearLayoutManager(context)
        fee_list.adapter = ListItemRecyclerViewAdapter<Fee>(emptyList())
        (activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = View.VISIBLE

        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = View.VISIBLE
        fab.setOnClickListener { view ->
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content_fragment, EditFeeFragment.newInstance())
                    .addToBackStack(EditFeeFragment::class.java.canonicalName)
                    .commit()
        }

        viewModel.fees.observe(this, Observer {
            it?.let {
                (activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = View.GONE
                fee_list.adapter = ListItemRecyclerViewAdapter<Fee>(it.map {
                    ListItem<Fee>(
                            mainName = it.fee.name,
                            caption = it.fee.displayAmount
                    ).apply {
                        deleteAction = {
                            context.showBasicDialog(
                                    titleResId = R.string.delete_fee_dialog_title,
                                    message = getString(R.string.delete_fee_dialog_message, mainName),
                                    positiveFunction = {
                                        viewModel.deleteFee(it)
                                    }
                            )
                        }
                        onClickAction = {
                            activity.supportFragmentManager.beginTransaction()
                                    .replace(R.id.main_content_fragment, EditFeeFragment.newInstance(it))
                                    .addToBackStack(EditRoomFragment::class.java.canonicalName)
                                    .commit()
                        }
                    }
                })
            }
        })
    }
}