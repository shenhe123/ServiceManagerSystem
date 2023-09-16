package com.jssg.servicemanagersystem.ui.account.usermanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.ActivityUserManagerBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class UserManagerActivity : BaseActivity() {
    private var clickItemPos: Int? = null
    private var page: Int = 1
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var adapter: UserManagerAdapter
    private lateinit var binding: ActivityUserManagerBinding

    private val launcherAddUser = registerForActivityResult(AddUserActivity.AddUserContracts()) { newUser ->
        newUser?.let {
            loadData(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        showBack()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserManagerAdapter()
        binding.recyclerView.adapter = adapter

        adapter.setOnItemChildClickListener { _, v, position ->
            clickItemPos = position
            val user = adapter.data[position]
            when(v.id) {
                R.id.card_layout -> UserManagerDetailActivity.goActivity(this, user)

                R.id.mbt_permission -> {
                    if (!RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_USER_EDIT.printableName, true)) return@setOnItemChildClickListener

                    PermissionDialogFragment.newInstance(user)
                        .addOnFinishListener(object :PermissionDialogFragment.OnFinishListener{
                            override fun onFinish(newUser: User) {
                                adapter.data[position] = newUser
                                adapter.notifyItemChanged(position)
                            }

                        })
                        .show(supportFragmentManager, "permission_dialog")
                }

                R.id.mbt_delete -> {
                    if (!RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_USER_REMOVE.printableName, true)) return@setOnItemChildClickListener

                    SingleBtnDialogFragment.newInstance("删除", "确定要删除此用户吗？")
                        .addConfrimClickLisntener(object :
                            SingleBtnDialogFragment.OnConfirmClickLisenter {
                            override fun onConfrimClick() {
                                accountViewModel.deleteUserInfo(user.userId)
                            }

                        })
                        .show(supportFragmentManager, "delete_user_dialog")
                }
            }


        }

        binding.fbtnAddNew.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_USER_ADD.printableName, true)) return@setOnClickListener
            launcherAddUser.launch("")
        }

        binding.mbtSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_USER_QUERY.printableName)) return@setOnClickListener
            val input = binding.inputSearch.text.toString()
            if (input.isEmpty()) {

                return@setOnClickListener
            }
            accountViewModel.searchUser(input)
        }

        binding.smartRefreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener{
            override fun onRefresh(refreshLayout: RefreshLayout) {
                loadData(true)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData(false)
            }

        })

        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        accountViewModel.userListLiveData.observe(this) { result ->
            if (!result.isLoading) {
                hideLoading()
                if (result.isPullRefresh) {
                    binding.smartRefreshLayout.finishRefresh()
                } else {
                    binding.smartRefreshLayout.finishLoadMore()
                }
            }

            if (result.isSuccess) {
                updateUserList(result)
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
                    adapter.removeAt(it)
                }
            }
        }

        showProgressbarLoading()
        loadData(true)
    }

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }

    private fun updateUserList(result: LoadListDataModel<List<User>?>) {
        result.rows?.let {
            if (result.isPullRefresh) {
                adapter.setList(it)
            } else {
                if (it.isEmpty()) { //无更多数据
                    binding.smartRefreshLayout.setNoMoreData(true)
                } else {
                    adapter.addData(it)
                }
            }
        }

        showNoData(adapter.data.isEmpty())
    }

    private fun loadData(isRefresh: Boolean) {
        page = if (isRefresh) {
            1
        } else {
            page + 1
        }
        accountViewModel.getUserList(page, isRefresh)
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, UserManagerActivity::class.java))
        }
    }
}