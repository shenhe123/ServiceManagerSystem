package com.jssg.servicemanagersystem.ui.onsite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityOrderHandleBinding

class OrderHandleActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderHandleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderHandleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        showBack()


    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, OrderHandleActivity::class.java))
        }
    }
}