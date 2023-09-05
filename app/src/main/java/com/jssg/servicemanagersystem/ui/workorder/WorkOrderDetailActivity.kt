package com.jssg.servicemanagersystem.ui.workorder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityWorkOrderDetailBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderCheckDetailFragment
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderDetailFragment

class WorkOrderDetailActivity : BaseActivity() {
    private var inputData: WorkOrderInfo? = null
    private lateinit var binding: ActivityWorkOrderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        showBack()

        inputData = intent?.getParcelableExtra("input")

        val tabs = arrayOf("工单详情", "排查详情")

        binding.viewPager2.offscreenPageLimit = 1
        binding.viewPager2.adapter = Vp2(this)

        val mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager2
        ) { tab, position ->

            tab.text = tabs[position]

        }.attach()


    }

    fun goBackForResult() {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("output", true)
        })
        finish()
    }

    inner class Vp2(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    WorkOrderDetailFragment.newInstance(inputData)
                }

                else -> WorkOrderCheckDetailFragment.newInstance(inputData)
            }
        }
    }

    class WorkOrderContracts: ActivityResultContract<WorkOrderInfo, Boolean?>(){
        override fun createIntent(context: Context, input: WorkOrderInfo): Intent {
            return Intent(context, WorkOrderDetailActivity::class.java).apply {
                putExtra("input", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else null
        }

    }

}