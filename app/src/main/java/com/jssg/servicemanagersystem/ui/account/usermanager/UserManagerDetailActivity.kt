package com.jssg.servicemanagersystem.ui.account.usermanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityUserManagerDetailBinding
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

class UserManagerDetailActivity : BaseActivity() {
    private var user: User? = null
    private lateinit var accountViewModel: AccountViewModel
    private var editable: Boolean = false
    private lateinit var binding: ActivityUserManagerDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserManagerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        user = intent?.getParcelableExtra("data")
        user?.let {
            updateUserInfo(it)
        }

        initView()

        initViewModel()
    }

    private fun updateUserInfo(it: User) {
        binding.etNickname.setText(it.nickName)
        binding.etCardId.setText(it.idNo)
        binding.etPhoneNum.setText(it.phonenumber)
        binding.etAddress.setText(it.address)
        binding.etExpiredDate.setText(it.expireDate)
        binding.etCreatePerson.setText(it.createBy)
        binding.etCreateTime.setText(it.createTime)
        binding.etUpdatePerson.setText(it.updateBy)
        binding.etUpdateTime.setText(it.updateTime)
    }

    private fun initViewModel() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        accountViewModel.updateUserInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                ToastUtils.showToast(result.msg)

                user?.let {
                    accountViewModel.getUserInfo(it.userId)
                }
            } else if (result.isError) {
                ToastUtils.showToast(result.msg)
            }
        }

        accountViewModel.userInfoLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                result.data?.let {
                    updateUserInfo(it.user)
                }
            }
        }

        user?.let {
            accountViewModel.getUserInfo(it.userId)
        }
    }

    private fun initView() {

        updateEditWidgets()

        binding.tvEdit.setOnClickListener {
            editable = !editable
            updateEditWidgets()
            binding.layoutPasswordRoot.isVisible = editable
        }

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.etNickname.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutNickname.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutNickname.background = drawable
            if (hasFocus) {
                setLast(binding.etNickname)
            }
        }

        binding.etPhoneNum.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutPhoneNum.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutPhoneNum.background = drawable
            if (hasFocus) {
                setLast(binding.etPhoneNum)
            }
        }

        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutPassword.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutPassword.background = drawable
            if (hasFocus) {
                setLast(binding.etPassword)
            }
        }

        binding.etAddress.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutAddress.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutAddress.background = drawable
            if (hasFocus) {
                setLast(binding.etAddress)
            }
        }

        binding.etCardId.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutCardId.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutCardId.background = drawable
            if (hasFocus) {
                setLast(binding.etCardId)
            }
        }

        binding.etExpiredDate.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutExpiredDate.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutExpiredDate.background = drawable
            if (hasFocus) {
                setLast(binding.etExpiredDate)
            }
        }

        binding.btnUpdate.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            if (nickname.isEmpty()) {
                ToastUtils.showToast("用户名不能为空")
                return@setOnClickListener
            }

            val phoneNumber = binding.etPhoneNum.text.toString()
            if (phoneNumber.isEmpty()) {
                ToastUtils.showToast("手机号不能为空")
                return@setOnClickListener
            }

            val cardId = binding.etCardId.text.toString()
            if (cardId.isEmpty()) {
                ToastUtils.showToast("身份证号不能为空")
                return@setOnClickListener
            }

            val address = binding.etAddress.text.toString()
            if (address.isEmpty()) {
                ToastUtils.showToast("居住地址不能为空")
                return@setOnClickListener
            }

            val expiredDate = binding.etExpiredDate.text.toString()
            if (expiredDate.isEmpty()) {
                ToastUtils.showToast("有效日期不能为空")
                return@setOnClickListener
            }

            val password = binding.etPassword.text.toString()

            user?.let {
                it.nickName = nickname
                it.phonenumber = phoneNumber
                it.idNo = cardId
                it.address = address
                it.expireDate = expiredDate
                accountViewModel.updateUserInfo(it.roleIds, it, password)
            }
        }
    }

    private fun updateEditWidgets() {
        binding.etNickname.isEnabled = editable
        binding.etPhoneNum.isEnabled = editable
        binding.etCardId.isEnabled = editable
        binding.etAddress.isEnabled = editable
        binding.etPassword.isEnabled = editable

        updateLayoutBackground(binding.layoutNickname)
        updateLayoutBackground(binding.layoutPhoneNum)
        updateLayoutBackground(binding.layoutPassword)
        updateLayoutBackground(binding.layoutCardId)
        updateLayoutBackground(binding.layoutAddress)
        updateLayoutBackground(binding.layoutExpiredDate)

        binding.btnUpdate.isVisible = editable
    }

    private fun updateLayoutBackground(layout: LinearLayoutCompat) {
        val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
        layout.background = drawable
    }

    private fun setLast(edit: EditText) {
        val len = edit.text?.length
        edit.setSelection(len ?: 0)
    }

    companion object {
        fun goActivity(context: Context, user: User) {
            context.startActivity(Intent(context, UserManagerDetailActivity::class.java).apply {
                putExtra("data", user)
            })
        }
    }

    class UserDetailContracts: ActivityResultContract<User, User?>() {
        override fun createIntent(context: Context, input: User): Intent {
            return Intent(context, UserManagerDetailActivity::class.java).apply {
                putExtra("input_data", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): User? {
            val newUser = intent?.getParcelableExtra<User>("output")
            return if (resultCode == Activity.RESULT_OK) {
                newUser
            } else null
        }

    }
}