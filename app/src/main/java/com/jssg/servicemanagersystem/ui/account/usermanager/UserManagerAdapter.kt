package com.jssg.servicemanagersystem.ui.account.usermanager

import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemUserManagerLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.User
import java.math.BigDecimal

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class UserManagerAdapter: BaseBindingAdapter<User, ItemUserManagerLayoutBinding>(ItemUserManagerLayoutBinding::inflate) {

    init {
        addChildClickViewIds(R.id.card_layout, R.id.mbt_permission, R.id.mbt_delete)
    }

    override fun convert(holder: VBViewHolder<ItemUserManagerLayoutBinding>, item: User) {
        holder.binding.tvName.text = "用户名：${item.nickName}"
        holder.binding.tvPhone.text = "联系方式：${item.phonenumber}"
        holder.binding.tvUserType.isVisible = true
        if (item.admin) {
            holder.binding.tvUserType.text = "用户类型：管理员"
        } else {
            if (item.userType.equals("sys_user")) {
                holder.binding.tvUserType.text = "用户类型：集团用户"
            } else if (item.userType.equals("factory_user")) {
                holder.binding.tvUserType.text = "用户类型：工厂用户"
            } else if (item.userType.equals("end_user")) {
                holder.binding.tvUserType.text = "用户类型：三方用户"
            } else {
                holder.binding.tvUserType.isVisible = false
            }
        }
        holder.binding.tvFactory.text = "所属工厂：${item.sysOrganizationVos?.joinToString(",") { it.orgShortName } ?: ""}"
        holder.binding.tvExpiredDate.text = "有效期至：${item.expireDate}"

        //userId小于100的用户，不允许编辑和修改
        holder.binding.layoutOptionBtn.isVisible = item.userId >= 100

        holder.binding.mbtPermission.isVisible = item.userType?.equals("end_user") == false
    }

}