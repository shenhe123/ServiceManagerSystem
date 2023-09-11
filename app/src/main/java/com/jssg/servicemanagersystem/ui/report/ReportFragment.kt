package com.jssg.servicemanagersystem.ui.report

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.databinding.FragmentReportBinding
import com.jssg.servicemanagersystem.ui.report.fragment.ReportTravelReportFragment
import com.jssg.servicemanagersystem.ui.report.fragment.ReportWorkOrderProgressFragment
import com.jssg.servicemanagersystem.ui.report.viewmodel.ReportViewModel
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderCheckListFragment
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderDetailFragment

class ReportFragment : BaseFragment() {

    companion object {
        fun newInstance() = ReportFragment().apply {
            Bundle().apply {

            }
        }
    }

    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]

        val tabs = arrayOf("出差报告", "派工单进度")

        binding.viewPager2.offscreenPageLimit = 1
        binding.viewPager2.adapter = Vp2(this)

        val mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager2
        ) { tab, position ->

            tab.text = tabs[position]

        }.attach()
    }

    inner class Vp2(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ReportTravelReportFragment.newInstance()
                }

                else -> ReportWorkOrderProgressFragment.newInstance()
            }
        }
    }
}