package com.jssg.servicemanagersystem.ui.account.role

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ActivityRoleManagerBinding

class RoleManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoleManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoleManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, RoleManagerActivity::class.java))
        }
    }
}