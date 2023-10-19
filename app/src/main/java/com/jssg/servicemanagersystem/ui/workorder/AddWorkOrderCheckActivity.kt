package com.jssg.servicemanagersystem.ui.workorder

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.bumptech.glide.Glide
import com.ezreal.audiorecordbutton.AudioPlayManager
import com.ezreal.audiorecordbutton.AudioRecordButton
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AppApplication
import com.jssg.servicemanagersystem.databinding.ActivityAddWorkOrderCheckBinding
import com.jssg.servicemanagersystem.databinding.ItemImageViewBinding
import com.jssg.servicemanagersystem.helper.AudioPlayHandler
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.adapter.AudioRecordAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.AudioRecordEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.UploadEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.popup.AddBatchNoDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.PicturesPreviewActivity
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.SelectorPictureDialog
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.SelectorPictureViewModel
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.BigDecimalUtils.bigDecimal
import com.jssg.servicemanagersystem.utils.BitmapUtils
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.FileUtils
import com.jssg.servicemanagersystem.utils.LogUtil
import com.jssg.servicemanagersystem.utils.MyLocationClient
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.luck.picture.lib.entity.LocalMedia
import net.arvin.permissionhelper.PermissionHelper
import java.io.File

class AddWorkOrderCheckActivity : BaseActivity() {
    private lateinit var audioAdapter: AudioRecordAdapter
    private var isAudioPlay: Boolean = false
    private var mAudioPlayHandler: AudioPlayHandler? = null
    private lateinit var permissionHelper: PermissionHelper
    private var billDetailNo: String? = null
    private var uploadSize: Int = 0
    private var state: Int = 0
    private var checkDate: String? = null
    private val selectPictures = arrayListOf<UploadEntity>()
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private var inputData: WorkOrderInfo? = null
    private lateinit var selectorPictureViewModel: SelectorPictureViewModel
    private lateinit var binding: ActivityAddWorkOrderCheckBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddWorkOrderCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        inputData = intent?.getParcelableExtra<WorkOrderInfo>("input")
        inputData?.let {
            binding.tvBadNum.text = it.productNum
            binding.tvCheckNumTotal.text = (it.checkNumTotal ?: 0).toString()
            binding.tvBadNumTotal.text = (it.badNumTotal ?: 0).toString()
        }

        initAudioPermission()

        initViewModel()

        getLocationInfo()

        addListener()
    }

    private fun initAudioPermission() {
        permissionHelper = PermissionHelper.Builder().with(this).build()

        binding.audioRecyclerView.layoutManager = LinearLayoutManager(this)
        audioAdapter = AudioRecordAdapter()
        binding.audioRecyclerView.adapter = audioAdapter

        audioAdapter.setOnItemChildClickListener{ _, view, position ->
            if (view.id == R.id.layout_audio_record_msg) {
                playAudio(audioAdapter.data[position], position)
            }
        }

        audioAdapter.setOnItemChildLongClickListener{ _, view, position ->
            if (view.id == R.id.layout_audio_record_msg) {
                  SingleBtnDialogFragment.newInstance("删除语音", "确定要删除语音条吗？")
                    .addConfrimClickLisntener(object :SingleBtnDialogFragment.OnConfirmClickLisenter{
                        override fun onConfrimClick() {
                            selectPictures.removeIf { p -> p.tag.equals(audioAdapter.data[position].getTag(), false) }

                            audioAdapter.removeAt(position)
                            binding.ivAddAudioRecord.isVisible = true
                        }

                    })
                    .show(supportFragmentManager, "delete_audio")
            }

            return@setOnItemChildLongClickListener true
        }

        // 录音按钮初始化和录音监听
        binding.btnAudioRecord.init(cacheDir.absolutePath + File.separator + "audio")
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

        binding.ivAddAudioRecord.setOnClickListener {
            permissionHelper.request("需要录音权限", Manifest.permission.RECORD_AUDIO
            ) { granted, isAlwaysDenied ->
                if (granted) {
                    showAudioRecordButton()
                }
            }
        }

        binding.ivCloseAudioRecord.setOnClickListener {
            hideAudioRecordButton()
        }

        binding.mbtAddBatchNo.setOnClickListener {
            AddBatchNoDialogFragment.newInstance().addConfrimClickLisntener(object :AddBatchNoDialogFragment.OnConfirmClickLisenter {
                override fun onConfirmClick(batchNo: String) {
                    val batchNos = binding.tvBatchNo.text.toString()
                    if (batchNos.isEmpty()) {
                        binding.tvBatchNo.text = batchNo
                    } else {
                        binding.tvBatchNo.text = "$batchNos,$batchNo"
                    }

                    binding.ivBatchNoClose.isVisible = true
                }

            }).show(supportFragmentManager, "add_batch_no_dialog")
        }

        binding.ivBatchNoClose.setOnClickListener {
            binding.tvBatchNo.text = ""
            binding.ivBatchNoClose.isVisible = false
        }

        binding.btnAudioRecord.setRecordingListener(object : AudioRecordButton.OnRecordingListener {
            override fun recordFinish(audioFilePath: String?, recordTime: Long) {
                audioFilePath?.let {
                    selectorPictureViewModel.audioRecordLiveData.value = AudioRecordEntity(audioFilePath, recordTime)
                }
            }

            override fun recordError(message: String?) {
                ToastUtils.showToast(message)
            }

        })

        binding.mbtSave.setOnClickListener {
            onSubmit(0)
        }

        binding.mbtSubmit.setOnClickListener {
            onSubmit(1)
        }
    }

    private fun showAudioRecordButton() {
        binding.layoutAudioRecord.isVisible = true
        binding.layoutNormal.isVisible = false
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

        val batchNo = binding.tvBatchNo.text.toString()
        if (batchNo.isEmpty()) {
            ToastUtils.showToast("批次号不能为空")
            return
        }

        val batchOssIds = batchPictures.map { it.ossId }

        val reworkPictures = selectPictures.filter { it.tag.startsWith("rework") }
        if (reworkPictures.isEmpty()) {
            ToastUtils.showToast("请上传返工图片")
            return
        }

        val reworkOssIds = reworkPictures.map { it.ossId }

        val audioOssIds = selectPictures.filter { it.tag.startsWith("audio") }.map { it.ossId }

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

            val availableCheckNum = it.checkNum.bigDecimal().subtract(it.checkNumTotal.bigDecimal())
            if (checkNum.bigDecimal() > availableCheckNum) {
                ToastUtils.showToast("不能超过最大可排查数量${availableCheckNum.toInt()}")
                return
            }

            if (badNum.bigDecimal() > checkNum.bigDecimal()) {
                ToastUtils.showToast("不良数量不能超过排查数量")
                return
            }

            workOrderViewModel.addWorkOrderDetail(
                it.billNo,
                billDetailNo,
                locationStr,
                badOssIds.joinToString(","),
                boxOssIds.joinToString(","),
                batchOssIds.joinToString(","),
                reworkOssIds.joinToString(","),
                audioOssIds.joinToString(","),
                checkNum,
                badNum,
                checkDate.toString(),
                state,
                binding.etRemark.text.toString(),
                batchNo
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

        selectorPictureViewModel.audioRecordLiveData.observe(this) { result ->
            if (audioAdapter.data.size >= 3) {
                ToastUtils.showToast("最多只能录制3条语音！")
                return@observe
            }

            uploadSize = 1
            showProgressbarLoading()
            audioAdapter.addData(result)

            val file = getOriginPathFile(result.audioFilePath)
            selectorPictureViewModel.fileOssUpload(file, result.getTag())

            binding.ivAddAudioRecord.isVisible = audioAdapter.data.size < 3

            hideAudioRecordButton()
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

    private fun hideAudioRecordButton() {
        binding.layoutAudioRecord.isVisible = false
        binding.layoutNormal.isVisible = true
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
        val fileSize = FileUtils.getAutoFileOrFilesSize(file.absolutePath)
        LogUtil.e("shenhe", "压缩前$fileSize")
        val directoryPictures =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        directoryPictures?.let {
            val fileDirectory =
                File(directoryPictures.absolutePath + File.separator + "cache")
            if (!fileDirectory.exists()) {
                fileDirectory.mkdirs()
            }
            val compressFile = BitmapUtils.compressFile(file, fileDirectory.absolutePath + File.separator + "compress_img_cache.jpg")
            if (compressFile != null) {
                val compressFileSize = FileUtils.getAutoFileOrFilesSize(compressFile.absolutePath)
                LogUtil.e("shenhe", "压缩后$compressFileSize")
                selectorPictureViewModel.fileOssUpload(compressFile, "$tag.$path")
            } else {
                selectorPictureViewModel.fileOssUpload(file, "$tag.$path")
            }
        }

        if (directoryPictures == null) {
            selectorPictureViewModel.fileOssUpload(file, "$tag.$path")
        }
        parent.addView(img)
    }

//    private fun initAudioWidget(result: AudioRecordEntity?, flowAudio: FlowLayout) {
//        result?.let { audioRecord ->
//            val audioView = layoutInflater.inflate(R.layout.item_audio_record_layout, null)
//            val bind = ItemAudioRecordLayoutBinding.bind(audioView)
//
//            bind.frameContainer.layoutParams = ViewGroup.LayoutParams(-1, -2)
//
//            bind.tvAudioTime.text = "${audioRecord.recordTime / 1000}''"
//
//            bind.layoutAudioRecordMsg.setOnClickListener {
//                playAudio(bind.layoutAudioRecordMsg, bind.ivAudioSound, audioRecord.audioFilePath, audioRecord.recordTime)
//            }
//
//            val tag = "audio.${audioRecord.audioFilePath}"
//            audioView.tag = tag
//
//            bind.layoutAudioRecordMsg.setOnLongClickListener {
//                SingleBtnDialogFragment.newInstance("删除语音", "确定要删除语音条吗？")
//                    .addConfrimClickLisntener(object :SingleBtnDialogFragment.OnConfirmClickLisenter{
//                        override fun onConfrimClick() {
//                            flowAudio.removeView(it)
//                            binding.ivAddAudioRecord.isVisible = true
//
//                            selectPictures.removeIf { p -> p.tag.equals(audioView.tag.toString(), false) }
//                        }
//
//                    })
//                    .show(supportFragmentManager, "delete_audio")
//
//                return@setOnLongClickListener true
//            }
//
//            val file = getOriginPathFile(audioRecord.audioFilePath)
//            selectorPictureViewModel.fileOssUpload(file, tag)
//
//            flowAudio.addView(audioView)
//        }
//    }

    private fun getOriginPathFile(path: String): File {
        val fileOriginPath = if (path.startsWith("content")) {
            FileUtils.getFileOriginPath(AppApplication.get(), Uri.parse(path))
        } else path

        return File(fileOriginPath)
    }

    /*** 播放音频，并监听播放进度，更新页面动画  */
    private fun playAudio(audioRecordEntity: AudioRecordEntity, pos: Int) {
        if (isAudioPlay) {
            // 如果正在播放，那会先关闭当前播放
            AudioPlayManager.pause()
            AudioPlayManager.release()
            mAudioPlayHandler?.stopAnimTimer()
            isAudioPlay = false

            return
        }

        if (mAudioPlayHandler == null) {
            mAudioPlayHandler = AudioPlayHandler()
        }

        //播放音频和动画
        AudioPlayManager.playAudio(this, audioRecordEntity.audioFilePath,
            object : AudioPlayManager.OnPlayAudioListener {
                override fun onPlay() {
                    // 启动播放动画
                    isAudioPlay = true
                    mAudioPlayHandler?.startAudioAnim(audioAdapter, pos, true)
                }

                override fun onComplete() {
                    isAudioPlay = false
                    mAudioPlayHandler?.stopAnimTimer()
                }

                override fun onError(message: String) {
                    isAudioPlay = false
                    mAudioPlayHandler?.stopAnimTimer()
                    ToastUtils.showToast(message)
                }
            })
    }

    class AddWordOrderDetailContracts: ActivityResultContract<WorkOrderInfo, Boolean?>(){
        override fun createIntent(context: Context, input: WorkOrderInfo): Intent {
            return Intent(context, AddWorkOrderCheckActivity::class.java).apply {
                putExtra("input", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else null
        }

    }

}