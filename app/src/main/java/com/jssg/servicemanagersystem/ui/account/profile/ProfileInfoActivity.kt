package com.jssg.servicemanagersystem.ui.account.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityProfileInfoBinding
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

class ProfileInfoActivity : BaseActivity() {
    private lateinit var accountViewModel: AccountViewModel
    private var editable: Boolean = false
    private lateinit var binding: ActivityProfileInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        initView()

        addListener()

        initViewModel()
    }

    private fun initViewModel() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        accountViewModel.userInfoLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                result.data?.let {
                    binding.etNickname.setText(it.user.nickName)
                    binding.etPhoneNum.setText(it.user.phonenumber)
                    binding.etCardId.setText(it.user.idNo)
                    binding.etAddress.setText(it.user.address)
                }
            }
        }

        accountViewModel.updateLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                editable = !editable
                initView()
                ToastUtils.showToast("修改成功")
            }
        }

        accountViewModel.getUserInfo()
    }

    private fun addListener() {
        binding.tvEdit.setOnClickListener {
            if (editable) { //更改个人信息
                val nickname = binding.etNickname.text.toString()
                if (nickname.isEmpty()) {
                    ToastUtils.showToast("用户名不能为空")
                    return@setOnClickListener
                }
                val phoneNumber = binding.etPhoneNum.text.toString()
                if (nickname.isEmpty()) {
                    ToastUtils.showToast("手机号不能为空")
                    return@setOnClickListener
                }
                val cardId = binding.etCardId.text.toString()
                if (nickname.isEmpty()) {
                    ToastUtils.showToast("身份证号不能为空")
                    return@setOnClickListener
                }
                val address = binding.etAddress.text.toString()
                if (nickname.isEmpty()) {
                    ToastUtils.showToast("居住地址不能为空")
                    return@setOnClickListener
                }

                accountViewModel.updateUserInfo(nickname, phoneNumber, cardId, address)
            } else {
                editable = true
                initView()
            }
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
    }

    private fun initView() {
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
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, ProfileInfoActivity::class.java))
        }
    }
}