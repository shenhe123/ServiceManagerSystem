package com.jssg.servicemanagersystem.ui.account.logmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.ActivityLogManagerBinding
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.logmanager.adapter.LoginLogManagerAdapter
import com.jssg.servicemanagersystem.ui.account.logmanager.fragment.LoginLogFragment
import com.jssg.servicemanagersystem.ui.account.logmanager.fragment.OptionLogFragment
import com.jssg.servicemanagersystem.ui.account.logmanager.viewmodel.LogManagerViewModel
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderCheckListFragment
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderDetailFragment
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import kotlinx.android.parcel.Parcelize

class LogManagerActivity : BaseActivity() {

    private lateinit var binding: ActivityLogManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        val tabs = arrayOf("登录日志", "操作日志")

        binding.viewPager2.offscreenPageLimit = 1
        binding.viewPager2.adapter = Vp2(this)

        val mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager2
        ) { tab, position ->

            tab.text = tabs[position]

        }.attach()
    }


    inner class Vp2(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    LoginLogFragment.newInstance()
                }

                else -> OptionLogFragment.newInstance()
            }
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, LogManagerActivity::class.java)).apply {

            }
        }
    }
}