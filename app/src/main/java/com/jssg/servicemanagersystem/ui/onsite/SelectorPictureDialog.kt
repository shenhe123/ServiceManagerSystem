package com.jssg.servicemanagersystem.ui.onsite

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssg.servicemanagersystem.base.BaseBottomSheetDialogFragment
import com.jssg.servicemanagersystem.databinding.DialogSelectorPictureBinding
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import net.arvin.permissionhelper.PermissionHelper


/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class SelectorPictureDialog: BaseBottomSheetDialogFragment() {

    private lateinit var binding: DialogSelectorPictureBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSelectorPictureBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permissionHelper = PermissionHelper.Builder().with(this).build()

        binding.tvTakePhoto.setOnClickListener {
            permissionHelper.request("需要拍照权限", Manifest.permission.CAMERA
            ) { granted, isAlwaysDenied ->
                if (granted) {
                    PictureSelector.create(this)
                        .openCamera(SelectMimeType.ofImage())
                        .forResult(object : OnResultCallbackListener<LocalMedia?> {
                            override fun onResult(result: ArrayList<LocalMedia?>) {}
                            override fun onCancel() {}
                        })
                }
            }
        }

        binding.tvChoosePic.setOnClickListener {
            permissionHelper.request("需要文件读写权限", Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) { granted, isAlwaysDenied ->
                if (granted) {
                    PictureSelector.create(this)
                        .openSystemGallery(SelectMimeType.ofImage())
                        .forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
                            override fun onResult(result: ArrayList<LocalMedia?>) {}
                            override fun onCancel() {}
                        })
                }
            }
        }

    }

    companion object {
        fun newInstance(): SelectorPictureDialog {
            val args = Bundle()

            val fragment = SelectorPictureDialog()
            fragment.arguments = args
            return fragment
        }
    }
}