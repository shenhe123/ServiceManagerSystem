package com.jssg.servicemanagersystem.ui.workorder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityWorkOrderCheckDetailBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.fragment.ReviewWorkOrderDetailFragment
import com.jssg.servicemanagersystem.ui.workorder.fragment.UpdateWorkOrderDetailFragment
import kotlinx.android.parcel.Parcelize

class WorkOrderCheckDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityWorkOrderCheckDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkOrderCheckDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputData = intent?.getParcelableExtra<InputData>("input")
        inputData?.let {
            val beginTransaction = supportFragmentManager.beginTransaction()
            if (it.isReView) {
                beginTransaction
                    .replace(R.id.fm_container, ReviewWorkOrderDetailFragment.newInstance(it.workOrderCheckInfo), "review_work_order_detail")
                    .commit()
            } else {
                beginTransaction
                    .replace(R.id.fm_container, UpdateWorkOrderDetailFragment.newInstance(it.workOrderCheckInfo), "update_work_order_detail")
                    .commit()
            }
        }

        binding.toolBar.setNavigationOnClickListener { finish() }
    }


    class WorkOrderCheckContracts: ActivityResultContract<InputData, Boolean?>(){
        override fun createIntent(context: Context, input: InputData): Intent {
            return Intent(context, WorkOrderCheckDetailActivity::class.java).apply {
                putExtra("input", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else null
        }

    }

    @Parcelize
    data class InputData(
        val workOrderCheckInfo: WorkOrderCheckInfo,
        val isReView: Boolean = false,
    ) : Parcelable
}