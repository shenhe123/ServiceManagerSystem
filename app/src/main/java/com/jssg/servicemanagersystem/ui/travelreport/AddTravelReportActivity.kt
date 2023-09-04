package com.jssg.servicemanagersystem.ui.travelreport

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityAddTravelReportBinding

class AddTravelReportActivity : BaseActivity() {
    private lateinit var binding: ActivityAddTravelReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTravelReportBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    class AddTravelReportContracts: ActivityResultContract<Any, Boolean?>() {
        override fun createIntent(context: Context, input: Any): Intent {
            return Intent(context, AddTravelReportActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else false
        }

    }
}