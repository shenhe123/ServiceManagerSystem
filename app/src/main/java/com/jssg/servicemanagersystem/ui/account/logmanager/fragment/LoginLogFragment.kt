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
import com.jssg.servicemanagersystem.databinding.FragmentLoginLogBinding
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.logmanager.LoginInfoSearchDialogFragment
import com.jssg.servicemanagersystem.ui.account.logmanager.adapter.LoginLogManagerAdapter
import com.jssg.servicemanagersystem.ui.account.logmanager.viewmodel.LogManagerViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.parcel.Parcelize

/**
 * A simple [Fragment] subclass.
 * Use the [LoginLogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginLogFragment : BaseFragment() {

    private lateinit var logManagerViewModel: LogManagerViewModel
    private var searchParams: SearchParams? = null
    private var page: Int = 1
    private lateinit var adapter: LoginLogManagerAdapter
    private lateinit var binding: FragmentLoginLogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = LoginLogManagerAdapter()
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

        logManagerViewModel.logInfoListLiveData.observe(viewLifecycleOwner) { result ->
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


    private fun addListener() {
        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(
                    MenuEnum.MONITOR_LOGININFOR_QUERY.printableName,
                    true
                )
            ) return@setOnClickListener

            showSearchDialogWindow()
        }
    }

    private fun loadData(isRefresh: Boolean) {
        page = if (isRefresh) {
            1
        } else page + 1

        logManagerViewModel.searchLoginLogInfo(searchParams, isRefresh, page)
    }

    private fun showSearchDialogWindow() {
        LoginInfoSearchDialogFragment.newInstance(searchParams)
            .setOnClickListener(object : LoginInfoSearchDialogFragment.OnSearchBtnClick {
                override fun onClick(searchParams: SearchParams) {
                    showProgressbarLoading()
                    this@LoginLogFragment.searchParams = searchParams
                    page = 1
                    logManagerViewModel.searchLoginLogInfo(searchParams, true, page)
                }

            }).show(childFragmentManager, "login_info_search")
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            LoginLogFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    @Parcelize
    data class SearchParams(
        val userName: String?,
        val startDate: String?,
        val endDate: String?,
    ) : Parcelable
}