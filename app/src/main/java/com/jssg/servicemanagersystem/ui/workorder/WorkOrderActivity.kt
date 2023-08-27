package com.jssg.servicemanagersystem.ui.workorder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityWorkOrderBinding

class WorkOrderActivity : BaseActivity() {
    private lateinit var binding: ActivityWorkOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        showBack()


    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, WorkOrderActivity::class.java))
        }
    }
}