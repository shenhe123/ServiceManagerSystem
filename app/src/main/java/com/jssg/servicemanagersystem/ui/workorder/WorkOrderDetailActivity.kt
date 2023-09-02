package com.jssg.servicemanagersystem.ui.workorder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityOrderHandleBinding

class WorkOrderDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderHandleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderHandleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        showBack()

        binding.mbtCheckOrder.setOnClickListener {
            WorkOrderCheckActivity.goActivity(this)
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, WorkOrderDetailActivity::class.java))
        }
    }
}