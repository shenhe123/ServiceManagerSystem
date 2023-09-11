package com.jssg.servicemanagersystem.ui.report.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.databinding.FragmentReportWorkOrderProgressBinding

class ReportWorkOrderProgressFragment : BaseFragment() {

    private lateinit var binding: FragmentReportWorkOrderProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportWorkOrderProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReportWorkOrderProgressFragment().apply {

            }
    }
}