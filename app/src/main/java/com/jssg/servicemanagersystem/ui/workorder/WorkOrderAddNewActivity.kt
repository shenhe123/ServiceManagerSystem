package com.jssg.servicemanagersystem.ui.workorder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ActivityWorkOrderAddNewBinding
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

class WorkOrderAddNewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkOrderAddNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkOrderAddNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.mbtSubmit.setOnClickListener {
            ToastUtils.showToast("提交成功")
            finish()
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, WorkOrderAddNewActivity::class.java))
        }
    }
}