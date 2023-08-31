package com.jssg.servicemanagersystem.ui.account.usermanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.ActivityUserManagerBinding
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class UserManagerActivity : BaseActivity() {
    private var page: Int = 1
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var adapter: UserManagerAdapter
    private lateinit var binding: ActivityUserManagerBinding

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
            when(v.id) {
                R.id.card_layout -> UserManagerDetailActivity.goActivity(this)

                R.id.mbt_permission -> PermissionDialogFragment.newInstance()
                    .show(supportFragmentManager, "permission_dialog")

                R.id.mbt_delete -> SingleBtnDialogFragment.newInstance("删除", "确定要删除此用户吗？").show(supportFragmentManager, "delete_user_dialog")
            }


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
            }
        }

        showProgressbarLoading()
        loadData(true)
    }

    private fun updateUserList(result: LoadListDataModel<List<User>?>) {
        result.rows?.let {
            if (result.isPullRefresh) {
                adapter.setList(it)
            } else {
                adapter.addData(it)
            }
        }
    }

    private fun loadData(isRefresh: Boolean) {
        accountViewModel.getUserList(page, isRefresh)
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, UserManagerActivity::class.java))
        }
    }
}