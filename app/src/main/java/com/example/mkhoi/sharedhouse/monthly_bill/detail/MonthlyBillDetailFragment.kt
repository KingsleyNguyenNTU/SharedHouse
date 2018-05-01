package com.example.mkhoi.sharedhouse.monthly_bill.detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.list_view.BillDetailListItem
import com.example.mkhoi.sharedhouse.list_view.BillDetailListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.monthly_bill.detail.MonthlyBillDetailActivity.Companion.BILL_DETAIL_LIST_KEY
import kotlinx.android.synthetic.main.fragment_monthly_bill_detail_list.*


class MonthlyBillDetailFragment : Fragment() {

    companion object {

        fun newInstance(billDetailList: ArrayList<BillDetailListItem>) = MonthlyBillDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(BILL_DETAIL_LIST_KEY, billDetailList)
            }
        }
    }

    internal lateinit var viewModel: MonthlyBillDetailViewModel
    internal lateinit var listAdapter: BillDetailListItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, MonthlyBillDetailViewModel.Factory(
                arguments?.getParcelableArrayList<BillDetailListItem>(BILL_DETAIL_LIST_KEY)?.toList()))
                .get(MonthlyBillDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_monthly_bill_detail_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monthly_bill_detail_list.layoutManager = LinearLayoutManager(context)
        listAdapter = BillDetailListItemRecyclerViewAdapter(viewModel.billDetailList)
        monthly_bill_detail_list.adapter = listAdapter
    }
}