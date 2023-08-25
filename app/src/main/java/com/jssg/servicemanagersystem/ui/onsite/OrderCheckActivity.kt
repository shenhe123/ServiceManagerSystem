package com.jssg.servicemanagersystem.ui.onsite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityOrderCheckBinding

class OrderCheckActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, OrderCheckActivity::class.java))
        }
    }

}