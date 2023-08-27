package com.jssg.servicemanagersystem.ui.accountcenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ActivitySystemSettingBinding

class SystemSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySystemSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySystemSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        binding.tvUserManager.setOnClickListener {
            UserManagerActivity.goActivity(this)
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, SystemSettingActivity::class.java))
        }
    }
}