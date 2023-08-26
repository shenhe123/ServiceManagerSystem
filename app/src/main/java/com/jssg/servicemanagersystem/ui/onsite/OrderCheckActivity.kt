package com.jssg.servicemanagersystem.ui.onsite

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityOrderCheckBinding
import com.jssg.servicemanagersystem.utils.MyLocationClient
import net.arvin.permissionhelper.PermissionHelper

class OrderCheckActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderCheckBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, OrderCheckActivity::class.java))
        }
    }

}