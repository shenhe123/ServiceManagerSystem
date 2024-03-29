package com.jssg.servicemanagersystem.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActLoginLayoutBinding
import com.jssg.servicemanagersystem.ui.main.MainActivity
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.account.network.ChooseHostActivity
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class LoginActivity: BaseActivity() {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var loginViewModel: LoginViewModel
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

        if (AccountManager.instance.isLogin()) {
            MainActivity.goActivity(this)
            finish()
        }

        val accountInfo = AccountManager.instance.getPassword()
        accountInfo?.let {
            val accountInfoStr = it.split("+")
            binding.inputUsername.setText(accountInfoStr[0])
            binding.inputPassword.setText(accountInfoStr[1])
        }

        addListener()

        initViewModel()
    }

    private fun initViewModel() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        loginViewModel.loginLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                if (binding.cbRememberPwd.isChecked) { //记住密码
                    //保存用户名和密码
                    AccountManager.instance.savePassword(binding.inputUsername.text.toString(), binding.inputPassword.text.toString())
                }

                result.data?.let {
                    //保存登录状态
                    AccountManager.instance.saveAuthorization(it.token)
                    accountViewModel.getUserProfile()
                }

                MainActivity.goActivity(this)
                finish()
            }
        }

//        dashBoardViewModel.updateLiveData.observe(viewLifecycleOwner) { data: LoadUpdateDataModel<UpdateEntity?> ->
//            if (data.isSuccess) {
//                val mUpdateEntity = data.data
//                val isNeedShowUpdateDialog =
//                    UpdateDialogFragment.isCanShowUpdateDialog(mUpdateEntity)
//                if (isNeedShowUpdateDialog) {
//                    //假如既满足APP推送弹窗，也满足更新弹窗，将APP启动时弹窗的时间置为3个小时后
//                    UpdateDialogFragment.newInstance(mUpdateEntity)
//                        .show(childFragmentManager, "update")
//                    if (NotificationDialogFragment.isNotificationDisable()) {
//                        MMKV.defaultMMKV()!!
//                            .encode(
//                                NotificationDialogFragment.NOTIFICATION_TIME_CANCLE,
//                                System.currentTimeMillis() - 69 * 60 * 60 * 1000
//                            )
//                    }
//                } else {
//                    //如果不弹版本升级弹窗。则检查是否禁用通知
//                    checkIsDisableNotification()
//                }
//            } else if (data.isError) {
//                //不弹 APP推送弹窗，并将下一次APP启动时弹窗的时间置为3个小时后
//                checkIsDisableNotification()
//            }
//        }
    }

    private fun addListener() {

        binding.mbtLogin.setOnClickListener {
            val userName = binding.inputUsername.text
            val password = binding.inputPassword.text
            if (userName.isNullOrEmpty() || password.isNullOrEmpty()) {
                ToastUtils.showToast("用户名或密码不能为空")
                return@setOnClickListener
            }

            loginViewModel.login(userName.toString(), password.toString())
        }

        binding.tvChooseHost.setOnClickListener {
            ChooseHostActivity.goActivity(this)
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            })
        }
    }


}