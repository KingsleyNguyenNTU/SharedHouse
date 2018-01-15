package com.example.mkhoi.sharedhouse.fee_edit

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.databinding.FragmentEditFeeBinding


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
        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            Log.d("EditFeeFragment", viewModel.fee.toString())
        }
    }
}