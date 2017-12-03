package com.example.mkhoi.sharedhouse.room_edit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.entity.Person


class RoommatesRecyclerViewAdapter(private val data: List<Person>) : RecyclerView.Adapter<RoommatesRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.phone.text = data[position].phone
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.roommate_list_item, parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val avatar: ImageView
        val name: TextView
        val phone: TextView

        init {
            avatar = mView.findViewById(R.id.roommate_avatar)
            name = mView.findViewById(R.id.roommate_name)
            phone = mView.findViewById(R.id.roommate_phone)
        }
    }
}