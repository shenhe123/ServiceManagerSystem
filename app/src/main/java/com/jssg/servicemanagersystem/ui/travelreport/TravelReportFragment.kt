package com.jssg.servicemanagersystem.ui.travelreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.databinding.FragmentTravelReportBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.travelreport.adapter.TravelReportAdapter
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderDetailActivity
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class TravelReportFragment : BaseFragment() {

    private val page: Int = 1
    private lateinit var sourceList: MutableList<TravelReportInfo>
    private lateinit var adapter: TravelReportAdapter
    private lateinit var travelReportViewModel: TravelReportViewModel
    private lateinit var binding: FragmentTravelReportBinding

    private val addNewLauncher = registerForActivityResult(AddTravelReportActivity.AddTravelReportContracts()){
        it?.let {
            loadData(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTravelReportBinding.inflate(inflater, container, false)
        travelReportViewModel = ViewModelProvider(this).get(TravelReportViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TravelReportAdapter()
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
        travelReportViewModel.travelReportListLiveData.observe(viewLifecycleOwner) { result ->
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
        loadData(true)
    }

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }

    private fun updateWorkOrderList(result: LoadListDataModel<List<TravelReportInfo>?>) {
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
        travelReportViewModel.getTravelReportList(isRefresh, page)
    }

    private fun addListener() {

        adapter.setOnItemClickListener { _, _, position ->
            val travelReportInfo = adapter.data[position]
            TravelReportDetailActivity.goActivity(requireContext(), travelReportInfo)
        }

        binding.fbtnAddNew.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_TRIPREPORT_ADD.name)) return@setOnClickListener

            addNewLauncher.launch("")
        }


        binding.mbtSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_TRIPREPORT_QUERY.name)) return@setOnClickListener

            val input = binding.inputSearch.text.toString()
            if (input.isEmpty()) {
                return@setOnClickListener
            }

            travelReportViewModel.searchTravelReport(input)
        }
    }

    companion object {
        fun newInstance() =
            TravelReportFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable("inputData", inputData)
                }
            }
    }
}