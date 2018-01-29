package com.example.mkhoi.sharedhouse.fee_edit

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.FeeSplitter
import com.example.mkhoi.sharedhouse.database.bean.ShareType
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.databinding.FragmentEditFeeBinding
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.util.showCustomDialog
import kotlinx.android.synthetic.main.fragment_edit_fee.*
import kotlinx.android.synthetic.main.fragment_edit_room.*


class EditFeeFragment: Fragment() {
    companion object {
        fun newInstance() = EditFeeFragment()
    }

    internal lateinit var viewModel: EditFeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, EditFeeViewModel.Factory(null))
                .get(EditFeeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_edit_fee, container, false)

        val binding = FragmentEditFeeBinding.bind(view)
        binding.viewModel = this.viewModel

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.findViewById(R.id.toolbar) as Toolbar).title = getString(R.string.edit_fee_fragment_title)

        splitters_list.layoutManager = LinearLayoutManager(context)
        splitters_list.adapter = ListItemRecyclerViewAdapter<FeeSplitter>(emptyList())

        initButtonListener()
    }

    private fun initButtonListener() {
        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = View.GONE

        save_room_btn.setOnClickListener {
            //viewModel.save()
        }

        add_splitters_btn.setOnClickListener{
            when(viewModel.fee.value?.shareType){
                ShareType.SHARE_BY_ROOM_WITHOUT_TIME, ShareType.SHARE_BY_ROOM_WITH_TIME -> {
                    //TODO open dialog to add room
                }

                ShareType.SHARE_BY_PERSON_WITHOUT_TIME, ShareType.SHARE_BY_PERSON_WITH_TIME -> {
                    //TODO open dialog to add person
                }
            }
        }
    }
}