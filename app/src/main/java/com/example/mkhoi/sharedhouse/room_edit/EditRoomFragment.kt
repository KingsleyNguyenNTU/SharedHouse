package com.example.mkhoi.sharedhouse.room_edit

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.GONE
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ProgressBar
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.databinding.FragmentEditRoomBinding
import com.example.mkhoi.sharedhouse.list_view.ListItem
import com.example.mkhoi.sharedhouse.list_view.ListItemRecyclerViewAdapter
import com.example.mkhoi.sharedhouse.room_edit.EditRoomActivity.Companion.ROOM_BUNDLE_KEY
import com.example.mkhoi.sharedhouse.util.getProfilePictureLiveData
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import com.example.mkhoi.sharedhouse.util.showCustomDialog
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.android.synthetic.main.fragment_edit_room.*
import java.util.*


class EditRoomFragment : Fragment() {

    companion object {
        private val phoneUtil = PhoneNumberUtil.getInstance()

        fun newInstance(room: UnitWithPersons? = null): EditRoomFragment {
            val fragment = EditRoomFragment()
            val arguments = Bundle()
            arguments.putParcelable(ROOM_BUNDLE_KEY, room)
            fragment.arguments = arguments
            return fragment
        }
    }

    internal lateinit var viewModel: EditRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders
                .of(this, EditRoomViewModel.Factory(arguments?.get(ROOM_BUNDLE_KEY) as UnitWithPersons?))
                .get(EditRoomViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_edit_room, container, false)

        val binding = FragmentEditRoomBinding.bind(view)
        binding.viewModel = this.viewModel

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.action_saving){
            viewModel.save()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roommates_list.layoutManager = LinearLayoutManager(context)
        roommates_list.adapter = ListItemRecyclerViewAdapter(emptyList())

        initButtonListener()

        viewModel.roommates.observe(this, Observer {
            roommates_list.adapter = it?.let { roommates ->
                ListItemRecyclerViewAdapter(
                        data = roommates.map {
                            ListItem(mainName = it.name,
                                     caption = it.phone).apply {
                                context?.let { context -> it.getProfilePictureLiveData(context, profilePicture) }
                                deleteAction = {
                                    context?.showBasicDialog(
                                            titleResId = R.string.delete_roommate_dialog_title,
                                            message = getString(R.string.delete_roommate_dialog_message, it.name),
                                            positiveFunction = {
                                                viewModel.deleteRoommate(it)
                                            }
                                    )
                                }
                                onClickAction = {
                                    openEditRoommateDialog(it)
                                }
                            }
                        })
            }
        })

        viewModel.isSaving.observe(this, Observer {
            when (it) {
                true -> {
                    (activity?.findViewById(R.id.progress_bar) as? ProgressBar)?.visibility = View.VISIBLE
                }
                false -> {
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
            }
        })
    }

    private fun initButtonListener() {
        val fab = activity?.findViewById(R.id.fab) as? FloatingActionButton

        fab?.setOnClickListener {
            openAddRoommateDialog()
        }
    }

    private fun openAddRoommateDialog() {
        val dialogView = layoutInflater.inflate(R.layout.add_roommate_dialog, null)

        val countryCodeView = dialogView.findViewById<AutoCompleteTextView>(R.id.input_roommate_country_code)
        prepareCountryCodeList(countryCodeView, Locale.getDefault())

        val dialog = context?.showCustomDialog(
                customView = dialogView,
                titleResId = R.string.add_roommate_dialog_title,
                positiveFunction = {
                    val roommateName = (dialogView.findViewById(R.id.input_roommate_name) as EditText).text.toString()
                    val roommatePhone = (dialogView.findViewById(R.id.input_roommate_phone) as EditText).text.toString()
                    val fullPhoneNumber = "+${countryCodeView.text}$roommatePhone"
                    val newRoommate = Person(name = roommateName, phone = fullPhoneNumber)

                    viewModel.addRoommate(newRoommate)
                }
        )
        dialog?.let { validatePhoneNumber(it, dialogView) }
    }

    private fun ListItem.openEditRoommateDialog(roommate: Person) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_roommate_dialog, null)
        val countryCodeView = dialogView.findViewById<AutoCompleteTextView>(R.id.input_roommate_country_code)
        val inputRoommateName = dialogView.findViewById(R.id.input_roommate_name) as EditText
        val inputRoommatePhone = dialogView.findViewById(R.id.input_roommate_phone) as EditText

        inputRoommateName.setText(roommate.name)

        try {
            val phoneNumber = phoneUtil.parse(roommate.phone, null)
            prepareCountryCodeList(countryCodeView,
                    Locale.Builder().setRegion(
                            phoneUtil.getRegionCodeForNumber(phoneNumber)).build())
            inputRoommatePhone.setText(phoneNumber.nationalNumber.toString())
        } catch (e: Exception) {
            prepareCountryCodeList(countryCodeView, Locale.getDefault())
            inputRoommatePhone.setText(roommate.phone)
        }

        val dialog = context?.showCustomDialog(
                customView = dialogView,
                titleResId = R.string.edit_roommate_dialog_title,
                positiveFunction = {
                    roommate.name = inputRoommateName.text.toString()
                    mainName = inputRoommateName.text.toString()
                    val fullPhoneNumber = "+${countryCodeView.text}${inputRoommatePhone.text}"
                    roommate.phone = fullPhoneNumber
                    caption = roommate.phone
                    roommates_list.adapter.notifyDataSetChanged()
                }
        )
        dialog?.let { validatePhoneNumber(it, dialogView) }
    }

    private fun prepareCountryCodeList(countryCodeView: AutoCompleteTextView, defaultLocale: Locale): AutoCompleteTextView? {
        val phoneCountryCodeAdapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item,
                Locale.getAvailableLocales()
                        .map { "${phoneUtil.getCountryCodeForRegion(it.country)}" }
                        .toSet()
                        .sorted())
        defaultLocale.let {
            countryCodeView.setText("${phoneUtil.getCountryCodeForRegion(it.country)}")
        }
        countryCodeView.setAdapter(phoneCountryCodeAdapter)
        countryCodeView.keyListener = null
        countryCodeView.setOnTouchListener { v, _ ->
            v.let {
                (it as AutoCompleteTextView).showDropDown()
                false
            }
        }
        return countryCodeView
    }

    private fun validatePhoneNumber(dialog: AlertDialog, dialogView: View){
        val phoneNumberView = dialogView.findViewById<EditText>(R.id.input_roommate_phone)
        val countryCodeView = dialogView.findViewById<AutoCompleteTextView>(R.id.input_roommate_country_code)

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = checkPhoneNumber(
                phoneNumber = phoneNumberView.text.toString(),
                countryCode = countryCodeView.text.toString().toInt())

        phoneNumberView.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val validationResult = checkPhoneNumber(
                        phoneNumber = s.toString(),
                        countryCode = countryCodeView.text.toString().toInt())
                if (validationResult){
                    phoneNumberView.error = null
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                } else {
                    phoneNumberView.error = getString(R.string.phone_number_error_msg)
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun checkPhoneNumber(phoneNumber: String, countryCode: Int): Boolean{
        var validationResult: Boolean
        try {
            validationResult = phoneUtil.isValidNumber(phoneUtil.parse(phoneNumber,
                    phoneUtil.getRegionCodeForCountryCode(countryCode)))
        } catch (e: Exception) {
            //fail to parse phone number
            validationResult = false
        }

        return validationResult
    }
}
