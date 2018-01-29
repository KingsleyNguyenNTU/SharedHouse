package com.example.mkhoi.sharedhouse.list_view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R


class ListItemRecyclerViewAdapter<T>(private val data: List<ListItem<T>>) : RecyclerView.Adapter<ListItemRecyclerViewAdapter<T>.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].mainName
        holder.caption.text = data[position].caption

        holder.delete.setOnClickListener{
            data[position].deleteAction.invoke()
        }

        holder.mainView.setOnClickListener {
            data[position].onClickAction.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }


    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val avatar: ImageView
        val name: TextView
        val caption: TextView
        val delete: Button
        val mainView: View

        init {
            mainView = mView
            avatar = mView.findViewById(R.id.list_item_avatar)
            name = mView.findViewById(R.id.list_item_name)
            caption = mView.findViewById(R.id.list_item_caption)
            delete = mView.findViewById(R.id.list_item_delete_btn)
        }
    }
}