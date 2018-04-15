package com.example.mkhoi.sharedhouse.fee_edit.tabs

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeViewModel


class PayersTabFragment: Fragment() {
    companion object {
        fun createInstance() = PayersTabFragment()
    }

    internal lateinit var viewModel: EditFeeViewModel
    internal var fab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(EditFeeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_edit_fee_payers_tab, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}