package com.jssg.servicemanagersystem.ui.account.role

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.ActivityRoleManagerBinding
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.role.adapter.AddNewRoleAdapter
import com.jssg.servicemanagersystem.ui.account.role.adapter.RoleManagerAdapter
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleChildChildEntity
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleParentEntity
import com.jssg.servicemanagersystem.ui.account.usermanager.PermissionDialogFragment
import com.jssg.servicemanagersystem.ui.account.usermanager.UserManagerAdapter
import com.jssg.servicemanagersystem.ui.account.usermanager.UserManagerDetailActivity
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.jssg.servicemanagersystem.widgets.decoration.ThemeLineItemDecoration
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class RoleManagerActivity : BaseActivity() {
    private var clickItemPos: Int? = null
    private lateinit var adapter: RoleManagerAdapter
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var binding: ActivityRoleManagerBinding

    private val addRoleLauncher = registerForActivityResult(AddRoleActivity.AddRoleContracts()) { refresh ->
        if (refresh == true) {
            //刷新列表
            loadData(true)
        }
    }

    private val updateRoleLauncher = registerForActivityResult(UpdateRoleActivity.UpdateRoleContracts()) { output ->
        output?.let {
            adapter.data[it.position] = it.role
            adapter.notifyItemChanged(it.position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoleManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(ThemeLineItemDecoration())
        adapter = RoleManagerAdapter()
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, v, position ->
            clickItemPos = position
            val role = adapter.data[position]
            updateRoleLauncher.launch(
                UpdateRoleActivity.InputData(
                    position,
                    role
                )
            )
        }

        binding.fbtnAddNew.setOnClickListener {
            addRoleLauncher.launch("")
        }

        binding.mbtSearch.setOnClickListener {
            val input = binding.inputSearch.text.toString()
            if (input.isEmpty()) {

                return@setOnClickListener
            }
            accountViewModel.searchRole(input)
        }

        binding.smartRefreshLayout.setEnableLoadMore(false)
        binding.smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                loadData(true)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData(false)
            }

        })

        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        accountViewModel.roleListLiveData.observe(this) { result ->
            if (!result.isLoading) {
                hideLoading()
                if (result.isPullRefresh) {
                    binding.smartRefreshLayout.finishRefresh()
                } else {
                    binding.smartRefreshLayout.finishLoadMore()
                }
            }

            if (result.isSuccess) {
                updateRoleList(result)
            } else if (result.isError) {
                ToastUtils.showToast(result.msg)
                showNoData(true)
            }
        }

        accountViewModel.deleteUserInfoLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("删除成功")
                clickItemPos?.let {
                    adapter.notifyItemRemoved(it)
                }
            }
        }

        showProgressbarLoading()
        loadData(true)
    }

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }

    private fun updateRoleList(result: LoadListDataModel<List<Role>?>) {
        result.rows?.let {
            val reversedList = it.reversed()
            if (result.isPullRefresh) {
                adapter.setList(reversedList)
            } else {
                adapter.addData(reversedList)
            }
        }

        showNoData(adapter.data.isEmpty())
    }

    private fun loadData(isRefresh: Boolean) {
        accountViewModel.getRoleList()
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, RoleManagerActivity::class.java))
        }
    }
}