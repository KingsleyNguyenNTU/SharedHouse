package com.example.mkhoi.sharedhouse.fee_edit.tabs

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeFragment


class EditFeeViewPagerAdapter(fm: FragmentManager?,
                              val fragment: EditFeeFragment) : FragmentPagerAdapter(fm) {

    companion object {
        private const val TABS_SIZE =2

        const val SPLITTER_TAB_POS = 0
        const val PAYER_TAB_POS = 1
    }

    val splittersTabFragment = SplittersTabFragment.createInstance()
    val payersTabFragment =  PayersTabFragment.createInstance()

    override fun getItem(position: Int): Fragment {
        return when (position){
            SPLITTER_TAB_POS -> splittersTabFragment
            else -> payersTabFragment
        }
    }

    override fun getCount() = TABS_SIZE

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            SPLITTER_TAB_POS -> fragment.context?.getString(R.string.edit_fee_splitters_text_label)
            PAYER_TAB_POS -> fragment.context?.getString(R.string.edit_fee_payerers_text_label)
            else -> super.getPageTitle(position)
        }
    }
}