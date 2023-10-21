package com.jssg.servicemanagersystem.ui.workorder

import android.os.Bundle
import android.os.Parcelable
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
import com.jssg.servicemanagersystem.databinding.FragmentWorkOrderBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.DoubleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.adapter.WorkOrderAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.popup.WorkOrderSearchDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.parcel.Parcelize

class WorkOrderFragment : BaseFragment() {

    private var deletePos: Int? = null
    private var searchParams: SearchParams? = null
    private val checkedBillNos = arrayListOf<String>()
    private var page: Int = 1
    private lateinit var adapter: WorkOrderAdapter
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var binding: FragmentWorkOrderBinding

    private val addNewLauncher =
        registerForActivityResult(AddWorkOrderActivity.AddNewWorkOrderContracts()) {
            it?.let {
                loadData(true)
            }
        }

    private val workOrderLauncher =
        registerForActivityResult(WorkOrderDetailActivity.WorkOrderContracts()) {
            it?.let {
                loadData(true)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        binding = FragmentWorkOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WorkOrderAdapter(false)
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

        workOrderViewModel.closeCaseWorkOrderLiveData.observe(viewLifecycleOwner) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("结案成功")
                binding.tvCloseCase.text = "结案"
                checkedBillNos.clear()
                adapter.isCloseCase = false
                loadData(true)
            }
        }

        workOrderViewModel.deleteWorkOrderLiveData.observe(viewLifecycleOwner) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("删除成功")
                deletePos?.let {
                    adapter.removeAt(it)
                }
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
        binding.tvCloseCase.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_FINISH.printableName)
        binding.fbtnAddNew.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_ADD.printableName)
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

        showNoData(adapter.data.isEmpty())
    }

    private fun loadData(isRefresh: Boolean) {
        page = if (isRefresh) {
            searchParams = null
            binding.smartRefreshLayout.setEnableLoadMore(true)
            resetCloseCaseStatus()
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
//                    if (workOrderInfo.waitCheckCount.bigDecimal() <= BigDecimal.ZERO) {
//                        DoubleBtnDialogFragment.newInstance("确定勾选", "此工单还未经排查，确定要勾选吗？")
//                            .addConfirmClickLisntener(object :
//                                DoubleBtnDialogFragment.OnConfirmClickLisenter {
//                                override fun onConfrimClick() {
//                                    checkedBillNos.add(workOrderInfo.billNo)
//                                }
//
//                            }).addCancelClickLisntener(object :
//                                DoubleBtnDialogFragment.OnCancelClickLisenter {
//                                override fun onCancelClick() {
//                                    view.toggle()
//                                }
//                            }).show(childFragmentManager, "close_case_dialog")
//                    } else {
                        checkedBillNos.add(workOrderInfo.billNo)
//                    }
                    } else {
                        if (checkedBillNos.contains(workOrderInfo.billNo)) {
                            checkedBillNos.remove(workOrderInfo.billNo)
                        }
                    }
                }

                R.id.mbt_delete -> {
                    if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_REMOVE.printableName, true)) return@setOnItemChildClickListener

                    deletePos = position
                    SingleBtnDialogFragment.newInstance("删除", "确定要删除此工单吗？")
                        .addConfrimClickLisntener(object :
                            SingleBtnDialogFragment.OnConfirmClickLisenter {
                            override fun onConfrimClick() {
                                workOrderViewModel.deleteWorkOrderInfo(workOrderInfo.billNo)
                            }

                        })
                        .show(childFragmentManager, "delete_work_order_dialog")
                }
            }
        }

        adapter.setOnItemClickListener { _, _, position ->
            val workOrderInfo = adapter.data[position]
            workOrderLauncher.launch(workOrderInfo)
        }

        binding.fbtnAddNew.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_ADD.printableName, true)) return@setOnClickListener
            addNewLauncher.launch("")
        }


        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_QUERY.printableName, true)) return@setOnClickListener

            WorkOrderSearchDialogFragment
                .newInstance(searchParams)
                .setOnClickListener(object : WorkOrderSearchDialogFragment.OnSearchBtnClick {
                    override fun onClick(searchParams: SearchParams) {
                        showProgressbarLoading()
                        this@WorkOrderFragment.searchParams = searchParams
                        binding.smartRefreshLayout.setEnableLoadMore(false)
                        workOrderViewModel.searchWorkOrder(searchParams)
                    }

                })
                .show(childFragmentManager, "work_order_search_dialog")
        }

        binding.tvCloseCase.setOnClickListener {

            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_FINISH.printableName, true)) return@setOnClickListener

            if (!adapter.isCloseCase) {
                binding.tvCloseCase.text = "提交"
                adapter.isCloseCase = true
                adapter.notifyDataSetChanged()
            } else {
                val tipsContent = if (checkedBillNos.isNotEmpty()) {
                    "确定将所选工单全部结案吗？"
                } else {
                    "还没有选择工单哦，继续结案吗"
                }

                DoubleBtnDialogFragment.newInstance(
                    "提示",
                    tipsContent,
                    "关闭结案",
                    "继续结案"
                ).addCancelClickLisntener(object :
                    DoubleBtnDialogFragment.OnCancelClickLisenter {
                    override fun onCancelClick() {
                        resetCloseCaseStatus()
                    }
                }).addConfirmClickLisntener(object : DoubleBtnDialogFragment.OnConfirmClickLisenter {
                    override fun onConfrimClick() {
                        if (checkedBillNos.isNotEmpty()) {
                            workOrderViewModel.closeCaseWorkOrder(checkedBillNos)
                        }
                    }

                })
                    .show(childFragmentManager, "close_case_dialog")

            }

        }
    }

    private fun resetCloseCaseStatus() {
        binding.tvCloseCase.text = "结案"
        adapter.isCloseCase = false
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): WorkOrderFragment {
            val args = Bundle()

            val fragment = WorkOrderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Parcelize
    data class SearchParams(
        val applyName: String?,
        val productDesc: String?,
        val productCode: String?,
        val startDate: String?,
        val endDate: String?,
        val oaBillNo: String?,
        val factory: String,
        val state: String,
        val batchNo: String? = null,
    ) : Parcelable
}