package com.jssg.servicemanagersystem

import android.os.Bundle
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActLoginLayoutBinding
import com.jssg.servicemanagersystem.ui.MainActivity

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class LoginActivity: BaseActivity() {

    private lateinit var binding: ActLoginLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActLoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mbtLogin.setOnClickListener {
            MainActivity.goActivity(this)
        }
    }


}