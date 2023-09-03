package com.jssg.servicemanagersystem.ui.workorder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityWorkOrderCheckDetailBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo

class WorkOrderCheckDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityWorkOrderCheckDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkOrderCheckDetailBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_work_order_check_detail)
    }

    companion object {
        fun goActivity(context: Context, input: WorkOrderInfo) {
            context.startActivity(Intent(context, WorkOrderCheckDetailActivity::class.java).apply {
                putExtra("input", input)
            })
        }
    }
}