package com.jssg.servicemanagersystem.ui.account.network

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckedTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.get
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.core.Constants
import com.jssg.servicemanagersystem.databinding.ActivityChooseHostBinding
import com.jssg.servicemanagersystem.databinding.ItemCheckedTextViewBinding
import com.jssg.servicemanagersystem.utils.DpPxUtils

class ChooseHostActivity : AppCompatActivity() {
    private var lastChooseHost: String = ""
    private lateinit var binding: ActivityChooseHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        updateHostChoose()
    }

    private fun updateHostChoose() {
        val hostArray = Constants.hostArray
        lastChooseHost = AccountManager.instance.getChooseHost()
        hostArray.forEach {
            addCheckedTextView(it)
        }
    }

    private fun addCheckedTextView(host: String) {
        val checkedView =
            LayoutInflater.from(this).inflate(R.layout.item_checked_text_view, null)
        val bind = ItemCheckedTextViewBinding.bind(checkedView)
        bind.ckLine.layoutParams = LinearLayoutCompat.LayoutParams(-1, DpPxUtils.dip2px(this, 64.0f))
        bind.ckLine.text = host
        bind.ckLine.tag = host
        bind.ckLine.isChecked = TextUtils.equals(lastChooseHost, host)

        bind.ckLine.setOnClickListener {
            if (!TextUtils.equals(lastChooseHost, host)) {
                lastChooseHost = host
                checkMark()

                AccountManager.instance.saveChooseHost(lastChooseHost)
                RetrofitService.recreateRetrofit()
                finish()
            }
        }

        binding.layoutHost.addView(checkedView)
    }

    private fun checkMark() {
        for (i in 0 until binding.layoutHost.childCount) {
            val checkedTextView = binding.layoutHost[0] as CheckedTextView
            checkedTextView.isChecked = lastChooseHost.equals(checkedTextView.tag.toString(), false)
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, ChooseHostActivity::class.java))
        }
    }
}