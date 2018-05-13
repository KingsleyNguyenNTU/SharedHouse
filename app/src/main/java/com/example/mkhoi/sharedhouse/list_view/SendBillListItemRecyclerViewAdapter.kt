package com.example.mkhoi.sharedhouse.list_view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.util.toDisplayAmount


class SendBillListItemRecyclerViewAdapter(private val data: List<BillDetailListItem>)
    : RecyclerView.Adapter<SendBillListItemRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val totalPrepaid = data[position].payers.sumByDouble { it -> it.amount.toDouble() }
        if (totalPrepaid > 0){
            val totalAmount = data[position].roommates.sumByDouble { it -> it.amount.toDouble() }
            holder.prepaidRoot.visibility = VISIBLE
            holder.amount.text = totalAmount.toFloat().toDisplayAmount()
            holder.namePrepaid.text = holder.context.resources.getString(R.string.bill_detail_payer_bill_name, data[position].mainName)
            holder.amountPrepaid.text = (0f - totalPrepaid.toFloat()).toDisplayAmount()
        } else {
            holder.prepaidRoot.visibility = GONE
            holder.amount.text = data[position].amount.toDisplayAmount()
        }

        holder.name.text = data[position].mainName

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
        val prepaidRoot = mView.findViewById<View>(R.id.list_item_prepaid)
        val namePrepaid: TextView = mView.findViewById(R.id.list_item_name_prepaid)
        val amountPrepaid: TextView = mView.findViewById(R.id.list_item_amount_prepaid)
    }
}