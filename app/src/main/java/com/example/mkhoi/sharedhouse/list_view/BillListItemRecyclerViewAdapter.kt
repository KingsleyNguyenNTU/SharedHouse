package com.example.mkhoi.sharedhouse.list_view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.util.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class BillListItemRecyclerViewAdapter(private val data: List<BillListItem>)
    : RecyclerView.Adapter<BillListItemRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].mainName
        holder.amount.text = data[position].amount.toDisplayAmount()
        holder.detailList.layoutManager = LinearLayoutManager(holder.context)
        holder.detailList.adapter = BillDetailListItemRecyclerViewAdapter(data[position].billDetails)

        data[position].profilePicture.observeForever {
            Picasso.with(holder.context)
                    .load(it)
                    .placeholder(R.drawable.ic_account_circle_grey_24dp)
                    .error(R.drawable.ic_account_circle_grey_24dp)
                    .into(holder.avatar, object : Callback {
                        override fun onSuccess() {
                            holder.avatar.displayRoundImage(holder.context.resources)
                        }

                        override fun onError() {
                            holder.avatar.setImageResource(R.drawable.ic_account_circle_grey_24dp)
                        }

                    })
        }

        holder.sendBtn.setOnClickListener {
            holder.context.showBasicDialog(
                    titleResId = R.string.send_bill_dialog_title,
                    message = holder.context.getString(R.string.send_bill_dialog_msg, data[position].mainName),
                    positiveFunction = {
                        sendBill(holder.context, data[position])
                    }
            )
        }
    }

    private fun sendBill(context: Context, billListItem: BillListItem) {
        val billImage = prepareBill(context, billListItem)

        billListItem.phoneNumbers.forEach {
            val whatsappPhone = "$it@s.whatsapp.net"
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.`package` = "com.whatsapp"
            sendIntent.putExtra(Intent.EXTRA_STREAM, billImage);
            sendIntent.type = "image/jpeg";
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sendIntent.putExtra("jid", whatsappPhone)
            context.startActivity(sendIntent)
        }
    }

    private fun prepareBill(context: Context, billListItem: BillListItem): Uri {
        val layout = LayoutInflater.from(context).inflate(R.layout.send_bill_template, null)
                as LinearLayout

        layout.findViewById<RecyclerView>(R.id.send_bill_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SendBillListItemRecyclerViewAdapter(billListItem.billDetails)
        }

        layout.findViewById<TextView>(R.id.total_amount).text = billListItem.amount.toDisplayAmount()

        return layout.toImage().toUri(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bill_list_item, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val context: Context = mView.context
        val avatar: ImageView = mView.findViewById(R.id.list_item_avatar)
        val name: TextView = mView.findViewById(R.id.list_item_name)
        val amount: TextView = mView.findViewById(R.id.list_item_amount)
        val detailList: RecyclerView = mView.findViewById(R.id.bill_detail_list)
        val sendBtn: ImageView = mView.findViewById(R.id.list_item_send)

    }
}