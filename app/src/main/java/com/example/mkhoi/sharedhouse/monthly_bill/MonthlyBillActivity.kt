package com.example.mkhoi.sharedhouse.monthly_bill

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.mkhoi.sharedhouse.MainActivity
import com.example.mkhoi.sharedhouse.R
import com.example.mkhoi.sharedhouse.util.showBasicDialog


class MonthlyBillActivity: MainActivity() {

    companion object {
        const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0
        private const val READ_CONTACTS_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.findFragmentById(R.id.main_content_fragment) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_content_fragment, MonthlyBillFragment.newInstance())
                    .commitNow()
        }

        if(hasReadContactsPermission().not()) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    READ_CONTACTS_REQUEST_CODE)
        }
    }

    fun hasWriteExternalPermission() = (ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

    private fun hasReadContactsPermission() = (ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (hasWriteExternalPermission().not()){
                    showBasicDialog(
                            titleResId = R.string.permission_required_dialog_title,
                            message = getString(R.string.write_external_storage_dialog_msg),
                            positiveFunction = { ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE)}
                    )
                }
            }
            READ_CONTACTS_REQUEST_CODE -> {
                if (hasReadContactsPermission().not()){
                    showBasicDialog(
                            titleResId = R.string.permission_required_dialog_title,
                            message = getString(R.string.read_contact_dialog_msg),
                            positiveFunction = { ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.READ_CONTACTS),
                                    READ_CONTACTS_REQUEST_CODE)}
                    )
                }
            }
        }
    }
}