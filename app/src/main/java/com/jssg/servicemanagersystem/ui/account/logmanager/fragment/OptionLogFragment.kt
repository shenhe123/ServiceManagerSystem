package com.jssg.servicemanagersystem.ui.account.logmanager.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.FragmentOptionLogBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.logmanager.OptionsLogSearchDialogFragment
import com.jssg.servicemanagersystem.ui.account.logmanager.adapter.OptionLogManagerAdapter
import com.jssg.servicemanagersystem.ui.account.logmanager.entity.OptionLogChildEntity
import com.jssg.servicemanagersystem.ui.account.logmanager.entity.OptionLogParentEntity
import com.jssg.servicemanagersystem.ui.account.logmanager.viewmodel.LogManagerViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.parcel.Parcelize

/**
 * A simple [Fragment] subclass.
 * Use the [OptionLogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OptionLogFragment : BaseFragment() {

    private lateinit var logManagerViewModel: LogManagerViewModel
    private var searchParams: SearchParams? = null
    private var page: Int = 1
    private lateinit var adapter: OptionLogManagerAdapter
    private lateinit var binding: FragmentOptionLogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptionLogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = OptionLogManagerAdapter()
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

    private fun initViewModel() {
        logManagerViewModel = ViewModelProvider(this)[LogManagerViewModel::class.java]

        logManagerViewModel.optionInfoListLiveData.observe(viewLifecycleOwner) { result ->
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

    private fun updateLogInfoList(result: LoadListDataModel<List<OptionLogParentEntity>?>) {
        result.rows?.let {
            if (result.isPullRefresh) {
                it.forEach { parent ->
                    parent.childNode = mutableListOf(OptionLogChildEntity(parent.operParam))
                }
                adapter.setList(it)
            } else {
                if (it.isEmpty()) { //无更多数据
                    binding.smartRefreshLayout.setNoMoreData(true)
                } else {
                    it.forEach { parent ->
                        parent.childNode = mutableListOf(OptionLogChildEntity(parent.operParam))
                    }
                    adapter.addData(it)
                }
            }
        }

        showNoData(adapter.data.isEmpty())
    }

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }


    private fun addListener() {
        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.MONITOR_LOGININFOR_QUERY.printableName, true)) return@setOnClickListener

            showSearchDialogWindow()
        }
    }

    private fun loadData(isRefresh: Boolean) {
        page = if (isRefresh) {
            1
        } else page + 1

        logManagerViewModel.searchOptionsLogInfo(searchParams, isRefresh, page)
    }

    private fun showSearchDialogWindow() {
        OptionsLogSearchDialogFragment.newInstance(searchParams).setOnClickListener(object : OptionsLogSearchDialogFragment.OnSearchBtnClick {
            override fun onClick(searchParams: SearchParams) {
                showProgressbarLoading()
                this@OptionLogFragment.searchParams = searchParams
                page = 1
                logManagerViewModel.searchOptionsLogInfo(searchParams, true, page)
            }
        }).show(childFragmentManager, "options_log_search")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OptionLogFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    @Parcelize
    data class SearchParams(
        val title: String?, //模块名称
        val businessType: String?, //类型
        val operName: String?, //操作人
        val operParam: String?, //搜索内容
        val status: String?, //状态
        val beginTime: String?, //开始时间
        val endTime: String?, //结束时间
    ) : Parcelable
}