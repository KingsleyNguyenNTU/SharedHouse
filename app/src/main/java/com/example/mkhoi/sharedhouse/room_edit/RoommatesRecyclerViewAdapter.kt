package com.example.mkhoi.sharedhouse.room_edit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import com.example.mkhoi.sharedhouse.util.showCustomDialog


class RoommatesRecyclerViewAdapter(private val data: List<Person>,
                                   private val fragment: EditRoomFragment) : RecyclerView.Adapter<RoommatesRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.phone.text = data[position].phone

        holder.delete.setOnClickListener{
            fragment.context.showBasicDialog(
                    titleResId = R.string.delete_roommate_dialog_title,
                    message = fragment.getString(
                            R.string.delete_roommate_dialog_message, data[position].name),
                    positiveFunction = {
                        fragment.viewModel.deleteRoommate(data[position])
                    }
            )
        }

        holder.mainView.setOnClickListener {
            val dialogView = LayoutInflater.from(fragment.context).inflate(R.layout.add_roommate_dialog, null)
            val inputRoommateName = dialogView.findViewById(R.id.input_roommate_name) as EditText
            val inputRoommatePhone = dialogView.findViewById(R.id.input_roommate_phone) as EditText
            inputRoommateName.setText(data[position].name)
            inputRoommatePhone.setText(data[position].phone)

            fragment.context.showCustomDialog(
                    customView = dialogView,
                    titleResId = R.string.edit_roommate_dialog_title,
                    positiveFunction = {
                        data[position].name = inputRoommateName.text.toString()
                        data[position].phone = inputRoommatePhone.text.toString()
                        fragment.updateRoommate()
                    }
            )
        }
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
        val delete: Button
        val mainView: View

        init {
            mainView = mView
            avatar = mView.findViewById(R.id.roommate_avatar)
            name = mView.findViewById(R.id.roommate_name)
            phone = mView.findViewById(R.id.roommate_phone)
            delete = mView.findViewById(R.id.roommate_delete_btn)
        }
    }
}