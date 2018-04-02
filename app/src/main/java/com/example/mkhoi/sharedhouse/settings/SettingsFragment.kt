package com.example.mkhoi.sharedhouse.settings


import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.database.bean.SettingKey
import com.example.mkhoi.sharedhouse.database.entity.Setting
import com.example.mkhoi.sharedhouse.database.entity.Setting.Companion.IMAGE_MAX_SIZE
import com.example.mkhoi.sharedhouse.util.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    companion object {

        private const val TAKE_PICTURE_REQUEST_CODE = 10
        private const val OPEN_GALLERY_REQUEST_CODE = 11

        fun newInstance() = SettingsFragment()
    }

    internal lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, SettingsViewModel.Factory())
                .get(SettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = getString(R.string.settings_fragment_title)

        initViewModelObservers()
        initImagePicker()
        initListener()
    }

    private fun initListener() {
        house_name_edit_btn.setOnClickListener {
            openTextInputDialog(SettingKey.HOUSE_NAME, viewModel.houseNameSetting.value?.value)
        }
    }

    private fun initViewModelObservers() {
        viewModel.housePictureSetting.observe(this, Observer {
            it?.let {
                val imageBitmap = CircleImageTransformation().transform(it.value.toBitmapFromBase64())
                house_profile_picture.setImageBitmap(imageBitmap)
            }
        })

        viewModel.houseNameSetting.observe(this, Observer {
            it?.let { house_name_value.text = it.value }
        })
    }

    private fun initImagePicker() {
        val imagePickerView = LayoutInflater.from(context).inflate(R.layout.image_picker_dialog, null)
        val imagePickerDialog = AlertDialog.Builder(context)
                .setTitle(R.string.image_picker_dialog_title)
                .setView(imagePickerView)
                .create()

        house_profile_picture_setting.setOnClickListener{
            imagePickerDialog.show()
        }

        imagePickerView.findViewById<LinearLayout>(R.id.image_picker_camera).setOnClickListener {
            Log.d("ImagePicker","Open camera")
            imagePickerDialog.dismiss()
            this.startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PICTURE_REQUEST_CODE)
        }

        imagePickerView.findViewById<LinearLayout>(R.id.image_picker_gallery).setOnClickListener {
            Log.d("ImagePicker","Open gallery")
            imagePickerDialog.dismiss()
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            this.startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode){
            TAKE_PICTURE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK){
                    (data?.extras?.get("data") as? Bitmap)?.let {
                        //reduced image size and make it circle before storing into DB
                        val circlePicture = it.reduceSize(IMAGE_MAX_SIZE, CircleImageTransformation())
                        val newSetting = Setting(SettingKey.HOUSE_PICTURE, circlePicture.toBase64String())
                        viewModel.saveSetting(newSetting)
                    }

                }
            }
            OPEN_GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK){
                    context?.let {
                        data?.data?.toBitmap(it)?.let {
                            val newSetting = Setting(SettingKey.HOUSE_PICTURE, it.toBase64String())
                            viewModel.saveSetting(newSetting)
                        }
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openTextInputDialog(settingKey: SettingKey, initValue: String?){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.input_setting_dialog, null)
        val inputSettingEditText = dialogView.findViewById<EditText>(R.id.input_setting_edit_text)
        inputSettingEditText.setText(initValue)

        context?.showCustomDialog(
                customView = dialogView,
                positiveFunction = {
                    val newSetting = Setting(settingKey, inputSettingEditText.text.toString())
                    viewModel.saveSetting(newSetting)
                },
                titleResId = settingKey.labelKey
        )
    }
}
