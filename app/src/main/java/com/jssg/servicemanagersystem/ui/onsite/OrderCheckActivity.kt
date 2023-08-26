package com.jssg.servicemanagersystem.ui.onsite

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.bumptech.glide.Glide
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityOrderCheckBinding
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.onsite.selectorpicture.PicturesPreviewActivity
import com.jssg.servicemanagersystem.ui.onsite.selectorpicture.SelectorPictureDialog
import com.jssg.servicemanagersystem.ui.onsite.selectorpicture.SelectorPictureViewModel
import com.jssg.servicemanagersystem.utils.DpPxUtils
import com.jssg.servicemanagersystem.utils.MyLocationClient
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.luck.picture.lib.entity.LocalMedia
import net.arvin.permissionhelper.PermissionHelper
import java.io.File

class OrderCheckActivity : BaseActivity() {
    private lateinit var selectorPictureViewModel: SelectorPictureViewModel
    private lateinit var binding: ActivityOrderCheckBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        selectorPictureViewModel = ViewModelProvider(this).get(SelectorPictureViewModel::class.java)

        selectorPictureViewModel.badPicturesLiveData.observe(this) { pictures ->
            if (binding.xflBadPicture.childCount >= 3) {
                ToastUtils.showToast("不良图片最多只能选择3张！")
                return@observe
            }

            val availablePic: List<LocalMedia?> = if (pictures.size >= 3) {
                pictures.take(3)
            } else {
                pictures
            }

            availablePic.forEach { localMedia ->
                localMedia?.let {
                    initImageWidget(it.availablePath, binding.xflBadPicture, binding.ivAddBadPhoto)
                }
            }

            binding.ivAddBadPhoto.isVisible = binding.xflBadPicture.childCount < 3
        }


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

        binding.ivAddBadPhoto.setOnClickListener {
            SelectorPictureDialog.newInstance().show(supportFragmentManager, "selector_picture_dialog")
        }
    }

    private fun initImageWidget(path: String, parent: ViewGroup, addView: ImageView) {
        val img = ImageView(this)
        val width = DpPxUtils.dip2px(this, 66f)
        img.layoutParams = LinearLayout.LayoutParams(width, width)
        img.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(this).load(File(path)).into(img)
        img.setOnLongClickListener {

            SingleBtnDialogFragment.newInstance("删除图片", "确定要删除图片吗？")
                .addConfrimClickLisntener(object :SingleBtnDialogFragment.OnConfirmClickLisenter{
                    override fun onConfrimClick() {
                        parent.removeView(it)
                        if (parent.childCount == 0) {
                            addView.isVisible = true
                        }
                    }

                })
                .show(supportFragmentManager, "delete")

            return@setOnLongClickListener true
        }

        img.setOnClickListener {//预览
            PicturesPreviewActivity.goActivity(this, path)
        }
        parent.addView(img)
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, OrderCheckActivity::class.java))
        }
    }

}