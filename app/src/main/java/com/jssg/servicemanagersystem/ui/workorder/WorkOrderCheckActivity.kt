package com.jssg.servicemanagersystem.ui.workorder

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.bumptech.glide.Glide
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AppApplication
import com.jssg.servicemanagersystem.databinding.ActivityOrderCheckBinding
import com.jssg.servicemanagersystem.databinding.ItemImageViewBinding
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.entity.UploadEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.PicturesPreviewActivity
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.SelectorPictureDialog
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.SelectorPictureViewModel
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.DpPxUtils
import com.jssg.servicemanagersystem.utils.FileUtils
import com.jssg.servicemanagersystem.utils.LogUtil
import com.jssg.servicemanagersystem.utils.MyLocationClient
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.parcel.Parcelize
import net.arvin.permissionhelper.PermissionHelper
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.Locale

class WorkOrderCheckActivity : BaseActivity() {
    private var billDetailNo: String? = null
    private var uploadSize: Int = 0
    private var state: Int = 0
    private var checkDate: String? = null
    private val selectPictures = arrayListOf<UploadEntity>()
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private var inputData: WorkOrderInfo? = null
    private lateinit var selectorPictureViewModel: SelectorPictureViewModel
    private lateinit var binding: ActivityOrderCheckBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        inputData = intent?.getParcelableExtra<WorkOrderInfo>("input")
        inputData?.let {
            binding.tvBadNum.text = it.productNum
            binding.tvCheckNumTotal.text = (it.checkNumTotal ?: 0).toString()
            binding.tvBadNumTotal.text = (it.badNumTotal ?: 0).toString()
        }

        initViewModel()

        getLocationInfo()

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
    }

    private fun onSubmit(state: Int) {
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
            workOrderViewModel.addWorkOrderDetail(
                it.billNo,
                billDetailNo,
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

    private fun initViewModel() {
        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]
        selectorPictureViewModel = ViewModelProvider(this)[SelectorPictureViewModel::class.java]

        selectorPictureViewModel.badPicturesLiveData.observe(this) { pictures ->
            if (binding.xflBadPicture.childCount >= 3) {
                ToastUtils.showToast("不良图片最多只能选择3张！")
                return@observe
            }

            if (pictures.size > 3) {
                ToastUtils.showToast("不良图片最多只能选择3张！")
            }

            val availablePic: List<LocalMedia?> = if (pictures.size >= 3) {
                pictures.take(3)
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

        selectorPictureViewModel.boxPicturesLiveData.observe(this) { pictures ->
            if (binding.xflBoxPicture.childCount >= 3) {
                ToastUtils.showToast("外箱标签图片最多只能选择3张！")
                return@observe
            }

            if (pictures.size > 3) {
                ToastUtils.showToast("外箱标签图片最多只能选择3张！")
            }

            val availablePic: List<LocalMedia?> = if (pictures.size >= 3) {
                pictures.take(3)
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

        selectorPictureViewModel.batchInfoPicturesLiveData.observe(this) { pictures ->
            if (binding.xflBatchInfoPicture.childCount >= 3) {
                ToastUtils.showToast("批次信息图片最多只能选择3张！")
                return@observe
            }

            if (pictures.size > 3) {
                ToastUtils.showToast("批次信息图片最多只能选择3张！")
            }

            val availablePic: List<LocalMedia?> = if (pictures.size >= 3) {
                pictures.take(3)
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

        selectorPictureViewModel.reworkPicturesLiveData.observe(this) { pictures ->
            if (binding.xflReworkPicture.childCount >= 5) {
                ToastUtils.showToast("返回数量图片最多只能选择5张！")
                return@observe
            }

            if (pictures.size > 5) {
                ToastUtils.showToast("返回数量图片最多只能选择5张！")
            }

            val availablePic: List<LocalMedia?> = if (pictures.size >= 5) {
                pictures.take(5)
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

        selectorPictureViewModel.fileOssUploadLiveData.observe(this) { result ->
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

        workOrderViewModel.addWorkOrderDetailLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                if (state == 0) {
                    ToastUtils.showToast("保存成功")

                    billDetailNo = result.data.toString()
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra("output", true)
                    })

                    finish()
                } else {
                    ToastUtils.showToast("提交成功")
                    billDetailNo = result.data.toString()
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra("output", true)
                    })
                    finish()
                }
            }
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

    private fun initImageWidget(tag: String, path: String, parent: ViewGroup, addView: ImageView) {
        val img = layoutInflater.inflate(R.layout.item_image_view, null)
        val bind = ItemImageViewBinding.bind(img)
//        ToastUtils.showToast("图片路径---$path")
        if (path.startsWith("content")) {
            Glide.with(this).load(path).into(bind.ivImage)
        } else {
            Glide.with(this).load(File(path)).into(bind.ivImage)
        }

        img.tag = tag + path

        if (checkDate.isNullOrEmpty() && tag.equals("rework", false)) {
            checkDate = DateUtil.getFullData(System.currentTimeMillis())
            binding.tvCheckDate.text = checkDate
        }

        img.setOnLongClickListener {

            SingleBtnDialogFragment.newInstance("删除图片", "确定要删除图片吗？")
                .addConfrimClickLisntener(object :SingleBtnDialogFragment.OnConfirmClickLisenter{
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
            PicturesPreviewActivity.goActivity(this, path)
        }

        val file = getOriginPathFile(path)
//        val compressFile = compressFile(file)
        selectorPictureViewModel.fileOssUpload(file, "$tag.$path")

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
                        LogUtil.e("shenhe", "压缩成功 -- ${compressFile.absolutePath}")
                    }
                }

                override fun onError(e: Throwable?) {
                    // 当压缩过程出现问题时调用
                    compressFile = file
                    LogUtil.e("shenhe", "压缩失败 -- ${compressFile.absolutePath}")
                }
            }).launch()

        return compressFile
    }

    class AddWordOrderDetailContracts: ActivityResultContract<WorkOrderInfo, Boolean?>(){
        override fun createIntent(context: Context, input: WorkOrderInfo): Intent {
            return Intent(context, WorkOrderCheckActivity::class.java).apply {
                putExtra("input", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else null
        }

    }

}