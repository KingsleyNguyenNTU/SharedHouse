package com.example.mkhoi.sharedhouse.list_view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import android.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import com.example.mkhoi.sharedhouse.util.displayRoundImage


class ListItemRecyclerViewAdapter(private val data: List<ListItem>) : RecyclerView.Adapter<ListItemRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].mainName
        holder.caption.text = data[position].caption

        if (data[position].deleteAction == null){
            holder.delete.visibility = GONE
        }

        holder.delete.setOnClickListener{
            data[position].deleteAction?.invoke()
        }

        holder.mainView.setOnClickListener {
            data[position].onClickAction.invoke()
        }

        Picasso.with(holder.mainView.context)
                .load(data[position].profilePicture)
                .placeholder(R.drawable.ic_account_circle_grey_24dp)
                .error(R.drawable.ic_account_circle_grey_24dp)
                .into(holder.avatar, object : Callback{
                    override fun onSuccess() {
                        holder.avatar.displayRoundImage(holder.mainView.context.resources)
                    }

                    override fun onError() {
                        holder.avatar.setImageResource(R.drawable.ic_account_circle_grey_24dp)
                    }

                })
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