package com.jssg.servicemanagersystem.ui.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ActivityProfileInfoBinding

class ProfileInfoActivity : AppCompatActivity() {
    private var editable: Boolean = false
    private lateinit var binding: ActivityProfileInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        updateEditWidgets()

        binding.tvEdit.setOnClickListener {
            editable = !editable
            updateEditWidgets()
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

    private fun updateEditWidgets() {
        binding.etNickname.isEnabled = editable
        binding.etPhoneNum.isEnabled = editable
        binding.etCardId.isEnabled = editable
        binding.etAddress.isEnabled = editable
        binding.etPassword.isEnabled = editable

        val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
        binding.layoutNickname.background = drawable
        binding.layoutPhoneNum.background = drawable
        binding.layoutCardId.background = drawable
        binding.layoutAddress.background = drawable
        binding.layoutPassword.background = drawable

        binding.btnUpdate.isVisible = editable
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