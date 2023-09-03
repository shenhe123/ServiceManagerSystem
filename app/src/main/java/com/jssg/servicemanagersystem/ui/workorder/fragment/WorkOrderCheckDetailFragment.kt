package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo

class WorkOrderCheckDetailFragment : Fragment() {
    private var inputData: WorkOrderInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            inputData = it.getParcelable("inputData")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_order_check_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(inputData: WorkOrderInfo?) =
            WorkOrderCheckDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("inputData", inputData)
                }
            }
    }
}