package com.jssg.servicemanagersystem.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.base.loadmodel.LoadUpdateDataModel
import com.jssg.servicemanagersystem.core.AppApplication
import com.jssg.servicemanagersystem.databinding.ActivityMainBinding
import com.jssg.servicemanagersystem.ui.account.AccountFragment
import com.jssg.servicemanagersystem.ui.main.update.UpdateDialogFragment
import com.jssg.servicemanagersystem.ui.report.ReportFragment
import com.jssg.servicemanagersystem.ui.travelreport.TravelReportFragment
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment

class MainActivity : BaseActivity() {

    private lateinit var homePageAdapter: HomePageAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppApplication.get().getMainViewModel().updateInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data?.let {
                    val isNeedShowUpdateDialog: Boolean = UpdateDialogFragment.isCanShowUpdateDialog(it)

                    if (isNeedShowUpdateDialog) {
                        //更新弹窗，假如是非强制更新，则下次提醒时间为1天后
                        UpdateDialogFragment.newInstance(it).show(supportFragmentManager, "update_dialog")
                    }

                }
            }
        }

        AppApplication.get().getMainViewModel().getUpdateInfo(false)

        homePageAdapter = HomePageAdapter(this)
        binding.viewPager.adapter = homePageAdapter

        //禁止左右滑动
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.navView.menu.getItem(position).isChecked = true
            }
        })

        binding.navView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_dashboard -> binding.viewPager.setCurrentItem(0, false)
                R.id.navigation_travel_report -> binding.viewPager.setCurrentItem(1, false)
                R.id.navigation_report -> binding.viewPager.setCurrentItem(2, false)
                R.id.navigation_mine -> binding.viewPager.setCurrentItem(3, false)
            }

            true
        }
    }

    private inner class HomePageAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return WorkOrderFragment.newInstance()
                1 -> return TravelReportFragment.newInstance()
                2 -> return ReportFragment.newInstance()
                3-> return AccountFragment.newInstance()
            }
            return WorkOrderFragment.newInstance()
        }


        override fun getItemCount(): Int {
            return 5
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        AppApplication.get().getMainViewModel().updateInfoLiveData.value =
            LoadUpdateDataModel(false)
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}