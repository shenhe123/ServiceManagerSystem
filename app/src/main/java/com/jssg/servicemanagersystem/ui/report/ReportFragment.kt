package com.jssg.servicemanagersystem.ui.report

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bin.david.form.core.TableConfig
import com.bin.david.form.data.CellInfo
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat
import com.bin.david.form.data.format.draw.MultiLineDrawFormat
import com.bin.david.form.data.style.FontStyle
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.download.DownloadManager
import com.jssg.servicemanagersystem.base.download.DownloadState
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.FragmentReportBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.report.entity.ReportListInfo
import com.jssg.servicemanagersystem.ui.report.viewmodel.ReportViewModel
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment
import com.jssg.servicemanagersystem.ui.workorder.popup.WorkOrderSearchPopupWindow
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.LogUtil
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import net.arvin.permissionhelper.PermissionHelper
import java.io.File


class ReportFragment : BaseFragment() {

    companion object {
        fun newInstance() = ReportFragment().apply {
            Bundle().apply {

            }
        }
    }

    private var page: Int = 1
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

        binding.smartRefreshLayout.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                loadData(true)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData(false)
            }

        })

        binding.table.config.isShowTableTitle = false
        binding.table.config.columnTitleStyle =
            FontStyle(requireContext(), 16, resources.getColor(R.color.purple_700, null))

//        binding.table.config.columnTitleBackground =
//            BaseBackgroundFormat(ContextCompat.getColor(requireContext(), R.color.grey))

        binding.table.config.ySequenceStyle =
            FontStyle(requireContext(), 16, resources.getColor(R.color.purple_700, null))

        binding.table.config.isFixedYSequence = true

        binding.table.config.contentCellBackgroundFormat = object :BaseCellBackgroundFormat<CellInfo<Any>>() {
            override fun getBackGroundColor(cellInfo: CellInfo<Any>): Int {
                if(cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(requireContext(), R.color.grey);
                }
                return TableConfig.INVALID_COLOR;
            }

        }

        binding.table.config.isShowXSequence = false

        addListener()

        initViewModel()
    }

    private fun initViewModel() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        reportViewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        reportViewModel.reportListLiveData.observe(viewLifecycleOwner) { result ->
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
        binding.table.isVisible = !isVisible
    }

    private fun updateReportList(result: LoadListDataModel<List<ReportListInfo>?>) {
        result.rows?.let {
            if (result.isPullRefresh) {
                binding.table.setData(it)
            } else {
                if (it.isEmpty()) { //无更多数据
                    binding.smartRefreshLayout.setNoMoreData(true)
                } else {
                    binding.table.addData(it, true)
                }
            }

            if (binding.table.tableData.lineSize > 0) {
//            binding.table.tableData.columns[7].isFixed = true
                binding.table.tableData.columns[7].isAutoCount = true
                binding.table.tableData.columns[7].setDrawFormat(
                    MultiLineDrawFormat<String>(
                        requireContext(),
                        200
                    )
                )
            }

        }

        showNoData(binding.table.tableData.lineSize <= 0)
    }

    private fun loadData(isRefresh: Boolean) {
        if (isRefresh) {
            searchParams = null
            binding.smartRefreshLayout.setEnableLoadMore(true)
            page = 1
        } else {
            page++
        }
        reportViewModel.getReportList(page)
    }

    private fun addListener() {

        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(
                    MenuEnum.QM_WORKODERDETAIL_REPORT.printableName,
                    true
                )
            ) return@setOnClickListener

            showTipPopupWindow(binding.layoutSearch)
        }

        binding.tvExport.setOnClickListener {

            if (!RolePermissionUtils.hasPermission(
                    MenuEnum.QM_WORKODERDETAIL_REPORT.printableName,
                    true
                )
            ) return@setOnClickListener

            val fileName = "workOrder_${
                DateUtil.getFullData(System.currentTimeMillis()).replace(":", ".")
            }.xls"

            ExportDialogFragment.newInstance(
                "确定导出",
                "确定将当前列表的报表全部导出吗？导出后的文件会保存在本地Download/workOrder文件夹的下$fileName"
            )
                .addConfrimClickLisntener(object :
                    ExportDialogFragment.OnConfirmClickLisenter {
                    override fun onConfrimClick(isExportPicture: Boolean) {
                        exportWorkOrder(fileName, !isExportPicture)
                    }

                }).show(childFragmentManager, "close_case_dialog")
        }
    }

    private fun exportWorkOrder(fileName: String, noImage: Boolean) {
        PermissionHelper.Builder().with(this).build().request(
            "需要读写权限", Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) { granted, isAlwaysDenied ->
            if (granted) {
                //申请权限成功
                val directoryPictures =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                directoryPictures?.let {
                    val fileDirectory =
                        File(directoryPictures.absolutePath + File.separator + "workOrder")
                    if (!fileDirectory.exists()) {
                        fileDirectory.mkdirs()
                    }
                    val filePath = fileDirectory.absolutePath + File.separator + fileName
                    LogUtil.e("shenhe", "外置SD卡路径：" + fileDirectory.absolutePath)

                    lifecycleScope.launchWhenResumed {
                        showProgressbarLoading("正在导出...", true, false)
                        DownloadManager.downloadWorkOrderDetailReport(searchParams, File(filePath), noImage)
                            .collect {
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
        val popupWindow = ReportSearchPopupWindow(requireContext(), binding.root, searchParams)
        popupWindow.setOnClickListener(object : ReportSearchPopupWindow.OnSearchBtnClick {
            override fun onClick(searchParams: WorkOrderFragment.SearchParams) {
                showProgressbarLoading()
                this@ReportFragment.searchParams = searchParams
                binding.smartRefreshLayout.setEnableLoadMore(false)
                reportViewModel.searchReportList(searchParams)
            }

        })
        popupWindow.showAsDropDown(target, 0, 0)
    }
}