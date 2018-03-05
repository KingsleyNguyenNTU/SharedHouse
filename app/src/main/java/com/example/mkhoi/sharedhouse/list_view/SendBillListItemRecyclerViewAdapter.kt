package com.example.mkhoi.sharedhouse.list_view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.util.toDisplayAmount


class SendBillListItemRecyclerViewAdapter(private val data: List<BillDetailListItem>)
    : RecyclerView.Adapter<SendBillListItemRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].mainName
        holder.amount.text = data[position].amount.toDisplayAmount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.send_bill_item_template, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val context: Context = mView.context
        val name: TextView = mView.findViewById(R.id.list_item_name)
        val amount: TextView = mView.findViewById(R.id.list_item_amount)
    }
}