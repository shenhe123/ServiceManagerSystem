package com.jssg.servicemanagersystem.ui.report

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.download.DownloadManager
import com.jssg.servicemanagersystem.base.download.DownloadState
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.FragmentReportBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.report.entity.ReportListInfo
import com.jssg.servicemanagersystem.ui.report.viewmodel.ReportViewModel
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment
import com.jssg.servicemanagersystem.ui.workorder.popup.WorkOrderSearchPopupWindow
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.LogUtil
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import net.arvin.permissionhelper.PermissionHelper
import java.io.File


class ReportFragment : BaseFragment() {

    companion object {
        fun newInstance() = ReportFragment().apply {
            Bundle().apply {

            }
        }
    }

    private lateinit var reportViewModel: ReportViewModel
    private var searchParams: WorkOrderFragment.SearchParams? = null
    private lateinit var accountViewModel: AccountViewModel
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

        binding.smartRefreshLayout.setEnableLoadMore(false)
        binding.smartRefreshLayout.setOnRefreshListener { loadData(true) }

        addListener()

        initViewModel()
    }

    private fun initViewModel() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        reportViewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        reportViewModel.reportListLiveData.observe(viewLifecycleOwner) { result ->
            if (!result.isLoading) {
                hideLoading()
                if (binding.smartRefreshLayout.isRefreshing) {
                    binding.smartRefreshLayout.finishRefresh()
                }
                binding.pbLoading.isVisible = false
            }
            if (result.isSuccess) {
                updateReportList(result)
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

    private fun updateReportList(result: LoadListDataModel<List<ReportListInfo>?>) {
        result.rows?.let {
            val reversedList = it
//            val tableData = MapTableData.create("报表", reversedList)
            binding.table.setData(reversedList)
        }

        showNoData(result.rows.isNullOrEmpty())
    }

    private fun loadData(isRefresh: Boolean) {
        if (isRefresh) {
            searchParams = null
        }
        reportViewModel.getReportList()
    }

    private fun addListener() {

        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_QUERY.printableName, true)) return@setOnClickListener

            showTipPopupWindow(binding.layoutSearch)
        }

        binding.tvExport.setOnClickListener {

            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKODERDETAIL_REPORT.printableName, true)) return@setOnClickListener

            val fileName = "workOrder_${DateUtil.getFullData(System.currentTimeMillis())}.xls"
            if (searchParams == null) {
                SingleBtnDialogFragment.newInstance("确定导出", "确定将全部工单的报表全部导出吗？导出后的文件会保存在本地Download/workOrder文件夹的下$fileName")
                    .addConfrimClickLisntener(object :
                        SingleBtnDialogFragment.OnConfirmClickLisenter {
                        override fun onConfrimClick() {
                            exportWorkOrder(fileName)
                        }

                    }).show(childFragmentManager, "close_case_dialog")
            } else {
                SingleBtnDialogFragment.newInstance("确定导出", "确定将当前搜索结果的报表全部导出吗？导出后的文件会保存在本地Download/workOrder文件夹的下$fileName")
                    .addConfrimClickLisntener(object :
                        SingleBtnDialogFragment.OnConfirmClickLisenter {
                        override fun onConfrimClick() {
                            exportWorkOrder(fileName)
                        }

                    }).show(childFragmentManager, "close_case_dialog")
            }

        }
    }

    private fun exportWorkOrder(fileName: String) {
                PermissionHelper.Builder().with(this).build().request("需要读写权限", Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) { granted, isAlwaysDenied ->
            if (granted) {
                //申请权限成功
                val directoryPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                directoryPictures?.let {
                    val fileDirectory = File(directoryPictures.absolutePath + File.separator + "workOrder")
                    if (!fileDirectory.exists()) {
                        fileDirectory.mkdirs()
                    }
                    val filePath = fileDirectory.absolutePath + File.separator + fileName
                    LogUtil.e("shenhe", "外置SD卡路径：" + fileDirectory.absolutePath)

                    lifecycleScope.launchWhenResumed {
                        showProgressbarLoading()
                        DownloadManager.downloadWorkOrderDetailReport(searchParams, File(filePath)).collect {
                            when (it) {
                                is DownloadState.InProgress -> {
                                    LogUtil.e("shenhe", "download progress = ${it.progress}")
                                }
                                is DownloadState.Success -> {
                                    hideLoading()
                                    ToastUtils.showToast("导出成功")
                                    LogUtil.e("shenhe", "download success")
                                }
                                is DownloadState.Error -> {
                                    hideLoading()
                                    ToastUtils.showToast(it.throwable.message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showTipPopupWindow(target: View) {
        val popupWindow = WorkOrderSearchPopupWindow(requireContext(), binding.root, searchParams)
        popupWindow.setOnClickListener(object : WorkOrderSearchPopupWindow.OnSearchBtnClick {
            override fun onClick(searchParams: WorkOrderFragment.SearchParams) {
                showProgressbarLoading()
                this@ReportFragment.searchParams = searchParams
                reportViewModel.searchReportList(searchParams)
            }

        })
        popupWindow.showAsDropDown(target, 0, 0)
    }
}