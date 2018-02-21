package com.example.mkhoi.sharedhouse.fee_edit

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mkhoi.sharedhouse.R


class EditFeeFragment: Fragment() {
    companion object {
        fun newInstance() = EditFeeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_edit_fee, container, false)
}