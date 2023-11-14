package com.jssg.servicemanagersystem.base

import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.utils.DpPxUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/10/21.
 */
open class BaseSearchDialogFragment: AppCompatDialogFragment() {

    override fun getTheme(): Int = R.style.NoMarginsDialog

    override fun onStart() {
        super.onStart()

        dialog?.window?.let {
            val params = it.attributes
//            params.x = 0
//            params.y = DpPxUtils.dip2px(requireContext(), 136f)
            params.dimAmount = 0.0f //对话框外部透明
            it.attributes = params
            it.setGravity(Gravity.TOP)
            it.setLayout(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.MATCH_PARENT)

        }
    }
}