package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.android.material.checkbox.MaterialCheckBox
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.FragmentWorkOrderBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderAddNewActivity
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderDetailActivity
import com.jssg.servicemanagersystem.ui.workorder.adapter.WorkOrderAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class WorkOrderFragment : BaseFragment() {

    private var sourceList = mutableListOf<WorkOrderInfo>()
    private val checkedBillNos = arrayListOf<String>()
    private val page: Int = 1
    private lateinit var adapter: WorkOrderAdapter
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var binding: FragmentWorkOrderBinding

    private val addNewLauncher = registerForActivityResult(WorkOrderAddNewActivity.AddNewWorkOrderContracts()){
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

        binding = FragmentWorkOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WorkOrderAdapter(false)
        binding.recyclerView.adapter = adapter

        binding.smartRefreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener{
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
                adapter.isCloseCase = false
//                    adapter.setList(sourceList)
                loadData(true)
            }
        }

        showProgressbarLoading()
        loadData(true)
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

        sourceList = adapter.data

        showNoData(adapter.data.isEmpty())
    }

    private fun loadData(isRefresh: Boolean) {
        workOrderViewModel.getWorkOrderList(isRefresh, page)
    }

    private fun addListener() {

        adapter.setOnItemChildClickListener { _, view, position ->
            val workOrderInfo = adapter.data[position]
            if (view.id == R.id.mcb_check) {
                if ((view as MaterialCheckBox).isChecked) {
                    checkedBillNos.add(workOrderInfo.billNo)
                } else {
                    if (checkedBillNos.contains(workOrderInfo.billNo)) {
                        checkedBillNos.remove(workOrderInfo.billNo)
                    }
                }
            }
        }

        adapter.setOnItemClickListener { _, _, position ->
            val workOrderInfo = adapter.data[position]
            WorkOrderDetailActivity.goActivity(requireActivity(), workOrderInfo)
        }

        binding.fbtnAddNew.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_ADD.printableName)) return@setOnClickListener
            addNewLauncher.launch("")
        }


        binding.mbtSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_QUERY.printableName)) return@setOnClickListener

            val input = binding.inputSearch.text.toString()
            if (input.isEmpty()) {
                return@setOnClickListener
            }

            workOrderViewModel.searchWorkOrder(input)
        }

        binding.tvCloseCase.setOnClickListener {

            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_FINISH.printableName)) return@setOnClickListener

            if (!adapter.isCloseCase) {
                binding.tvCloseCase.text = "提交"
                adapter.isCloseCase = true
//                val checkedOrders = sourceList.filter { it.checkState == 2 }
//                adapter.setList(checkedOrders)
                adapter.notifyDataSetChanged()
            } else {
                if (checkedBillNos.isNotEmpty()) {
                    workOrderViewModel.closeCaseWorkOrder(checkedBillNos)
                } else {
                    binding.tvCloseCase.text = "结案"
                    adapter.isCloseCase = false
//                    adapter.setList(sourceList)
                    adapter.notifyDataSetChanged()
                }

            }

        }
    }

    companion object {
        fun newInstance(): WorkOrderFragment {
            val args = Bundle()

            val fragment = WorkOrderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}