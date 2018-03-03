package com.example.mkhoi.sharedhouse.list_view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.util.toDisplayAmount


class BillRoommateListItemRecyclerViewAdapter(private val data: List<BillRoommateListItem>)
    : RecyclerView.Adapter<BillRoommateListItemRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].mainName
        holder.amount.text = data[position].amount.toDisplayAmount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bill_simple_detail_list_item, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val name: TextView
        val amount: TextView

        init {
            name = mView.findViewById(R.id.list_item_name)
            amount = mView.findViewById(R.id.list_item_amount)
        }
    }
}