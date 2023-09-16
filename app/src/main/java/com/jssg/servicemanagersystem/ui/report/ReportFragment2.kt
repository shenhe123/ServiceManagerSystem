package com.jssg.servicemanagersystem.ui.report

import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jssg.servicemanagersystem.BuildConfig
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.core.Constants
import com.jssg.servicemanagersystem.databinding.FragmentReport2Binding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment
import com.jssg.servicemanagersystem.ui.workorder.popup.WorkOrderSearchPopupWindow
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils


class ReportFragment2 : BaseFragment() {

    companion object {
        fun newInstance() = ReportFragment2().apply {
            Bundle().apply {

            }
        }
    }

    private var hasSetCookies = false
    private var searchParams: WorkOrderFragment.SearchParams? = null
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var binding: FragmentReport2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReport2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWebView()
        addListener()

        initViewModel()
    }

    private fun initWebView() {
        initWebSettings(binding.webView)

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress in 1..100) {
                    if (!binding.progressBar.isVisible) {
                        binding.progressBar.isVisible = true
                    }
                    binding.progressBar.progress = newProgress
                }
                if (newProgress >= 20) {
                    view.settings.loadsImagesAutomatically = true
                    view.settings.blockNetworkImage = false
                }
                if (newProgress >= 100) {
                    view.postDelayed({ progressChanged(view) }, 100)
                }
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {

                request?.url?.let {
                    val response = RetrofitService.apiService.getWorkOrderDetailExport().execute()
                    return WebResourceResponse(
                        response.headers()["content-type"] ?: response.body()?.contentType()?.type,
                        response.headers()["content-encoding"] ?: "utf-8",
                        response.body()?.byteStream()
                    )
                }

                return super.shouldInterceptRequest(view, request)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError?
            ) {
                handler.proceed() //兼容https，信任所有证书
            }
        }
    }

    private fun initViewModel() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]

        accountViewModel.userProfileLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                judgeRolePermission()
            }
        }

        lifecycleScope.launchWhenResumed {
            if (AccountManager.instance.getUser() == null) {
                accountViewModel.getUserProfile()
            }
            loadData()
        }

    }

    private fun initWebSettings(webView: WebView): WebSettings {
        val webSettings = webView.settings
        // user-agent
        val newUserAgent = WebViewTools.setWebviewUserAgent(webSettings.userAgentString)
        webSettings.userAgentString = newUserAgent

        //设置开启、关闭新窗口 一个activity 只有一个webView窗口。所以默认关闭
        webSettings.setSupportMultipleWindows(false)

        webSettings.blockNetworkImage = true//阻塞图片加载,页面有内容后再放开

        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE//根据cache-control决定是否从网络上取数据。

        //银行卡买币供应商 要求开启的setting
        webSettings.javaScriptCanOpenWindowsAutomatically = true//让JavaScript自动打开窗口，默认false
        webSettings.allowFileAccess = true//是否允许访问文件，默认允许
        webSettings.allowContentAccess = true//是否允许在WebView中访问内容URL

        return webSettings
    }

    /**
     * 进度条结束后的一些操作
     */
    private fun progressChanged(view: WebView?) {
        binding.progressBar.visibility = View.GONE
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

    private fun loadData() {
        val baseUrl: String = if (BuildConfig.IS_TEST_HOST) Constants.Test_Host else Constants.Release_Host
        binding.webView.loadUrl("${baseUrl}staging-api/qm/workOrderDetail/reportExport")
    }

    private fun addListener() {

        binding.layoutSearch.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_QUERY.printableName, true)) return@setOnClickListener

            showTipPopupWindow(binding.layoutSearch)
        }

        binding.tvExport.setOnClickListener {

            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKODERDETAIL_REPORT.printableName, true)) return@setOnClickListener

            SingleBtnDialogFragment.newInstance("确定导出", "确定将当前所有的工单报表导出吗？")
                .addConfrimClickLisntener(object :
                    SingleBtnDialogFragment.OnConfirmClickLisenter {
                    override fun onConfrimClick() {

                    }

                }).show(childFragmentManager, "close_case_dialog")

        }
    }

    private fun showTipPopupWindow(target: View) {
        val popupWindow = WorkOrderSearchPopupWindow(requireContext(), binding.root, searchParams)
        popupWindow.setOnClickListener(object : WorkOrderSearchPopupWindow.OnSearchBtnClick {
            override fun onClick(searchParams: WorkOrderFragment.SearchParams) {
                showProgressbarLoading()
                this@ReportFragment2.searchParams = searchParams
//                workOrderViewModel.searchWorkOrder(searchParams)
            }

        })
        popupWindow.showAsDropDown(target, 0, 0)
    }
}