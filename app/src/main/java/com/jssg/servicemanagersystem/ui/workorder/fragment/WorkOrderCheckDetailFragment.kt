package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.FragmentWorkOrderCheckDetailBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderCheckDetailActivity
import com.jssg.servicemanagersystem.ui.workorder.adapter.WorkOrderCheckAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class WorkOrderCheckDetailFragment : Fragment() {

    private lateinit var adapter: WorkOrderCheckAdapter
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var binding: FragmentWorkOrderCheckDetailBinding
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
        binding = FragmentWorkOrderCheckDetailBinding.inflate(inflater, container, false)
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
                loadData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {

            }

        })

        adapter.setOnItemClickListener { _, view, position ->
            val workOrderInfo = adapter.data[position]
            workOrderCheckLauncher.launch(workOrderInfo)
        }


        binding.mbtSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_QUERY.printableName)) return@setOnClickListener

            val input = binding.inputSearch.text.toString()
            if (input.isEmpty()) {
                return@setOnClickListener
            }

            workOrderViewModel.searchWorkOrderDetail(input)
        }
    }

    private fun initViewModel() {
        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]
        workOrderViewModel.workOrderCheckListLiveData.observe(viewLifecycleOwner) { result ->
            if (!result.isLoading) {
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

        loadData()
    }

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }

    private fun updateWorkOrderList(result: LoadListDataModel<List<WorkOrderCheckInfo>?>) {
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

    private fun loadData() {
        workOrderViewModel.getWorkOrderCheckList()
    }

    companion object {
        @JvmStatic
        fun newInstance(inputData: WorkOrderInfo?) =
            WorkOrderCheckDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("inputData", inputData)
                }
            }
    }
}