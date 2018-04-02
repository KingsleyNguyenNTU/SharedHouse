package com.example.mkhoi.sharedhouse.backup

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mkhoi.sharedhouse.MainActivity
import com.example.mkhoi.sharedhouse.R
import kotlinx.android.synthetic.main.fragment_backup.*

class BackupFragment : Fragment() {

    companion object {
        fun newInstance() = BackupFragment()
    }

    internal lateinit var viewModel: BackupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, activity?.let { BackupViewModel.Factory(it) })
                .get(BackupViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_backup, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = getString(R.string.backup_fragment_title)

        backup_btn.setOnClickListener{
            val mainActivity = activity as MainActivity
            if (mainActivity.hasWriteExternalPermission()) {
                viewModel.backup()
            } else ActivityCompat.requestPermissions(mainActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MainActivity.WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
        }

        viewModel.backupResultLiveData.observe(this, Observer {
            when (it){
                true -> Toast.makeText(activity, R.string.backup_success_msg, Toast.LENGTH_LONG).show()
                false -> Toast.makeText(activity, R.string.backup_fail_msg, Toast.LENGTH_LONG).show()
            }
        })
    }
}
