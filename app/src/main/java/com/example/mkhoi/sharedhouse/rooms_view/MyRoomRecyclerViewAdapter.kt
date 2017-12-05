package com.example.mkhoi.sharedhouse.rooms_view

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.room_edit.EditRoomFragment

class MyRoomRecyclerViewAdapter(val data: List<UnitWithPersons>,
                                val activity: FragmentActivity) :
        RecyclerView.Adapter<MyRoomRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].unit.name
        holder.size.text = activity.resources.getQuantityString(R.plurals.unitSize,
                data[position].roommates?.size?:0,
                data[position].roommates?.size?:0)

        holder.itemView.setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content_fragment, EditRoomFragment.newInstance(data[position]))
                    .addToBackStack(EditRoomFragment::class.java.canonicalName)
                    .commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_room_item, parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val avatar: ImageView
        val name: TextView
        val size: TextView

        init {
            avatar = mView.findViewById(R.id.room_avatar)
            name = mView.findViewById(R.id.room_name)
            size = mView.findViewById(R.id.room_size)
        }
    }
}
