package com.jssg.servicemanagersystem.ui.account.updatepwd

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActivityUpdatePasswordBinding
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.login.LoginActivity
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import kotlin.math.log

class UpdatePasswordActivity : BaseActivity() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var binding: ActivityUpdatePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        binding.toolBar.setNavigationOnClickListener { finish() }

        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        accountViewModel.updatePasswordLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("修改密码成功")

                val password = AccountManager.instance.getPassword()
                val loginName = password?.split("+")?.get(0)
                loginName?.let {
                    AccountManager.instance.savePassword(loginName, "")
                }
                AccountManager.instance.logout()
                LoginActivity.goActivity(this)
            }
        }

        binding.mbtConfirm.setOnClickListener {
            val oldPwd = binding.etOldPassword.text.toString()
            val newPwd = binding.etNewPassword.text.toString()
            val confirmPwd = binding.etConfirmPassword.text.toString()

            if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
                ToastUtils.showToast("密码不能为空")

                return@setOnClickListener
            }

            if (newPwd.length < 6 || confirmPwd.length < 6) {
                ToastUtils.showToast("密码长度不能小于6位")

                return@setOnClickListener
            }

            if (!newPwd.equals(confirmPwd, false)) {
                ToastUtils.showToast("两次密码输入不一致")
                return@setOnClickListener
            }

            accountViewModel.updatePassword(oldPwd, newPwd)
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, UpdatePasswordActivity::class.java))
        }
    }
}