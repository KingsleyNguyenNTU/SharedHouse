package com.example.mkhoi.sharedhouse.list_view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import com.example.mkhoi.sharedhouse.util.toDisplayAmount


class BillListItemRecyclerViewAdapter(private val data: List<BillListItem>)
    : RecyclerView.Adapter<BillListItemRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].mainName
        holder.amount.text = data[position].amount.toDisplayAmount()
        holder.detailList.layoutManager = LinearLayoutManager(holder.context)
        holder.detailList.adapter = BillDetailListItemRecyclerViewAdapter(data[position].billDetails)

        holder.sendBtn.setOnClickListener {
            holder.context.showBasicDialog(
                    titleResId = R.string.send_bill_dialog_title,
                    message = holder.context.getString(R.string.send_bill_dialog_msg, data[position].mainName),
                    positiveFunction = {
                        sendBill()
                    }
            )
        }
    }

    private fun sendBill(){
        //TODO prepare image of bill then send via Whatsapp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bill_list_item, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val context: Context
        val avatar: ImageView
        val name: TextView
        val amount: TextView
        val detailList: RecyclerView
        val sendBtn: ImageView

        init {
            context = mView.context
            avatar = mView.findViewById(R.id.list_item_avatar)
            name = mView.findViewById(R.id.list_item_name)
            amount = mView.findViewById(R.id.list_item_amount)
            detailList = mView.findViewById(R.id.bill_detail_list)
            sendBtn = mView.findViewById(R.id.list_item_send)
        }
    }
}