package com.example.mkhoi.sharedhouse.fee_edit

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.FeeType
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import com.example.mkhoi.sharedhouse.database.bean.ReducedShareType
import com.example.mkhoi.sharedhouse.databinding.FragmentEditFeeBinding
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeActivity.Companion.FEE_BUNDLE_KEY
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeActivity.Companion.SELECTED_MONTH_KEY
import com.example.mkhoi.sharedhouse.fee_edit.tabs.EditFeeViewPagerAdapter
import com.example.mkhoi.sharedhouse.fee_edit.tabs.EditFeeViewPagerAdapter.Companion.PAYER_TAB_POS
import com.example.mkhoi.sharedhouse.fee_edit.tabs.EditFeeViewPagerAdapter.Companion.SPLITTER_TAB_POS
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import kotlinx.android.synthetic.main.fragment_edit_fee.*
import java.util.*


class EditFeeFragment: Fragment() {
    companion object {
        fun newInstance(selectedMonth: Calendar,
                        feeWithSplitters: FeeWithSplitters? = null) = EditFeeFragment().apply {
            arguments = Bundle().apply {
                putSerializable(SELECTED_MONTH_KEY, selectedMonth)
                putParcelable(FEE_BUNDLE_KEY, feeWithSplitters)
            }
        }
    }

    internal lateinit var viewModel: EditFeeViewModel
    private lateinit var binding: FragmentEditFeeBinding
    internal var fab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders
                .of(activity!!, EditFeeViewModel.Factory(
                        arguments?.get(SELECTED_MONTH_KEY) as Calendar,
                        arguments?.get(FEE_BUNDLE_KEY) as? FeeWithSplitters))
                .get(EditFeeViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_edit_fee, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditFeeBinding.bind(view)
        binding.viewModel = viewModel

        initViewModelObserver()
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.action_saving){
            if (viewModel.saveValidation())
                viewModel.save()
            else {
                context?.showBasicDialog(
                        titleResId = R.string.save_fee_fail_title,
                        message = getString(R.string.save_fee_fail_message),
                        positiveFunction = {}
                )
            }
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun initViewModelObserver() {
        viewModel.fee.observe(this, Observer {
            binding.executePendingBindings()
            initDropDownLists()
        })

        viewModel.isSaving.observe(this, Observer {
            when (it) {
                true -> {
                    (activity?.findViewById(R.id.progress_bar) as? ProgressBar)?.visibility = View.VISIBLE
                }
                false -> {
                    activity?.setResult(RESULT_OK)
                    activity?.finish()
                }
            }
        })
    }

    private fun initDropDownLists() {
        val feeTypeAdapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item ,
                FeeType.values().map { it.name.toLowerCase().capitalize() }.toList())
        input_fee_type.setAdapter(feeTypeAdapter)
        input_fee_type.keyListener = null
        input_fee_type.setOnTouchListener {v, _ -> v.let {
            (it as AutoCompleteTextView).showDropDown()
            false
        }}

        val shareTypeAdapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item ,
                ReducedShareType.values().map { it.name.toLowerCase().capitalize() }.toList())
        input_share_type.setAdapter(shareTypeAdapter)
        input_share_type.keyListener = null
        input_share_type.setOnTouchListener {v, _ -> v.let {
            (it as AutoCompleteTextView).showDropDown()
            false
        }}
        input_share_type.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateSplittersListFlag.let {
                    it.value = it.value?.not() ?: true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

        })
    }

    private fun initView() {
        fab = activity?.findViewById(R.id.fab)

        val pagerAdapter = EditFeeViewPagerAdapter(
                fm = childFragmentManager,
                fragment = this)

        fee_view_pager.adapter = pagerAdapter
        fee_tab_layout.setupWithViewPager(fee_view_pager)

        fab?.setOnClickListener{
            when (fee_tab_layout.selectedTabPosition){
                SPLITTER_TAB_POS -> pagerAdapter.splittersTabFragment.addSplitters()
                PAYER_TAB_POS -> pagerAdapter.payersTabFragment.addPayers()
                else -> {}
            }
        }
    }
}