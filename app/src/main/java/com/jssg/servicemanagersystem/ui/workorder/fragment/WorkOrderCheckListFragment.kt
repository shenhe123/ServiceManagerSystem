package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.FragmentWorkOrderCheckListBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderCheckDetailActivity
import com.jssg.servicemanagersystem.ui.workorder.adapter.WorkOrderCheckAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.popup.WorkOrderDetailSearchDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.parcel.Parcelize

class WorkOrderCheckListFragment : BaseFragment() {

    private var deletePos: Int? = null
    private var searchParams: SearchParams? = null
    private lateinit var adapter: WorkOrderCheckAdapter
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var binding: FragmentWorkOrderCheckListBinding
    private var inputData: WorkOrderInfo? = null

    private val workOrderCheckLauncher = registerForActivityResult(WorkOrderCheckDetailActivity.WorkOrderCheckContracts()) {
        it?.let {
            loadData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            inputData = it.getParcelable("inputData")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkOrderCheckListBinding.inflate(inflater, container, false)
        initView()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
    }

    private fun initView() {

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WorkOrderCheckAdapter()
        binding.recyclerView.adapter = adapter

        binding.smartRefreshLayout.setEnableLoadMore(false)
        binding.smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                searchParams = null
                loadData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {

            }

        })

        adapter.setOnItemClickListener { _, view, position ->
            val workOrderInfo = adapter.data[position]
            workOrderCheckLauncher.launch(WorkOrderCheckDetailActivity.InputData(workOrderInfo))
        }

        adapter.setOnItemChildClickListener { _, view, position ->
            val workOrderInfo = adapter.data[position]
            when(view.id) {
                R.id.mbt_review -> {  //审核
                    workOrderCheckLauncher.launch(WorkOrderCheckDetailActivity.InputData(workOrderInfo, true))
                }

                R.id.mbt_delete -> { //删除
                    if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_REMOVE.printableName, true)) return@setOnItemChildClickListener

                    deletePos = position
                    com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment.newInstance("删除", "确定要删除此排查工单吗？")
                        .addConfrimClickLisntener(object :
                            com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment.OnConfirmClickLisenter {
                            override fun onConfrimClick() {
                                workOrderViewModel.deleteWorkOrderCheckDetailInfo(workOrderInfo.billDetailNo)
                            }

                        })
                        .show(childFragmentManager, "delete_work_order_dialog")

                }
            }

        }

        binding.inputSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_QUERY.printableName)) return@setOnClickListener

            WorkOrderDetailSearchDialogFragment.newInstance(searchParams)
                .setOnClickListener(object : WorkOrderDetailSearchDialogFragment.OnSearchBtnClick{
                    override fun onClick(searchParams: SearchParams) {
                        showProgressbarLoading()
                        this@WorkOrderCheckListFragment.searchParams = searchParams
                        inputData?.let {
                            workOrderViewModel.searchWorkOrderDetail(searchParams, it.billNo)
                        }
                    }

                })
                .show(childFragmentManager, "search_work_order_detail_dialog")
        }
    }

    private fun initViewModel() {
        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]
        workOrderViewModel.workOrderCheckListLiveData.observe(viewLifecycleOwner) { result ->
            if (!result.isLoading) {
                hideLoading()
                if (result.isPullRefresh) {
                    binding.smartRefreshLayout.finishRefresh()
                } else {
                    binding.smartRefreshLayout.finishLoadMore()
                }
            }
            if (result.isSuccess) {
                updateWorkOrderList(result)
            } else if (result.isError) {
                ToastUtils.showToast(result.msg)
            }
        }

        workOrderViewModel.deleteWorkOrderCheckDetailLiveData.observe(viewLifecycleOwner) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("删除成功")
                deletePos?.let {
                    adapter.removeAt(it)
                }
            }
        }

        loadData()
    }

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }

    private fun updateWorkOrderList(result: LoadListDataModel<List<WorkOrderCheckInfo>?>) {
        result.rows?.let {
            val reversedList = it.reversed()
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

    private fun loadData() {
        inputData?.let {
            workOrderViewModel.getWorkOrderCheckList(it.billNo)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(inputData: WorkOrderInfo?) =
            WorkOrderCheckListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("inputData", inputData)
                }
            }
    }

    @Parcelize
    data class SearchParams(
        val batchNo: String?,
        val state: String?,
        val startDate: String?,
        val endDate: String?,
    ) : Parcelable
}