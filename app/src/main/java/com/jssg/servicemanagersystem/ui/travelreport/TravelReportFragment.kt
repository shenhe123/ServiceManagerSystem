package com.jssg.servicemanagersystem.ui.travelreport

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.download.DownloadManager
import com.jssg.servicemanagersystem.base.download.DownloadState
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.FragmentTravelReportBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.travelreport.adapter.TravelReportAdapter
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.travelreport.popup.TravelReportSearchPopupWindow
import com.jssg.servicemanagersystem.ui.travelreport.viewmodel.TravelReportViewModel
import com.jssg.servicemanagersystem.utils.LogUtil
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.arvin.permissionhelper.PermissionHelper
import java.io.File

class TravelReportFragment : BaseFragment() {

    private var permissionHelper: PermissionHelper? = null
    private var searchParams: TravelSearchParams? = null
    private lateinit var accountViewModel: AccountViewModel
    private var page: Int = 1
    private lateinit var sourceList: MutableList<TravelReportInfo>
    private lateinit var adapter: TravelReportAdapter
    private lateinit var travelReportViewModel: TravelReportViewModel
    private lateinit var binding: FragmentTravelReportBinding

    private val addNewLauncher = registerForActivityResult(AddTravelReportActivity.AddTravelReportContracts()){
        it?.let {
            loadData(true)
        }
    }

    private val detailLauncher = registerForActivityResult(TravelReportDetailActivity.TravelReportDetailContracts()){
        it?.let {
            adapter.data[it.pos] = it.inputData
            adapter.notifyItemChanged(it.pos)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTravelReportBinding.inflate(inflater, container, false)
        travelReportViewModel = ViewModelProvider(this)[TravelReportViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionHelper = PermissionHelper.Builder().with(this).build()

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

    private fun judgeRolePermission() {
        binding.fbtnAddNew.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.QM_TRIPREPORT_ADD.printableName)
    }

    private fun showNoData(isVisible: Boolean) {
        binding.tvEmpty.isVisible = isVisible
    }

    private fun updateWorkOrderList(result: LoadListDataModel<List<TravelReportInfo>?>) {
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

        sourceList = adapter.data

        showNoData(adapter.data.isEmpty())
    }

    private fun loadData(isRefresh: Boolean) {
        page = if (isRefresh) {
            1
        } else {
            page + 1
        }
        travelReportViewModel.getTravelReportList(isRefresh, page)
    }

    override fun onResume() {
        super.onResume()
        if (AccountManager.instance.getUser() != null) {
            judgeRolePermission()
        }
    }

    private fun addListener() {

        adapter.setOnItemClickListener { _, _, position ->
            val travelReportInfo = adapter.data[position]
            detailLauncher.launch(TravelReportDetailActivity.TravelReportInputData(travelReportInfo, position))
        }

        adapter.setOnItemChildClickListener { _, view, position ->
            val travelReportInfo = adapter.data[position]
            if (view.id == R.id.mbt_export) {

                permissionHelper?.request("需要读写权限", Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) { granted, isAlwaysDenied ->
                    if (granted) {
                        //申请权限成功
                        val directoryPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                        directoryPictures?.let {
                            val fileDirectory = File(directoryPictures.absolutePath + File.separator + "tripReport")
                            if (!fileDirectory.exists()) {
                                fileDirectory.mkdirs()
                            }
                            val fileName = fileDirectory.absolutePath + File.separator + "${travelReportInfo.billNo}.xls"
                            LogUtil.e("shenhe", "外置SD卡路径：" + fileDirectory.absolutePath)

                            SingleBtnDialogFragment.newInstance("出差报告导出", "确定要将当前出差报告导出吗？导出后的文件会保存在本地Download/tripReport文件夹的下${travelReportInfo.billNo}.xls")
                                .addConfrimClickLisntener(object :SingleBtnDialogFragment.OnConfirmClickLisenter{
                                    override fun onConfrimClick() {
                                        showProgressbarLoading()
                                        lifecycleScope.launchWhenResumed {
                                            DownloadManager.download(travelReportInfo.billNo, File(fileName)).collect {
                                                when (it) {
                                                    is DownloadState.InProgress -> {
//                                                        Log.d("~~~", "download in progress: ${it.progress}.")
                                                    }
                                                    is DownloadState.Success -> {
//                                                        Log.d("~~~", "download finished.")
                                                        hideLoading()
                                                        ToastUtils.showToast("导出成功")
                                                    }
                                                    is DownloadState.Error -> {
//                                                        Log.d("~~~", "download error: ${it.throwable}.")
                                                        hideLoading()
                                                        ToastUtils.showToast(it.throwable.message)
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }).show(childFragmentManager, "export_travel_report")
                        }
                    }
                }
            }
        }

        binding.fbtnAddNew.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_TRIPREPORT_ADD.printableName, true)) return@setOnClickListener

            addNewLauncher.launch("")
        }

        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_QUERY.printableName, true)) return@setOnClickListener

            showTipPopupWindow(binding.layoutSearch)
        }
    }

    private fun showTipPopupWindow(target: View) {
        val popupWindow = TravelReportSearchPopupWindow(requireContext(), binding.root, searchParams)
        popupWindow.setOnClickListener(object : TravelReportSearchPopupWindow.OnSearchBtnClick {
            override fun onClick(searchParams: TravelSearchParams) {
                showProgressbarLoading()
                this@TravelReportFragment.searchParams = searchParams
                travelReportViewModel.searchTravelReport(searchParams)
            }

        })
        popupWindow.showAsDropDown(target, 0, 0)
    }

    @Parcelize
    data class TravelSearchParams(
        val applyName: String?,
        val startDate: String?,
        val endDate: String?,
        val orgName: String,
        val dept: String,
    ) : Parcelable

    companion object {
        fun newInstance() =
            TravelReportFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable("inputData", inputData)
                }
            }
    }
}