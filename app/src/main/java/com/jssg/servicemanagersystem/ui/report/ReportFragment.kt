package com.jssg.servicemanagersystem.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.checkbox.MaterialCheckBox
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.FragmentReportBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.report.adapter.WorkOrderReportAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderFragment
import com.jssg.servicemanagersystem.ui.workorder.popup.WorkOrderSearchPopupWindow
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class ReportFragment : BaseFragment() {

    companion object {
        fun newInstance() = ReportFragment().apply {
            Bundle().apply {

            }
        }
    }

    private var searchParams: WorkOrderFragment.SearchParams? = null
    private val checkedBillNos = arrayListOf<String>()
    private var page: Int = 1
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var adapter: WorkOrderReportAdapter
    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WorkOrderReportAdapter(false)
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
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]
        workOrderViewModel.workOrderListLiveData.observe(viewLifecycleOwner) { result ->
            if (!result.isLoading) {
                hideLoading()
                if (result.isPullRefresh) {
                    binding.smartRefreshLayout.finishRefresh()
                } else {
                    binding.smartRefreshLayout.finishLoadMore()
                }

                binding.pbLoading.isVisible = false
            }
            if (result.isSuccess) {
                updateWorkOrderList(result)
            } else if (result.isError) {
                ToastUtils.showToast(result.msg)
            }
        }

        accountViewModel.userProfileLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                judgeRolePermission()
            }
        }

        lifecycleScope.launchWhenResumed {
            if (AccountManager.instance.getUser() == null) {
                accountViewModel.getUserProfile()
            }
            loadData(true)
        }

    }

    override fun onResume() {
        super.onResume()
        if (AccountManager.instance.getUser() != null) {
            judgeRolePermission()
        }
    }

    private fun judgeRolePermission() {
        binding.tvExport.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKODERDETAIL_REPORT.printableName)
    }

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }

    private fun updateWorkOrderList(result: LoadListDataModel<List<WorkOrderInfo>?>) {
        result.rows?.let {
            val reversedList = it
            if (result.isPullRefresh) {
                adapter.setList(reversedList)
            } else {
                if (reversedList.isEmpty()) { //无更多数据
                    binding.smartRefreshLayout.setNoMoreData(true)
                } else {
                    adapter.addData(reversedList)
                }
            }
        }

        updateCheckedIsAllBillNo()

        showNoData(adapter.data.isEmpty())
    }

    private fun loadData(isRefresh: Boolean) {
        page = if (isRefresh) {
            searchParams = null
            binding.smartRefreshLayout.setEnableLoadMore(true)
            1
        } else {
            page + 1
        }
        workOrderViewModel.getWorkOrderList(isRefresh, page)
    }

    private fun addListener() {

        adapter.setOnItemChildClickListener { _, view, position ->
            val workOrderInfo = adapter.data[position]
            when(view.id) {
                R.id.mcb_check -> {
                    if ((view as MaterialCheckBox).isChecked) {
                        checkedBillNos.add(workOrderInfo.billNo)
                    } else {
                        if (checkedBillNos.contains(workOrderInfo.billNo)) {
                            checkedBillNos.remove(workOrderInfo.billNo)
                        }
                    }
                }
            }
        }

//        adapter.setOnItemClickListener { _, _, position ->
//            val workOrderInfo = adapter.data[position]
//            workOrderLauncher.launch(workOrderInfo)
//        }

        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_QUERY.printableName, true)) return@setOnClickListener

            showTipPopupWindow(binding.layoutSearch)
        }

        binding.cbCheckAll.setOnClickListener {
            adapter.toggleCheckedState()

            updateCheckedIsAllBillNo()
        }

        binding.tvExport.setOnClickListener {

            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKODERDETAIL_REPORT.printableName, true)) return@setOnClickListener

            if (checkedBillNos.isNotEmpty()) {
                SingleBtnDialogFragment.newInstance("确定导出", "确定将所选工单的报表全部导出吗？")
                    .addConfrimClickLisntener(object :
                        SingleBtnDialogFragment.OnConfirmClickLisenter {
                        override fun onConfrimClick() {

                        }

                    }).show(childFragmentManager, "close_case_dialog")
            } else {
                ToastUtils.showToast("请先选择要导出的工单报表")
            }

        }
    }

    private fun updateCheckedIsAllBillNo() {
        if (binding.cbCheckAll.isChecked) {
            checkedBillNos.clear()
            checkedBillNos.addAll(adapter.data.map { it.billNo })
        } else {
            checkedBillNos.clear()
        }
    }

    private fun showTipPopupWindow(target: View) {
        val popupWindow = WorkOrderSearchPopupWindow(requireContext(), binding.root, searchParams)
        popupWindow.setOnClickListener(object : WorkOrderSearchPopupWindow.OnSearchBtnClick {
            override fun onClick(searchParams: WorkOrderFragment.SearchParams) {
                showProgressbarLoading()
                this@ReportFragment.searchParams = searchParams
                binding.smartRefreshLayout.setEnableLoadMore(false)
                workOrderViewModel.searchWorkOrder(searchParams)
            }

        })
        popupWindow.showAsDropDown(target, 0, 0)
    }
}