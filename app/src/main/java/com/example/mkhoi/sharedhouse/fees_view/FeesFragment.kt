package com.example.mkhoi.sharedhouse.fees_view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeFragment

class FeesFragment : Fragment() {
    companion object {
        fun newInstance() = FeesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_fees, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.findViewById(R.id.toolbar) as Toolbar).title = getString(R.string.fees_fragment_title)
        //(activity.findViewById(R.id.progress_bar) as ProgressBar).visibility = View.VISIBLE

        val fab = activity.findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = View.VISIBLE
        fab?.setOnClickListener { view ->
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content_fragment, EditFeeFragment.newInstance())
                    .addToBackStack(EditFeeFragment::class.java.canonicalName)
                    .commit()
        }
    }
}