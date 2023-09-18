package com.jssg.servicemanagersystem.ui.account.logmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.ActivityLogManagerBinding
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo
import com.jssg.servicemanagersystem.ui.account.logmanager.adapter.LogManagerAdapter
import com.jssg.servicemanagersystem.ui.account.logmanager.viewmodel.LogManagerViewModel
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class LogManagerActivity : BaseActivity() {
    private var page: Int = 1
    private lateinit var logManagerViewModel: LogManagerViewModel
    private lateinit var adapter: LogManagerAdapter
    private lateinit var binding: ActivityLogManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LogManagerAdapter()
        binding.recyclerView.adapter = adapter

        binding.smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                loadData(true)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData(false)
            }

        })

        addListener()

        initViewModel()
    }

    private fun loadData(isRefresh: Boolean) {
        page = if (isRefresh) 1 else page + 1

        logManagerViewModel.getLogInfo(isRefresh, page)
    }

    private fun addListener() {
        binding.toolBar.setNavigationOnClickListener { finish() }
    }

    private fun initViewModel() {
        logManagerViewModel = ViewModelProvider(this)[LogManagerViewModel::class.java]

        logManagerViewModel.logInfoListLiveData.observe(this) { result ->
            if (!result.isLoading) {
                hideLoading()
                if (result.isPullRefresh) {
                    binding.smartRefreshLayout.finishRefresh()
                } else {
                    binding.smartRefreshLayout.finishLoadMore()
                }
            }
            if (result.isSuccess) {
                updateLogInfoList(result)
            } else if (result.isError) {
                ToastUtils.showToast(result.msg)
            }
        }

        showProgressbarLoading()
        loadData(true)
    }

    private fun updateLogInfoList(result: LoadListDataModel<List<LogInfo>?>) {
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

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, LogManagerActivity::class.java)).apply {

            }
        }
    }
}