package com.jssg.servicemanagersystem.base

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.utils.DpPxUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
open class BaseDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        val lp = dialog.window!!.attributes
        lp.width = DpPxUtils.dip2px(context, resources.getDimension(R.dimen.dialog_width))
        return dialog
    }

}