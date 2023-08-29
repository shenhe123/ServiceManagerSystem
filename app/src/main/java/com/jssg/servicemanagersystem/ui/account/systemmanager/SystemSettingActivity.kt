package com.jssg.servicemanagersystem.ui.account.systemmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jssg.servicemanagersystem.databinding.ActivitySystemSettingBinding
import com.jssg.servicemanagersystem.ui.account.systemmanager.network.ChooseHostActivity
import com.jssg.servicemanagersystem.ui.account.systemmanager.usermanager.UserManagerActivity

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

        binding.tvNetworkManager.setOnClickListener {
            ChooseHostActivity.goActivity(this)
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, SystemSettingActivity::class.java))
        }
    }
}