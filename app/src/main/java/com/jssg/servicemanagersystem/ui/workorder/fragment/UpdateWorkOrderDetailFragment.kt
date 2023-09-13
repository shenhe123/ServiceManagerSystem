package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import android.view.View
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
        val hasPermission =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_EDIT.printableName)
        binding.layoutNormal.isVisible = hasPermission

//        0  "已保存"
//        1  "已提交"
//        2  "已审核"
//        3  "退文"
//        4  "不同意"
        if (inputData?.state == 0) {
            binding.mbtSave.isVisible = true
            binding.mbtSubmit.isVisible = true

            isEditable = true

            isAddPictureEnable = true && hasPermission

            isAddReworkPictureEnable = true && hasPermission

            isPictureLongClickable = true

        } else if (inputData?.state == 1) { //已提交，是不能修改为已保存的,也不能再次更新

            //所以需隐藏保存按钮
            binding.mbtSave.isVisible = false
            binding.mbtSubmit.isVisible = false

            isAddPictureEnable = false

            isPictureLongClickable = false

        } else if (inputData?.state == 2 || inputData?.state == 4) {

            binding.layoutNormal.isVisible = false
            isPictureLongClickable = false
            isAddPictureEnable = false

        } else if (inputData?.state == 3) { //退文

            //所以需隐藏保存按钮
            binding.mbtSave.isVisible = false
            binding.mbtSubmit.isVisible = true

            isAddPictureEnable = true && hasPermission

            isPictureLongClickable = true
        }

        binding.ivAddBadPhoto.isVisible = isAddPictureEnable
        binding.ivAddBoxPhoto.isVisible = isAddPictureEnable
        binding.ivAddBatchInfoPhoto.isVisible = isAddPictureEnable
        binding.ivAddReworkPhoto.isVisible = isAddReworkPictureEnable

    }
}