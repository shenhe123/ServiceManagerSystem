package com.jssg.servicemanagersystem.ui.workorder

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.bumptech.glide.Glide
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AppApplication
import com.jssg.servicemanagersystem.databinding.ActivityWorkOrderCheckDetailBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.adapter.ApplyInfoAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.UploadEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderCheckDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.PicturesPreviewActivity
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.SelectorPictureDialog
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.SelectorPictureViewModel
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.DpPxUtils
import com.jssg.servicemanagersystem.utils.FileUtils
import com.jssg.servicemanagersystem.utils.MyLocationClient
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.jssg.servicemanagersystem.widgets.decoration.ThemeLineItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import net.arvin.permissionhelper.PermissionHelper
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.Locale

class WorkOrderCheckDetailActivity : BaseActivity() {
    private var isEditable: Boolean = true
    private var uploadSize: Int = 0
    private var checkDate: String? = null
    private var state: Int = 0
    private val selectPictures = arrayListOf<UploadEntity>()
    private lateinit var selectPicturesViewModel: SelectorPictureViewModel
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private var inputData: WorkOrderCheckInfo? = null
    private lateinit var binding: ActivityWorkOrderCheckDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkOrderCheckDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        initData()

        addListener()
    }

    private fun addListener() {
        binding.ivAddBadPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance(0).show(supportFragmentManager, "selector_picture_dialog")
        }

        binding.ivAddBoxPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance(1).show(supportFragmentManager, "selector_picture_dialog")
        }

        binding.ivAddBatchInfoPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance(2).show(supportFragmentManager, "selector_picture_dialog")
        }

        binding.ivAddReworkPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance(3).show(supportFragmentManager, "selector_picture_dialog")
        }

        binding.mbtSave.setOnClickListener {
            onSubmit(0)
        }

        binding.mbtSubmit.setOnClickListener {
            onSubmit(1)
        }

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.tvCheck.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKDERDETAIL_APPROVE.printableName)) return@setOnClickListener

            inputData?.let {
                WorkOrderCheckDialogFragment.newInstance(it)
                    .addOnFinishListener(object :WorkOrderCheckDialogFragment.OnFinishListener{
                        override fun onFinish() {
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra("output", true)
                            })
                            finish()
                        }

                    })
                    .show(supportFragmentManager, "check_order_dialog")
            }
        }
    }

    private fun initViewModel() {
        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]
        selectPicturesViewModel = ViewModelProvider(this)[SelectorPictureViewModel::class.java]

        //网络图片加载
        selectPicturesViewModel.badOssListLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data.forEach {
                    it.tag = "bad" + it.url
                    selectPictures.add(it)
                    initImageWidget("bad", it.url, binding.xflBadPicture, binding.ivAddBadPhoto)
                    binding.ivAddBadPhoto.isVisible = binding.xflBadPicture.childCount < 3 && isEditable
                }
            }
        }

        selectPicturesViewModel.boxOssListLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data.forEach {
                    it.tag = "box" + it.url
                    selectPictures.add(it)
                    initImageWidget("box", it.url, binding.xflBoxPicture, binding.ivAddBadPhoto)
                    binding.ivAddBoxPhoto.isVisible = binding.xflBoxPicture.childCount < 3 && isEditable
                }
            }
        }

        selectPicturesViewModel.batchOssListLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data.forEach {
                    it.tag = "batch" + it.url
                    selectPictures.add(it)
                    initImageWidget("batch", it.url, binding.xflBatchInfoPicture, binding.ivAddBadPhoto)
                    binding.ivAddBatchInfoPhoto.isVisible = binding.xflBatchInfoPicture.childCount < 3 && isEditable
                }
            }
        }

        selectPicturesViewModel.reworkOssListLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data.forEach {
                    it.tag = "rework" + it.url
                    selectPictures.add(it)
                    initImageWidget("rework", it.url, binding.xflReworkPicture, binding.ivAddBadPhoto)
                    binding.ivAddReworkPhoto.isVisible = binding.xflReworkPicture.childCount < 3 && isEditable
                }
            }
        }

        //本地图片选择
        selectPicturesViewModel.badPicturesLiveData.observe(this) { pictures ->
            if (binding.xflBadPicture.childCount >= 3) {
                ToastUtils.showToast("不良图片最多只能选择3张！")
                return@observe
            }

            val available = 3 - binding.xflBadPicture.childCount
            val availablePic: List<LocalMedia?> = if (pictures.size >= available) {
                pictures.take(available)
            } else {
                pictures
            }

            uploadSize = availablePic.size
            showProgressbarLoading()
            availablePic.forEach { localMedia ->
                localMedia?.let {
                    initImageWidget("bad", it.availablePath, binding.xflBadPicture, binding.ivAddBadPhoto)
                }
            }

            binding.ivAddBadPhoto.isVisible = binding.xflBadPicture.childCount < 3
        }

        selectPicturesViewModel.boxPicturesLiveData.observe(this) { pictures ->
            if (binding.xflBoxPicture.childCount >= 3) {
                ToastUtils.showToast("外箱标签图片最多只能选择3张！")
                return@observe
            }

            val available = 3 - binding.xflBoxPicture.childCount
            val availablePic: List<LocalMedia?> = if (pictures.size >= available) {
                pictures.take(available)
            } else {
                pictures
            }

            uploadSize = availablePic.size
            showProgressbarLoading()
            availablePic.forEach { localMedia ->
                localMedia?.let {
                    initImageWidget("box", it.availablePath, binding.xflBoxPicture, binding.ivAddBoxPhoto)
                }
            }

            binding.ivAddBoxPhoto.isVisible = binding.xflBoxPicture.childCount < 3
        }

        selectPicturesViewModel.batchInfoPicturesLiveData.observe(this) { pictures ->
            if (binding.xflBatchInfoPicture.childCount >= 3) {
                ToastUtils.showToast("外箱标签图片最多只能选择3张！")
                return@observe
            }

            val available = 3 - binding.xflBatchInfoPicture.childCount
            val availablePic: List<LocalMedia?> = if (pictures.size >= available) {
                pictures.take(available)
            } else {
                pictures
            }

            uploadSize = availablePic.size
            showProgressbarLoading()
            availablePic.forEach { localMedia ->
                localMedia?.let {
                    initImageWidget("batch", it.availablePath, binding.xflBatchInfoPicture, binding.ivAddBatchInfoPhoto)
                }
            }

            binding.ivAddBatchInfoPhoto.isVisible = binding.xflBatchInfoPicture.childCount < 3
        }

        selectPicturesViewModel.reworkPicturesLiveData.observe(this) { pictures ->
            if (binding.xflReworkPicture.childCount >= 5) {
                ToastUtils.showToast("返回数量图片最多只能选择5张！")
                return@observe
            }

            val available = 5 - binding.xflBadPicture.childCount
            val availablePic: List<LocalMedia?> = if (pictures.size >= available) {
                pictures.take(available)
            } else {
                pictures
            }

            uploadSize = availablePic.size
            showProgressbarLoading()
            availablePic.forEach { localMedia ->
                localMedia?.let {
                    initImageWidget("rework", it.availablePath, binding.xflReworkPicture, binding.ivAddReworkPhoto)
                }
            }

            binding.ivAddReworkPhoto.isVisible = binding.xflReworkPicture.childCount < 5
        }

        selectPicturesViewModel.fileOssUploadLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data?.let {
                    uploadSize--
                    if (uploadSize <= 0) {
                        hideLoading()
                    }
                    selectPictures.add(it)
                }
            }
        }

        workOrderViewModel.updateWorkOrderDetailLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                if (state == 0) {
                    ToastUtils.showToast("保存成功")
                } else {
                    ToastUtils.showToast("提交成功")
                    finish()
                }
            }
        }
    }

    private fun onSubmit(state: Int) {
        if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_EDIT.printableName)) return

        this.state = state
        val locationStr = binding.tvLocationAddress.text.toString()
        if (locationStr.isEmpty()) {
            getLocationInfo()
            ToastUtils.showToast("服务地点不可为空")
            return
        }

        val badPictures = selectPictures.filter { it.tag.startsWith("bad") }
        if (badPictures.isEmpty()) {
            ToastUtils.showToast("请上传不良图片")
            return
        }

        val badOssIds = badPictures.map { it.ossId }

        val boxPictures = selectPictures.filter { it.tag.startsWith("box") }
        if (boxPictures.isEmpty()) {
            ToastUtils.showToast("请上传外箱标签图片")
            return
        }

        val boxOssIds = boxPictures.map { it.ossId }

        val batchPictures = selectPictures.filter { it.tag.startsWith("batch") }
        if (batchPictures.isEmpty()) {
            ToastUtils.showToast("请上传批次信息图片")
            return
        }

        val batchOssIds = batchPictures.map { it.ossId }

        val reworkPictures = selectPictures.filter { it.tag.startsWith("rework") }
        if (reworkPictures.isEmpty()) {
            ToastUtils.showToast("请上传返工图片")
            return
        }

        val reworkOssIds = reworkPictures.map { it.ossId }

        val checkNum = binding.etCheckNum.text.toString()
        if (checkNum.isEmpty()) {
            ToastUtils.showToast("请输入排查数量")
            return
        }
        val badNum = binding.etBadNum.text.toString()
        if (badNum.isEmpty()) {
            ToastUtils.showToast("请输入不良数量")
            return
        }

        inputData?.let {
            workOrderViewModel.updateWorkOrderDetail(
                it.billNo,
                it.billDetailNo,
                locationStr,
                badOssIds.joinToString(","),
                boxOssIds.joinToString(","),
                batchOssIds.joinToString(","),
                reworkOssIds.joinToString(","),
                checkNum,
                badNum,
                checkDate.toString(),
                state,
                binding.etRemark.text.toString()
            )
        }
    }

    private fun initData() {
        inputData = intent?.getParcelableExtra<WorkOrderCheckInfo>("input")
        inputData?.let {
            binding.tvLocationAddress.text = it.place
            binding.tvCheckDate.text = it.checkDate
            checkDate = it.checkDate
            val checkNumTotal = it.workOrderVo?.checkNumTotal ?: 0
            binding.tvCheckNumTotal.text = checkNumTotal.toString()
            val badNumTotal = it.workOrderVo?.badNumTotal ?: 0
            binding.tvBadNumTotal.text = badNumTotal.toString()
            binding.etCheckNum.setText(it.checkNum.toString())
            binding.etBadNum.setText(it.badNum.toString())
            binding.etRemark.setText(it.remark)

            if (it.state == 1) { //已提交，是不能修改为已保存的
                //所以需隐藏保存按钮
                binding.mbtSave.isVisible = false
            }

            if (it.state == 2 || it.state == 4) {
                //该单子已审核或已退回，不能再修改，或者审核了
                binding.mbtSave.isVisible = false
                binding.mbtSubmit.isVisible = false
                binding.tvCheck.isVisible = false

                isEditable = false
            }

            if (it.state > 1) {
                //已经有审核意见
                binding.layoutReviewRemark.isVisible = true

                binding.recyclerView.layoutManager = object :LinearLayoutManager(this){
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                binding.recyclerView.addItemDecoration(ThemeLineItemDecoration())
                val applyInfoAdapter = ApplyInfoAdapter()
                binding.recyclerView.adapter = applyInfoAdapter

                applyInfoAdapter.setList(it.applyInfoVos)
            }

            binding.etBadNum.isEnabled = isEditable
            binding.etCheckNum.isEnabled = isEditable
            binding.etRemark.isEnabled = isEditable

            if (it.place.isNullOrEmpty()) {
                getLocationInfo()
            }

            selectPicturesViewModel.getBadPictures(it.badPicNames)
            selectPicturesViewModel.getBoxPictures(it.badPicNames)
            selectPicturesViewModel.getBatchPictures(it.badPicNames)
            selectPicturesViewModel.getReworkPictures(it.badPicNames)
        }
    }

    private fun getLocationInfo() {
        val permissionHelper = PermissionHelper.Builder().with(this).build()
        permissionHelper.request("需要位置权限", arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        ) { granted, isAlwaysDenied ->
            if (granted) {
                val locationClient = MyLocationClient().init(this)
                locationClient.registerLocationListener(object : BDAbstractLocationListener() {
                    override fun onReceiveLocation(location: BDLocation?) {
                        location?.let {
                            binding.tvLocationAddress.text = it.addrStr
                        }

                        if (location != null) locationClient.stop()
                    }

                })
                locationClient.start()
            }
        }
    }

    private fun initImageWidget(tag: String, url: String, parent: ViewGroup, addView: ImageView) {
        val img = ImageView(this)
        val width = DpPxUtils.dip2px(this, 66f)
        img.layoutParams = LinearLayout.LayoutParams(width, width)
        img.scaleType = ImageView.ScaleType.FIT_XY
        if (url.startsWith("http") || url.startsWith("content")) {
            Glide.with(this).load(url).into(img)
        } else {
            Glide.with(this).load(File(url)).into(img)
        }
        img.tag = tag + url

        if (checkDate.isNullOrEmpty() && tag.equals("rework", false)) {
            checkDate = DateUtil.getFullData(System.currentTimeMillis())
            binding.tvCheckDate.text = checkDate
        }

        img.setOnLongClickListener {

            SingleBtnDialogFragment.newInstance("删除图片", "确定要删除图片吗？")
                .addConfrimClickLisntener(object : SingleBtnDialogFragment.OnConfirmClickLisenter{
                    override fun onConfrimClick() {
                        parent.removeView(it)
                        addView.isVisible = true

                        selectPictures.removeIf { p -> p.tag.equals(img.tag.toString(), false) }
                    }

                })
                .show(supportFragmentManager, "delete")

            return@setOnLongClickListener true
        }

        img.setOnClickListener {//预览
            PicturesPreviewActivity.goActivity(this, url)
        }

        if (!url.startsWith("http")) {
            val file = getOriginPathFile(url)
//            val compressFile = compressFile(file)
            selectPicturesViewModel.fileOssUpload(file, "$tag.$url")
        }
        parent.addView(img)
    }


    private fun getOriginPathFile(path: String): File {
        val fileOriginPath = if (path.startsWith("content")) {
            FileUtils.getFileOriginPath(AppApplication.get(), Uri.parse(path))
        } else path

        return File(fileOriginPath)
    }

    private fun compressFile(file: File): File {
        var compressFile: File = file
        Luban.with(this)
            .load(file)
            .ignoreBy(100)
            .setTargetDir(cacheDir.absolutePath)
            .filter { path ->
                !(TextUtils.isEmpty(path) || path.lowercase(Locale.getDefault())
                    .endsWith(".gif"))
            }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                    //压缩开始前调用，可以在方法内启动 loading UI
                    showProgressbarLoading()
                }

                override fun onSuccess(file: File?) {
                    // 压缩成功后调用，返回压缩后的图片文件
                    file?.let {
                        compressFile = file
                    }
                }

                override fun onError(e: Throwable?) {
                    // 当压缩过程出现问题时调用
                    compressFile = file
                }
            }).launch()

        return compressFile
    }

    class WorkOrderCheckContracts: ActivityResultContract<WorkOrderCheckInfo, Boolean?>(){
        override fun createIntent(context: Context, input: WorkOrderCheckInfo): Intent {
            return Intent(context, WorkOrderCheckDetailActivity::class.java).apply {
                putExtra("input", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else null
        }

    }
}