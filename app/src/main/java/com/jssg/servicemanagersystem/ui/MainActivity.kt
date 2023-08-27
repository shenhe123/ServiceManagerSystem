package com.jssg.servicemanagersystem.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityMainBinding
import com.jssg.servicemanagersystem.ui.accountcenter.AccountFragment
import com.jssg.servicemanagersystem.ui.onsite.OnsiteOptionsFragment
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment

class MainActivity : BaseActivity() {

    private lateinit var homePageAdapter: HomePageAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homePageAdapter = HomePageAdapter(this)
        binding.viewPager.adapter = homePageAdapter

        //禁止左右滑动
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.navView.menu.getItem(position).isChecked = true
            }
        })

        binding.navView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_dashboard -> binding.viewPager.setCurrentItem(0, false)
                R.id.navigation_home -> binding.viewPager.setCurrentItem(1, false)
                R.id.navigation_travel_report -> binding.viewPager.setCurrentItem(2, false)
                R.id.navigation_report -> binding.viewPager.setCurrentItem(3, false)
                R.id.navigation_mine -> binding.viewPager.setCurrentItem(4, false)
            }

            true
        }
    }

    private inner class HomePageAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return OnsiteOptionsFragment.newInstance()
                1 -> return WorkOrderFragment.newInstance()
                2 -> return WorkOrderFragment.newInstance()
                3 -> return WorkOrderFragment.newInstance()
                4-> return AccountFragment.newInstance()
            }
            return OnsiteOptionsFragment.newInstance()
        }


        override fun getItemCount(): Int {
            return 5
        }
    }


    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}