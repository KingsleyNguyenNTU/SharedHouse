package com.example.mkhoi.sharedhouse.backup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mkhoi.sharedhouse.R
import kotlinx.android.synthetic.main.fragment_restore.*


class RestoreFragment : Fragment() {

    companion object {
        private const val PICK_FILE_RESULT_CODE = 1
        fun newInstance() = RestoreFragment()
    }

    internal lateinit var viewModel: RestoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, RestoreViewModel.Factory())
                .get(RestoreViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_restore, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choose_backup_file_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            this.startActivityForResult(intent, PICK_FILE_RESULT_CODE)
        }

        restore_btn.setOnClickListener{
            context?.let { context -> viewModel.restore(context) }
        }

        viewModel.backupFilePath.observe(this, Observer {
            backup_file_path.text = it?.lastPathSegment
        })

        viewModel.restoreResultLiveData.observe(this, Observer {
            when (it){
                true -> Toast.makeText(activity, R.string.restore_success_msg, Toast.LENGTH_LONG).show()
                false -> Toast.makeText(activity, R.string.restore_fail_msg, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_FILE_RESULT_CODE){
            viewModel.backupFilePath.value = data?.data
        }
        else super.onActivityResult(requestCode, resultCode, data)
    }
}
