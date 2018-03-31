package com.example.mkhoi.sharedhouse.settings


import android.app.Activity.RESULT_OK
import android.app.AlertDialog
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
import android.widget.LinearLayout

import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.util.displayRoundImage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.*
import java.lang.Exception

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

        initImagePicker()
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
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    house_profile_picture.setImageBitmap(imageBitmap)
                    house_profile_picture.displayRoundImage(resources)
                }
            }
            OPEN_GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK){
                    Picasso.get()
                            .load(data?.data)
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round)
                            .into(house_profile_picture, object : Callback {
                                override fun onSuccess() {
                                    house_profile_picture.displayRoundImage(resources)
                                }

                                override fun onError(e: Exception?) {
                                    house_profile_picture.setImageResource(R.mipmap.ic_launcher_round)
                                }
                            })

                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
