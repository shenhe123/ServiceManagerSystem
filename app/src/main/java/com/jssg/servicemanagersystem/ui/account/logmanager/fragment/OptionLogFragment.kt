package com.jssg.servicemanagersystem.ui.account.logmanager.fragment

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.FragmentOptionLogBinding
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.entity.OptionLogInfo
import com.jssg.servicemanagersystem.ui.account.logmanager.LoginInfoSearchPopupWindow
import com.jssg.servicemanagersystem.ui.account.logmanager.OptionLogSearchPopupWindow
import com.jssg.servicemanagersystem.ui.account.logmanager.adapter.LoginLogManagerAdapter
import com.jssg.servicemanagersystem.ui.account.logmanager.adapter.OptionLogManagerAdapter
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

    private fun updateLogInfoList(result: LoadListDataModel<List<OptionLogInfo>?>) {
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


    private fun addListener() {
        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.MONITOR_LOGININFOR_QUERY.printableName, true)) return@setOnClickListener

            showTipPopupWindow(binding.layoutSearch)
        }
    }

    private fun loadData(isRefresh: Boolean) {
        page = if (isRefresh) {
            searchParams = null
            1
        } else page + 1

        logManagerViewModel.getOptionsLogInfo(isRefresh, page)
    }

    private fun showTipPopupWindow(target: View) {
        val popupWindow = OptionLogSearchPopupWindow(requireContext(), binding.root, searchParams)
        popupWindow.setOnClickListener(object : OptionLogSearchPopupWindow.OnSearchBtnClick {
            override fun onClick(searchParams: SearchParams) {
                showProgressbarLoading()
                this@OptionLogFragment.searchParams = searchParams
                page = 1
                logManagerViewModel.searchOptionsLogInfo(searchParams, true, page)
            }

        })
        popupWindow.showAsDropDown(target, 0, 0)
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
        val status: String?, //状态
        val beginTime: String?, //开始时间
        val endTime: String?, //结束时间
    ) : Parcelable
}