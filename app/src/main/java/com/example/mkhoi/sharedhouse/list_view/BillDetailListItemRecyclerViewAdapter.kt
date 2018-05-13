package com.example.mkhoi.sharedhouse.list_view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.util.toDisplayAmount


class BillDetailListItemRecyclerViewAdapter(private val data: List<BillDetailListItem>)
    : RecyclerView.Adapter<BillDetailListItemRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].mainName
        holder.amount.text = data[position].amount.toDisplayAmount()
        holder.detailList.layoutManager = LinearLayoutManager(holder.context)
        holder.detailList.adapter = BillRoommateListItemRecyclerViewAdapter(data[position].roommates)

        data[position].payers.apply {
            if (isEmpty()){
                holder.payerListRoot.visibility = GONE
            } else {
                holder.payerListRoot.visibility = VISIBLE
                holder.payersList.layoutManager = LinearLayoutManager(holder.context)
                holder.payersList.adapter = BillRoommateListItemRecyclerViewAdapter(this)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bill_detail_list_item, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val context: Context = mView.context
        val name: TextView = mView.findViewById(R.id.list_item_name)
        val amount: TextView = mView.findViewById(R.id.list_item_amount)
        val detailList: RecyclerView = mView.findViewById(R.id.bill_detail_roommate_list)
        val payersList: RecyclerView = mView.findViewById(R.id.bill_detail_payers_list)
        val payerListRoot: View = mView.findViewById(R.id.bill_detail_payers_list_root)

    }
}