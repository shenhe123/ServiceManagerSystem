package com.jssg.servicemanagersystem

import android.content.Intent
import android.os.Bundle
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActLoginLayoutBinding
import com.jssg.servicemanagersystem.ui.MainActivity
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class LoginActivity: BaseActivity() {

    private lateinit var binding: ActLoginLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTaskRoot) {
            val action = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                finish()
                return
            }
        }

        binding = ActLoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accountInfo = AccountManager.instance.getPassword()
        accountInfo?.let {
            val accountInfoStr = it.split("+")
            binding.inputUsername.setText(accountInfoStr[0])
            binding.inputPassword.setText(accountInfoStr[1])
        }

        binding.mbtLogin.setOnClickListener {
            val userName = binding.inputUsername.text
            val password = binding.inputPassword.text
            if (userName.isNullOrEmpty() || password.isNullOrEmpty()) {
                ToastUtils.showToast("用户名或密码不能为空")
                return@setOnClickListener
            }

            if (binding.cbRememberPwd.isChecked) { //记住密码
                AccountManager.instance.savePassword(userName.toString(), password.toString())
            }

            MainActivity.goActivity(this)
            finish()
        }
    }


}