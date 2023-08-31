package com.jssg.servicemanagersystem.ui.account.role

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ActivityRoleManagerBinding
import com.jssg.servicemanagersystem.ui.account.role.adapter.AddNewRoleAdapter
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleChildChildEntity
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleParentEntity
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.jssg.servicemanagersystem.widgets.decoration.ThemeLineItemDecoration

class RoleManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoleManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoleManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.setNavigationOnClickListener { finish() }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(ThemeLineItemDecoration())

        val mList = initData()

        val adapter = AddNewRoleAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setNewInstance(mList)

        binding.mbtConfirm.setOnClickListener {

            val roleName = binding.etRoleName.text.toString()
            val remark = binding.etRemark.text.toString()

            if (roleName.isEmpty()) {
                ToastUtils.showToast("角色名称不能为空")
                return@setOnClickListener
            }

            val menuIds = mutableListOf<Long>()
            adapter.data.forEach {
                if (it is RoleParentEntity) {
                    it.childNode?.forEach { child ->
                        if ((child as RoleChildChildEntity).checked) {
                            menuIds.add(child.id)
                        }
                    }
                }
            }

            if (menuIds.isEmpty()) {
                ToastUtils.showToast("请选择菜单权限")
                return@setOnClickListener
            }


        }
    }

    private fun initData(): MutableList<BaseNode> {
        val list = mutableListOf<BaseNode>()

        //派工单
        val workOrderDetailP = RoleParentEntity("派工单管理")
        workOrderDetailP.isExpanded = false
        val workOrderDetailTitles = arrayOf(
            "派工单明细新增",
            "派工单明细修改",
            "派工单明细查询",
            "派工单明细删除",
            "派工单明细审核"
        )
        val workOrderDetailIds = arrayOf(
            1692454085873725443,
            1692454085873725444,
            1692454085873725442,
            1692454085873725445,
            1695608263517618177
        )
        val workOrderDetailC = mutableListOf<BaseNode>()
        for (i in workOrderDetailTitles.indices) {
            workOrderDetailC.add(RoleChildChildEntity(workOrderDetailTitles[i], workOrderDetailIds[i]))
        }
        workOrderDetailP.childNode = workOrderDetailC
        list.add(workOrderDetailP)

        //工单
        val workOrderP = RoleParentEntity("工单管理")
        workOrderP.isExpanded = false
        val workOrderTitles = arrayOf(
            "工单新增",
            "工单审核",
            "工单修改",
            "工单查询",
            "工单删除"
        )
        val workOrderIds = arrayOf(
            1692454085341048835,
            1695608092855582721,
            1692454085341048836,
            1692454085341048834,
            1692454085341048837
        )
        val workOrderC = mutableListOf<BaseNode>()
        for (i in workOrderTitles.indices) {
            workOrderC.add(RoleChildChildEntity(workOrderTitles[i], workOrderIds[i]))
        }
        workOrderP.childNode = workOrderC
        list.add(workOrderP)

        //出差报告
        val tripReportP = RoleParentEntity("出差报告")
        tripReportP.isExpanded = false
        val tripReportTitles = arrayOf(
            "出差报告新增",
            "出差报告修改",
            "出差报告查询",
            "出差报告删除"
        )
        val tripReportIds = arrayOf(
            1692454084808372227,
            1692454084808372228,
            1692454084808372226,
            1692454084808372229
        )
        val tripReportC = mutableListOf<BaseNode>()
        for (i in tripReportTitles.indices) {
            tripReportC.add(RoleChildChildEntity(tripReportTitles[i], tripReportIds[i]))
        }
        tripReportP.childNode = tripReportC
        list.add(tripReportP)

        //日志管理
        val logP = RoleParentEntity("日志管理")
        logP.isExpanded = false
        val logTitles = arrayOf(
            "派工单变更日志新增",
            "派工单变更日志修改",
            "派工单变更日志查询",
            "派工单变更日志删除"
        )
        val logIds = arrayOf<Long>(
            1692454086473510915,
            1692454086473510916,
            1692454086473510914,
            1692454086473510917
        )
        val logC = mutableListOf<BaseNode>()
        for (i in logTitles.indices) {
            logC.add(RoleChildChildEntity(logTitles[i], logIds[i]))
        }
        logP.childNode = logC
        list.add(logP)

        //系统管理
        val systemP = RoleParentEntity("系统管理")
        systemP.isExpanded = false
        val systemTitles = arrayOf(
            "登录日志查询",
            "用户查询",
            "用户新增",
            "用户修改",
            "用户删除",
            "重置密码"
        )
        val systemIds = arrayOf<Long>(
            1043,
            1001,
            1002,
            1003,
            1004,
            1007
        )
        val systemC = mutableListOf<BaseNode>()
        for (i in systemTitles.indices) {
            systemC.add(RoleChildChildEntity(systemTitles[i], systemIds[i]))
        }
        systemP.childNode = systemC
        list.add(systemP)

        return list
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, RoleManagerActivity::class.java))
        }
    }
}