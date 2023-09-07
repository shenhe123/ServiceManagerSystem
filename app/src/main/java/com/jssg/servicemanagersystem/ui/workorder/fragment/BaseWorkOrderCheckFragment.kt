package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.core.AppApplication
import com.jssg.servicemanagersystem.databinding.FragmentBaseWorkOrderCheckBinding
import com.jssg.servicemanagersystem.databinding.ItemImageViewBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.workorder.adapter.ApplyInfoAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.UploadEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.PicturesPreviewActivity
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.SelectorPictureDialog
import com.jssg.servicemanagersystem.ui.workorder.selectorpicture.SelectorPictureViewModel
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.FileUtils
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.jssg.servicemanagersystem.widgets.decoration.ThemeLineItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.Locale

abstract class BaseWorkOrderCheckFragment : BaseFragment() {

    protected var uploadSize: Int = 0
    protected var checkDate: String? = null
    protected var state: Int = 0
    protected val selectPictures = arrayListOf<UploadEntity>()
    protected lateinit var selectPicturesViewModel: SelectorPictureViewModel
    protected lateinit var workOrderViewModel: WorkOrderViewModel
    protected var inputData: WorkOrderCheckInfo? = null
    protected lateinit var binding: FragmentBaseWorkOrderCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseWorkOrderCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputData = arguments?.getParcelable("input")

        initViewVisible()

        initViewModel()

        initData()

        addListener()
    }

    private fun addListener() {
        binding.ivAddBadPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance(0)
                .show(childFragmentManager, "selector_picture_dialog")
        }

        binding.ivAddBoxPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance(1)
                .show(childFragmentManager, "selector_picture_dialog")
        }

        binding.ivAddBatchInfoPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance(2)
                .show(childFragmentManager, "selector_picture_dialog")
        }

        binding.ivAddReworkPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance(3)
                .show(childFragmentManager, "selector_picture_dialog")
        }

        binding.mbtSave.setOnClickListener {
            onSubmit(0)
        }

        binding.mbtSubmit.setOnClickListener {
            onSubmit(1)
        }

        binding.mbtReview.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKDERDETAIL_APPROVE.printableName)) return@setOnClickListener

            inputData?.let {
                WorkOrderCheckDialogFragment.newInstance(it)
                    .addOnFinishListener(object : WorkOrderCheckDialogFragment.OnFinishListener {
                        override fun onFinish() {
                            requireActivity().setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra("output", true)
                            })
                            requireActivity().finish()
                        }

                    })
                    .show(childFragmentManager, "check_order_dialog")
            }
        }
    }

    private fun initViewModel() {
        workOrderViewModel = ViewModelProvider(requireActivity())[WorkOrderViewModel::class.java]
        selectPicturesViewModel =
            ViewModelProvider(requireActivity())[SelectorPictureViewModel::class.java]

        //网络图片加载
        selectPicturesViewModel.badOssListLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                result.data.forEach {
                    it.tag = "bad" + it.url
                    selectPictures.add(it)
                    initImageWidget("bad", it.url, binding.xflBadPicture, binding.ivAddBadPhoto)
                }
                binding.ivAddBadPhoto.isVisible =
                    binding.xflBadPicture.childCount < 3 && isAddPictureEnable
            }
        }

        selectPicturesViewModel.boxOssListLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                result.data.forEach {
                    it.tag = "box" + it.url
                    selectPictures.add(it)
                    initImageWidget("box", it.url, binding.xflBoxPicture, binding.ivAddBadPhoto)
                }
                binding.ivAddBoxPhoto.isVisible =
                    binding.xflBoxPicture.childCount < 3 && isAddPictureEnable
            }
        }

        selectPicturesViewModel.batchOssListLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                result.data.forEach {
                    it.tag = "batch" + it.url
                    selectPictures.add(it)
                    initImageWidget(
                        "batch",
                        it.url,
                        binding.xflBatchInfoPicture,
                        binding.ivAddBadPhoto
                    )
                }
                binding.ivAddBatchInfoPhoto.isVisible =
                    binding.xflBatchInfoPicture.childCount < 3 && isAddPictureEnable
            }
        }

        selectPicturesViewModel.reworkOssListLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                result.data.forEach {
                    it.tag = "rework" + it.url
                    selectPictures.add(it)
                    initImageWidget(
                        "rework",
                        it.url,
                        binding.xflReworkPicture,
                        binding.ivAddBadPhoto
                    )
                }
                binding.ivAddReworkPhoto.isVisible =
                    binding.xflReworkPicture.childCount < 5 && isAddPictureEnable
            }
        }

        //本地图片选择
        selectPicturesViewModel.badPicturesLiveData.observe(viewLifecycleOwner) { pictures ->
            if (binding.xflBadPicture.childCount >= 3) {
                ToastUtils.showToast("不良图片最多只能选择3张！")
                return@observe
            }

            if (pictures.size > 3) {
                ToastUtils.showToast("不良图片最多只能选择3张！")
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
                    initImageWidget(
                        "bad",
                        it.availablePath,
                        binding.xflBadPicture,
                        binding.ivAddBadPhoto
                    )
                }
            }

            binding.ivAddBadPhoto.isVisible =
                binding.xflBadPicture.childCount < 3 && isAddPictureEnable
        }

        selectPicturesViewModel.boxPicturesLiveData.observe(viewLifecycleOwner) { pictures ->
            if (binding.xflBoxPicture.childCount >= 3) {
                ToastUtils.showToast("外箱标签图片最多只能选择3张！")
                return@observe
            }

            if (pictures.size > 3) {
                ToastUtils.showToast("外箱标签图片最多只能选择3张！")
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
                    initImageWidget(
                        "box",
                        it.availablePath,
                        binding.xflBoxPicture,
                        binding.ivAddBoxPhoto
                    )
                }
            }

            binding.ivAddBoxPhoto.isVisible =
                binding.xflBoxPicture.childCount < 3 && isAddPictureEnable
        }

        selectPicturesViewModel.batchInfoPicturesLiveData.observe(viewLifecycleOwner) { pictures ->
            if (binding.xflBatchInfoPicture.childCount >= 3) {
                ToastUtils.showToast("批次信息图片最多只能选择3张！")
                return@observe
            }

            if (pictures.size > 3) {
                ToastUtils.showToast("批次信息图片最多只能选择3张！")
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
                    initImageWidget(
                        "batch",
                        it.availablePath,
                        binding.xflBatchInfoPicture,
                        binding.ivAddBatchInfoPhoto
                    )
                }
            }

            binding.ivAddBatchInfoPhoto.isVisible =
                binding.xflBatchInfoPicture.childCount < 3 && isAddPictureEnable
        }

        selectPicturesViewModel.reworkPicturesLiveData.observe(viewLifecycleOwner) { pictures ->
            if (binding.xflReworkPicture.childCount >= 5) {
                ToastUtils.showToast("返回数量图片最多只能选择5张！")
                return@observe
            }

            if (pictures.size > 5) {
                ToastUtils.showToast("返回数量图片最多只能选择5张！")
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
                    initImageWidget(
                        "rework",
                        it.availablePath,
                        binding.xflReworkPicture,
                        binding.ivAddReworkPhoto
                    )
                }
            }

            binding.ivAddReworkPhoto.isVisible =
                binding.xflReworkPicture.childCount < 5 && isAddPictureEnable
        }

        selectPicturesViewModel.fileOssUploadLiveData.observe(viewLifecycleOwner) { result ->
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

        workOrderViewModel.updateWorkOrderDetailLiveData.observe(viewLifecycleOwner) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                if (state == 0) {
                    ToastUtils.showToast("保存成功")
                } else {
                    ToastUtils.showToast("提交成功")
                }

                requireActivity().setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("output", true)
                })
                requireActivity().finish()
            }
        }
    }

    private fun onSubmit(state: Int) {
        if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_EDIT.printableName)) return

        this.state = state
        val locationStr = binding.tvLocationAddress.text.toString()
        if (locationStr.isEmpty()) {
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

            if (it.state > 0) {
                //已经有审核意见且审核数据不为空
                if (!it.applyInfoVos.isNullOrEmpty()) {
                    binding.layoutReviewRemark.isVisible = true

                    binding.recyclerView.layoutManager =
                        object : LinearLayoutManager(requireContext()) {
                            override fun canScrollVertically(): Boolean {
                                return false
                            }
                        }
                    binding.recyclerView.addItemDecoration(
                        ThemeLineItemDecoration(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.x_divider,
                                null
                            ), 1, 0
                        )
                    )
                    val applyInfoAdapter = ApplyInfoAdapter()
                    binding.recyclerView.adapter = applyInfoAdapter

                    applyInfoAdapter.setList(it.applyInfoVos)
                }
            }

            binding.etBadNum.isEnabled = isEditable
            binding.etCheckNum.isEnabled = isEditable
            binding.etRemark.isEnabled = isEditable

            selectPicturesViewModel.getBadPictures(it.badPicNames)
            selectPicturesViewModel.getBoxPictures(it.boxPicNames)
            selectPicturesViewModel.getBatchPictures(it.batchPicNames)
            selectPicturesViewModel.getReworkPictures(it.reworkPicNames)
        }
    }

    private fun initImageWidget(tag: String, url: String, parent: ViewGroup, addView: ImageView) {
        val img = layoutInflater.inflate(R.layout.item_image_view, null)
        val bind = ItemImageViewBinding.bind(img)

        if (url.startsWith("http") || url.startsWith("content")) {
            Glide.with(this).load(url).into(bind.ivImage)
        } else {
            Glide.with(this).load(File(url)).into(bind.ivImage)
        }
        img.tag = tag + url

        if (checkDate.isNullOrEmpty() && tag.equals("rework", false)) {
            checkDate = DateUtil.getFullData(System.currentTimeMillis())
            binding.tvCheckDate.text = checkDate
        }

        img.setOnLongClickListener {

            if (!isPictureLongClickable) return@setOnLongClickListener true

            SingleBtnDialogFragment.newInstance("删除图片", "确定要删除图片吗？")
                .addConfrimClickLisntener(object : SingleBtnDialogFragment.OnConfirmClickLisenter {
                    override fun onConfrimClick() {
                        parent.removeView(it)
                        addView.isVisible = true

                        selectPictures.removeIf { p -> p.tag.equals(img.tag.toString(), false) }
                    }

                })
                .show(childFragmentManager, "delete")

            return@setOnLongClickListener true
        }

        img.setOnClickListener {//预览
            PicturesPreviewActivity.goActivity(requireActivity(), url)
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
        Luban.with(requireContext())
            .load(file)
            .ignoreBy(100)
            .setTargetDir(requireContext().cacheDir.absolutePath)
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

    abstract fun initViewVisible()

    //图片是否可以长按删除
    var isPictureLongClickable: Boolean = false

    //添加图片按钮是否可用
    var isAddPictureEnable: Boolean = false

    //输入框是否可编辑
    var isEditable: Boolean = false
}