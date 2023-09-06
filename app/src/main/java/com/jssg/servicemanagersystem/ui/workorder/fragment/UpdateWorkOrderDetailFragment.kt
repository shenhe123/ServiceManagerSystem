package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.utils.RolePermissionUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/6.
 */
class UpdateWorkOrderDetailFragment : BaseWorkOrderCheckFragment() {


    companion object {
        fun newInstance(input: WorkOrderCheckInfo): UpdateWorkOrderDetailFragment {
            val args = Bundle()
            args.putParcelable("input", input)
            val fragment = UpdateWorkOrderDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initViewVisible() {
        binding.layoutNormal.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_EDIT.printableName)

        isPictureLongClickable = true
        isAddPictureEnable = true

        if (inputData?.state == 1) { //已提交，是不能修改为已保存的
            //所以需隐藏保存按钮
            binding.mbtSave.isVisible = false
        } else if (inputData?.state == 2 || inputData?.state == 4) {
            binding.layoutNormal.isVisible = false
            isPictureLongClickable = false
        } else if (inputData?.state == 3) { //退文
            //所以需隐藏保存按钮
            binding.mbtSave.isVisible = false
        }

        binding.ivAddBadPhoto.isVisible = isAddPictureEnable
        binding.ivAddBoxPhoto.isVisible = isAddPictureEnable
        binding.ivAddBatchInfoPhoto.isVisible = isAddPictureEnable
        binding.ivAddReworkPhoto.isVisible = isAddPictureEnable

    }
}