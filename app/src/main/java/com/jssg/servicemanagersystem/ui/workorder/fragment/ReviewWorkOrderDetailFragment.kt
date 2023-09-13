package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.ui.workorder.entity.AudioRecordEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/6.
 */
class ReviewWorkOrderDetailFragment: BaseWorkOrderCheckFragment() {


    override fun initViewVisible() {
        binding.layoutReview.isVisible = true

        isPictureLongClickable = false

    }

    companion object {
        fun newInstance(input: WorkOrderCheckInfo): ReviewWorkOrderDetailFragment {
            val args = Bundle()
            args.putParcelable("input", input)
            val fragment = ReviewWorkOrderDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}