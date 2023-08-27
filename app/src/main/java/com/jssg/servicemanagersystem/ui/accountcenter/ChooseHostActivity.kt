package com.jssg.servicemanagersystem.ui.accountcenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jssg.servicemanagersystem.BuildConfig
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActivityChooseHostBinding

class ChooseHostActivity : AppCompatActivity(), View.OnClickListener {
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

        binding.ckLine2.setOnClickListener(this)
        binding.ckLine1.setOnClickListener(this)
    }

    private fun updateHostChoose() {
//        hosts = HostManger.getHostService().getHostArray(false)
        lastChooseHost = AccountManager.instance.getChoosehost() ?: "192.168.1.1"
//        if (hosts == null || hosts.size < 2) {
//            finish()
//            ToastUtils.show("host_error")
//            return
//        }

        binding.ckLine1.tag = "192.168.1.1"
        binding.ckLine2.tag = "192.168.1.2"
        checkMark()
    }

    private fun checkMark() {
        binding.ckLine1.isChecked = TextUtils.equals(lastChooseHost, binding.ckLine1.tag.toString())
        binding.ckLine2.isChecked = TextUtils.equals(lastChooseHost, binding.ckLine2.tag.toString())
    }

    override fun onClick(v: View) {
        if (!TextUtils.equals(lastChooseHost, v.tag.toString())) {
            lastChooseHost = v.tag.toString()
//            HostManger.getHostService().changeNowUseHost(lastChooseHost, false)
            checkMark()

            AccountManager.instance.saveChooseHost(lastChooseHost)
            finish()
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, ChooseHostActivity::class.java))
        }
    }
}