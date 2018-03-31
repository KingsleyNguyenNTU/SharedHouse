package com.example.mkhoi.sharedhouse.monthly_bill

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.View.GONE
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.list_view.BillListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.util.showMonthPickerDialog
import com.example.mkhoi.sharedhouse.util.toString
import kotlinx.android.synthetic.main.fragment_monthly_bill_list.*

class MonthlyBillFragment : Fragment() {

    companion object {
        fun newInstance() = MonthlyBillFragment()
    }

    internal lateinit var viewModel: MonthlyBillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, MonthlyBillViewModel.Factory())
                .get(MonthlyBillViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.monthly_bill_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_month_picker){
            context?.showMonthPickerDialog(viewModel.selectedMonth)
            return true
        }
        else return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_monthly_bill_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = getString(R.string.bill_fragment_title)
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = GONE

        monthly_bill_list.layoutManager = LinearLayoutManager(context)
        monthly_bill_list.adapter = BillListItemRecyclerViewAdapter(emptyList())

        viewModel.monthlyBillList.observe(this, Observer {
            it?.let {
                monthly_bill_list.adapter = BillListItemRecyclerViewAdapter(it)
            }
        })

        viewModel.selectedMonth.observe(this, Observer {
            it?.let {
                activity?.findViewById<Toolbar>(R.id.toolbar)?.title = it.toString("MMMM yyyy")
                viewModel.reloadMonthlyBills(it)
            }
        })
    }
}
