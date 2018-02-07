package com.example.mkhoi.sharedhouse.monthly_bill

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mkhoi.sharedhouse.R

class MonthlyBillFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle): View?
            = inflater.inflate(R.layout.fragment_monthly_bill_list, container, false)
}
